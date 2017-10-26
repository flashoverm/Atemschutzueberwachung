package de.thral.atemschutzueberwachung.Monitoring;

import android.os.CountDownTimer;

import java.util.ArrayList;
import java.util.List;

import de.thral.atemschutzueberwachung.Draegerman.Draegerman;

/**
 * Created by Markus Thral on 21.10.2017.
 */

public class Squad {

    public enum Order {Explore, Firefighting, Search, Protection , Other};

    public enum OperationTime {Short, Normal, Long};

    private static final int RETURN_WARNING_PERCENTAGE = 120;
    private static final int RETURN_SAFETY_FACTOR = 2;

    private final Draegerman leader;
    private final Draegerman member;

    private int leaderReturnPressure;
    private int memberReturnPressure;

    private final Squad.Order order;

    private final long operationTime;
    private CountDownTimer timer;
    private long timerValue;

    //Better as Array?
    private List<Event> eventList;

    public Squad(Draegerman leader, int initialPressureLeader,
                 Draegerman member, int initialPressureMember,
                 Squad.OperationTime operationTime, Squad.Order order){
        this.leader = leader;
        this.member = member;
        this.order = order;
        this.eventList = new ArrayList<>();

        switch(operationTime) {
            case Short: this.operationTime = 20*60*1000;
                break;
            case Long:  this.operationTime = 60*60*1000;
                break;
            default:    this.operationTime = 30*60*1000;
        }
        register(initialPressureLeader, initialPressureMember);
    }

    public Draegerman getLeader() {
        return leader;
    }

    public Draegerman getMember() {
        return member;
    }

    public int getLeaderReturnPressure() {
        return leaderReturnPressure;
    }

    public int getMemberReturnPressure() {
        return memberReturnPressure;
    }

    public Squad.Order getOrder() {
        return order;
    }

    public long getTimerValue() {
        return timerValue;
    }

    public List<Event> getEvents() {
        return eventList;
    }

    public Event.Type getLastEventType() {
        return eventList.get(eventList.size()-1).getEventType();
    }

    public boolean isEventExisting(Event.Type eventType){
        for(Event event : this.getEvents()){
            if(event.getEventType().equals(eventType)){
                return true;
            }
        }
        return false;
    }

    private void register(int initialPressureLeader, int initialPressureMember){
        timerValue = this.operationTime;
        createTimer(timerValue);
        addPressureValues(Event.Type.Register, initialPressureLeader,
                initialPressureMember);
    }

    private void createTimer(long startValue){
        timer = new CountDownTimer(startValue, 1000) {

            public void onTick(long millisUntilFinished) {
                timerValue = millisUntilFinished;
                if(timerValue ==  operationTime*2/3){
                    //TODO 1/3 vergangen ->
                } else if(timerValue == operationTime/3){
                    //TODO 2/3 vergangen ->
                }
            }

            public void onFinish() {
                timerValue = 0;
                //TODO Timer abgelaufen
            }
        };
    }

    public boolean beginOperation(int pressureLeader, int pressureMember){
        if(addPressureValues(Event.Type.Arrived, pressureLeader, pressureMember)){
            timer.start();
            return true;
        }
        return false;
    }

    public boolean arriveOperationTarget(int pressureLeader, int pressureMember){
        if(addPressureValues(Event.Type.Arrived, pressureLeader, pressureMember)){
            for(Event event : eventList){
                if(event.getEventType().equals(Event.Type.Register)){
                    this.leaderReturnPressure =
                            RETURN_SAFETY_FACTOR * (event.getPressureLeader()-pressureLeader);
                    this.memberReturnPressure =
                            RETURN_SAFETY_FACTOR * (event.getPressureMember()-pressureMember);
                    break;
                }
            }
            return true;
        }
        return false;
    }

    public boolean addPressureValues(Event.Type eventType, int pressureLeader, int pressureMember){
        if(!isEventExisting(eventType)){
            eventList.add(new Event(eventType, pressureLeader, pressureMember, getTimerValue()));
            if(pressureLeader <= leaderReturnPressure
                    || pressureMember <= memberReturnPressure){
                //TODO Alarm! Druch zu niedrig
            } else if(pressureLeader <= (leaderReturnPressure*RETURN_WARNING_PERCENTAGE/100)
                    || pressureMember <= (memberReturnPressure*RETURN_WARNING_PERCENTAGE/100)){
                //TODO Warning! Druck bald bei Mindestgrenzen
            }
            return true;
        }
        return false;
    }

    public boolean pauseOperation(){
        eventList.add(new Event(Event.Type.TimerStopped, getTimerValue()));
        timer.cancel();
        return true;
    }

    public boolean resumeOperation(){
        eventList.add(new Event(Event.Type.TimerResumed, getTimerValue()));
        createTimer(timerValue);
        timer.start();
        return true;
    }

    public boolean endOperation(int pressureLeader, int pressureMember){
        if(addPressureValues(Event.Type.End, pressureLeader, pressureMember)){
            timer.cancel();
            return true;
        }
        return false;
    }
}

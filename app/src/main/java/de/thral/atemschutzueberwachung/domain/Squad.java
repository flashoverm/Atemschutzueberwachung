package de.thral.atemschutzueberwachung.domain;

import android.os.CountDownTimer;

import java.util.ArrayList;
import java.util.List;

public class Squad {

    private static final int RETURN_WARNING_PERCENTAGE = 120;
    private static final int RETURN_SAFETY_FACTOR = 2;

    private final String name;
    private final Draegerman leader;
    private final Draegerman member;
    private final Order order;

    private int leaderReturnPressure;
    private int memberReturnPressure;

    private final long operatingTime;
    private final int secOneThird;
    private final int secTwoThird;
    private transient CountDownTimer timer;
    private long timerValue;
    private boolean reminder;
    private List<Event> eventList;

    private SquadChangeListener changeListener;
    private TimerChangeListener timerListener;

    public Squad(String name, Draegerman leader, int initialPressureLeader,
                 Draegerman member, int initialPressureMember,
                 OperatingTime operatingTime, Order order){
        this.name = name;
        this.leader = leader;
        this.member = member;
        this.order = order;
        this.leaderReturnPressure = -1;
        this.memberReturnPressure = -1;
        this.eventList = new ArrayList<>();
        this.operatingTime = operatingTime.getTime()*60*1000;
        this.secOneThird = operatingTime.getTime()*20;
        this.secTwoThird = operatingTime.getTime()*30;
        this.reminder = false;

        register(initialPressureLeader, initialPressureMember);
    }

    public String getName() {
        return name;
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

    public Order getOrder() {
        return order;
    }

    public long getTimerValue() {
        return timerValue;
    }

    public String getTimerValueAsClock() {
        String clock = "";

        int seconds = (int)timerValue/1000;
        int minutes = seconds/60;
        seconds = seconds - minutes*60;

        if(minutes < 10){
            clock = "0" + minutes;
        } else {
            clock = minutes+"";
        }
        clock += ":";
        if(seconds <10){
            clock += "0"+seconds;
        } else {
            clock += seconds+"";
        }
        return clock;
    }

    public boolean isReminderActive(){
        return reminder;
    }

    public List<Event> getEvents() {
        return eventList;
    }

    public EventType getState() {
        Event event;
        for(int i=0; i<3; i++){
            event = eventList.get(i);
            if(event.getType() == EventType.ResumeTimer){
                i++;
            } else if(event.getType() != EventType.Timer){
                return event.getType();
            }
        }
        return null;
    }

    public Event getLastPressureValue(){
        for(Event event : eventList) {
            if (event.getPressureLeader() != -1) {
                return event;
            }
        }
        return null;
    }

    public Event[] getLastPressureValues(){
        Event[] last = new Event[3];
        int j=0;
        Event event;
        for(int i=0; i<eventList.size() && j<3; i++){
            event = eventList.get(i);
            if(event.getPressureLeader() != -1){
                last[j] = event;
                j++;
            }
        }
        while(j<3){
            last[j] = null;
            j++;
        }
        return last;
    }

    public boolean isEventExisting(EventType eventType){
        for(Event event : this.getEvents()){
            if(event.getType().equals(eventType)){
                return true;
            }
        }
        return false;
    }

    public void setChangeListener(SquadChangeListener changeListener){
        this.changeListener = changeListener;
    }

    public void setTimerListener(TimerChangeListener timerListener){
        this.timerListener = timerListener;
    }

    private void createTimer(long startValue){
        timer = new CountDownTimer(startValue, 1000) {
            public void onTick(long millisUntilFinished) {
                timerValue = millisUntilFinished;
                timerListener.onTimerUpdate(Squad.this);

                if((timerValue/1000) == secTwoThird || (timerValue/1000) == secOneThird){
                    reminder = true;
                    timerListener.onTimerReachedMark(Squad.this, false);
                }
            }
            public void onFinish() {
                timerValue = 0;
                timerListener.onTimerUpdate(Squad.this);
                timerListener.onTimerReachedMark(Squad.this, true);
            }
        };
    }

    public void runTimer(){
        createTimer(timerValue);
        timer.start();
    }

    private void register(int initialPressureLeader, int initialPressureMember){
        timerValue = this.operatingTime;
        createTimer(timerValue);
        addPressureValues(EventType.Register, initialPressureLeader,
                initialPressureMember);
    }

    public boolean beginOperation(){
        if(!isEventExisting(EventType.Begin)){
            eventList.add(0, new Event(EventType.Begin, getTimerValue()));
            changeListener.onStateUpdate(this);
            runTimer();
            return true;
        }
        return false;
    }

    public boolean arriveTarget(int pressureLeader, int pressureMember){
        if(addPressureValues(EventType.Arrive, pressureLeader, pressureMember)){
            for(Event event : eventList){
                if(event.getType().equals(EventType.Register)){
                    this.leaderReturnPressure =
                            RETURN_SAFETY_FACTOR * (event.getPressureLeader()-pressureLeader);
                    this.memberReturnPressure =
                            RETURN_SAFETY_FACTOR * (event.getPressureMember()-pressureMember);
                    break;
                }
            }
            changeListener.onCalculatedReturnPressure(Squad.this);
            return true;
        }
        return false;
    }

    public boolean addPressureValues(EventType eventType, int pressureLeader, int pressureMember){
        if(!isEventExisting(eventType) || eventType == EventType.Timer){
            eventList.add(0, new Event(eventType, pressureLeader, pressureMember, getTimerValue()));
            reminder = false;
            if(changeListener != null){
                changeListener.onStateUpdate(this);
                changeListener.onPressureUpdate(Squad.this);
            }

            if(pressureLeader < leaderReturnPressure
                    || pressureMember < memberReturnPressure){
                changeListener.onPressureInfo(Squad.this, true);
            } else if(pressureLeader <= (leaderReturnPressure*RETURN_WARNING_PERCENTAGE/100)
                    || pressureMember <= (memberReturnPressure*RETURN_WARNING_PERCENTAGE/100)){
                changeListener.onPressureInfo(Squad.this, false);
            }
            return true;
        }
        return false;
    }

    public boolean pauseOperation(){
        eventList.add(0, new Event(EventType.PauseTimer, getTimerValue()));
        changeListener.onStateUpdate(this);
        timer.cancel();
        return true;
    }

    public boolean resumeOperation(){
        eventList.add(0, new Event(EventType.ResumeTimer, getTimerValue()));
        changeListener.onStateUpdate(this);
        runTimer();
        return true;
    }

    public boolean retreat(int pressureLeader, int pressureMember){
        return addPressureValues(EventType.Retreat, pressureLeader, pressureMember);
    }

    public boolean endOperation(int pressureLeader, int pressureMember){
        if(addPressureValues(EventType.End, pressureLeader, pressureMember)){
            timer.cancel();
            return true;
        }
        return false;
    }
}

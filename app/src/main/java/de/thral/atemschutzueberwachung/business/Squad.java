package de.thral.atemschutzueberwachung.business;

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

    private OperationTimer operationTimer;
    private List<Event> eventList;

    private transient SquadChangeListener changeListener;

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
        this.operationTimer = new OperationTimer(operatingTime);

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

    public EventType getState() {
        Event event;
        for(int i=0; i<eventList.size(); i++){
            event = eventList.get(i);
            if(event.getType() == EventType.ResumeTimer){
                i++;
            } else if(event.getType() != EventType.Timer){
                return event.getType();
            }
        }
        return null;
    }

    public Event[] getLastPressureValues(int count){
        Event[] last = new Event[count];
        int j=0;
        Event event;
        for(int i=0; i<eventList.size() && j<count; i++){
            event = eventList.get(i);
            if(event.getPressureLeader() != -1){
                last[j] = event;
                j++;
            }
        }
        while(j<count){
            last[j] = null;
            j++;
        }
        return last;
    }

    public boolean isEventExisting(EventType eventType){
        for(Event event : eventList){
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
        operationTimer.setTimerListener(timerListener);
    }

    /*
       Timer functions
     */

    public String getTimerValueAsClock(){
        return operationTimer.getValueAsClock();
    }

    public boolean isReminderActive(){
        return operationTimer.isReminderActive();
    }

    public boolean isTimerExpired(){
        return (operationTimer.getValue() == 0);
    }

    public boolean isAlarmUnconfirmed(){
        return operationTimer.isAlarmUnconfirmed();
    }

    public void confirmAlarm(){
        operationTimer.confirmAlarm();
    }

    public void resumeAfterError(){
        operationTimer.resumeAfterError();
    }

    /*
        Squad functions
     */

    private void register(int initialPressureLeader, int initialPressureMember){
        addPressureValues(EventType.Register, initialPressureLeader,
                initialPressureMember);
    }

    public boolean beginOperation(){
        if(!isEventExisting(EventType.Begin)){
            eventList.add(0, new Event(EventType.Begin, operationTimer.getValue()));
            changeListener.onStateUpdate(this);
            operationTimer.start();
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
            eventList.add(0, new Event(eventType, pressureLeader, pressureMember, operationTimer.getValue()));
            operationTimer.deactiveReminder();
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
        eventList.add(0, new Event(EventType.PauseTimer, operationTimer.getValue()));
        changeListener.onStateUpdate(this);
        operationTimer.cancel();
        return true;
    }

    public boolean resumeOperation(){
        eventList.add(0, new Event(EventType.ResumeTimer, operationTimer.getValue()));
        operationTimer.start();
        changeListener.onStateUpdate(this);
        return true;
    }

    public boolean retreat(int pressureLeader, int pressureMember){
        return addPressureValues(EventType.Retreat, pressureLeader, pressureMember);
    }

    public boolean endOperation(int pressureLeader, int pressureMember){
        if(addPressureValues(EventType.End, pressureLeader, pressureMember)){
            operationTimer.cancel();
            return true;
        }
        return false;
    }
}

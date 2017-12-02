package de.thral.atemschutzueberwachung.business;

public class Event {

    private final EventType type;
    private final long remainingOperationTime;
    private final long timeStamp;
    private final int pressureLeader;
    private final int pressureMember;

    public Event(EventType type, int pressureLeader,
                 int pressureMember, long remainingOperationTime) {
        this.type = type;
        this.remainingOperationTime = remainingOperationTime;
        this.timeStamp = System.currentTimeMillis();
        this.pressureLeader = pressureLeader;
        this.pressureMember = pressureMember;
    }

    public Event(EventType type, long remainingOperationTime) {
        this.type = type;
        this.remainingOperationTime = remainingOperationTime;
        this.timeStamp = System.currentTimeMillis();
        this.pressureLeader = -1;
        this.pressureMember = -1;
    }

    public EventType getType() {
        return type;
    }

    public long getRemainingOperationTime() {
        return remainingOperationTime;
    }

    public String getTimeStampAsClock() {
        String clock = "";

        int seconds = (int)timeStamp/1000;
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

    public int getPressureLeader() {
        return pressureLeader;
    }

    public int getPressureMember() {
        return pressureMember;
    }
}

package de.thral.atemschutzueberwachung.Monitoring;

/**
 * Created by Markus Thral on 21.10.2017.
 */

public class Event {

    public enum Type {Register, Begin, Arrived, OneThird, TwoThirds,
        Retreat, End, TimerStopped, TimerResumed, Other}

    private final Type eventType;

    private final long remainingOperationTime;
    private final long timeStamp;

    private final int pressureLeader;
    private final int pressureMember;

    public Event(Type eventType, int pressureLeader,
                 int pressureMember, long remainingOperationTime) {
        this.eventType = eventType;
        this.remainingOperationTime = remainingOperationTime;
        this.timeStamp = System.currentTimeMillis();
        this.pressureLeader = pressureLeader;
        this.pressureMember = pressureMember;
    }

    public Event(Type eventType, long remainingOperationTime) {
        this.eventType = eventType;
        this.remainingOperationTime = remainingOperationTime;
        this.timeStamp = System.currentTimeMillis();
        this.pressureLeader = -1;
        this.pressureMember = -1;
    }

    public Type getEventType() {
        return eventType;
    }

    public long getRemainingOperationTime() {
        return remainingOperationTime;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getPressureLeader() {
        return pressureLeader;
    }

    public int getPressureMember() {
        return pressureMember;
    }
}

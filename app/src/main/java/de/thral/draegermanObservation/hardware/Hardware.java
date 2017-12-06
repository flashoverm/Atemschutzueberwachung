package de.thral.draegermanObservation.hardware;

public interface Hardware {

    void init();
    boolean isAvailable();
    void turnOn(long activeMillis, long pauseMillis);
    void turnOff();
}

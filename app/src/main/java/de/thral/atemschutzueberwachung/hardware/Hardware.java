package de.thral.atemschutzueberwachung.hardware;

public interface Hardware {

    void init();
    void turnOn(long activeMillis, long pauseMillis);
    void turnOff();
}

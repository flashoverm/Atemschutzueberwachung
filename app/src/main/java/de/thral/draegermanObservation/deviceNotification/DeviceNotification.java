package de.thral.draegermanObservation.deviceNotification;

public interface DeviceNotification {

    String LOG_TAG = "DEVICE_NOTIFICATION";

    void init();
    boolean isAvailable();
    void turnOn(long activeMillis, long pauseMillis);
    void turnOff();
}

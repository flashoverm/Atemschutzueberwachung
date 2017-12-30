package de.thral.draegermanObservation.notification;

public interface NotificationComponent {

    String LOG_TAG = "NOTIFICATION_MANAGEMENT";

    void init();
    boolean isAvailable();
    void turnOn(long activeMillis, long pauseMillis);
    void turnOff();
}

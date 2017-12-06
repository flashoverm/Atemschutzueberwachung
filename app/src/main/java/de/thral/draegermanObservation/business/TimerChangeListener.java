package de.thral.draegermanObservation.business;

public interface TimerChangeListener {

    void onTimerUpdate(String timer);
    void onTimerReachedMark(boolean expired);

}

package de.thral.atemschutzueberwachung.business;

public interface TimerChangeListener {

    void onTimerUpdate(String timer);
    void onTimerReachedMark(boolean expired);

}

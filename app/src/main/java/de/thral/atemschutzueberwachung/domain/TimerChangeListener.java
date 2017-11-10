package de.thral.atemschutzueberwachung.domain;

/**
 * Created by Markus Thral on 04.11.2017.
 */

public interface TimerChangeListener {

    void onTimerUpdate(String timer);
    void onTimerReachedMark(boolean expired);

}

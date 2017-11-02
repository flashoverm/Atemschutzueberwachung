package de.thral.atemschutzueberwachung.domain;

/**
 * Created by Markus Thral on 28.10.2017.
 */

public interface SquadChangeListener {

    void onTimerUpdate(Squad squad);
    void onStateUpdate(Squad squad);
    void onPressureUpdate(Squad squad);
    void onCalculatedReturnPressure(Squad squad);

    void onTimerReachedMark(Squad squad, boolean expired);
    void onPressureInfo(Squad squad, boolean underShot);
}

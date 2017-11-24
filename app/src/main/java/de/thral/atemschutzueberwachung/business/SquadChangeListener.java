package de.thral.atemschutzueberwachung.business;

/**
 * Created by Markus Thral on 28.10.2017.
 */

public interface SquadChangeListener {

    void onStateUpdate(Squad squad);
    void onPressureUpdate(Squad squad);
    void onCalculatedReturnPressure(Squad squad);

    void onPressureInfo(Squad squad, boolean underShot);
}

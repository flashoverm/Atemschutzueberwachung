package de.thral.atemschutzueberwachung.business;

public interface SquadChangeListener {

    void onStateUpdate(Squad squad);
    void onPressureUpdate(Squad squad);
    void onCalculatedReturnPressure(Squad squad);

    void onPressureInfo(Squad squad, boolean underShot);
}

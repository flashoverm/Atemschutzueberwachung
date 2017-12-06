package de.thral.draegermanObservation.business;

public interface SquadChangeListener {

    void onStateUpdate(Squad squad);
    void onPressureUpdate(Squad squad);
    void onCalculatedReturnPressure(Squad squad);

    void onPressureInfo(Squad squad, boolean underShot);
}

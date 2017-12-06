package de.thral.draegermanObservation.hardware;

import android.content.Context;
import android.os.Vibrator;

public class Vibration implements Hardware {

    private Context context;

    private boolean vibratorAvailable;
    private boolean vibratorState;

    private Vibrator vibrator;

    public Vibration(Context context){
        this.context = context;
        init();
    }

    @Override
    public void init() {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibratorAvailable = vibrator.hasVibrator();
    }

    @Override
    public boolean isAvailable(){
        return vibratorAvailable;
    }

    @Override
    public void turnOn(long activeMillis, long pauseMillis) {
        if(vibratorAvailable && !vibratorState){
            vibrator.vibrate(new long[]{0, activeMillis, pauseMillis}, 0);
            vibratorState = true;
        }
    }

    @Override
    public void turnOff() {
        if(vibratorAvailable && vibratorState){
            vibrator.cancel();
            vibratorState = false;
        }
    }
}

package de.thral.atemschutzueberwachung.hardware;

import android.content.Context;
import android.os.Vibrator;

public class Vibrate implements Hardware {

    private Context context;

    private boolean vibratorAvailable;
    private boolean vibratorState;

    private Vibrator vibrator;

    public Vibrate(Context context){
        this.context = context;
        init();
    }

    @Override
    public void init() {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibratorAvailable = vibrator.hasVibrator();
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

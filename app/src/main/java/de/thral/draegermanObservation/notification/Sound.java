package de.thral.draegermanObservation.notification;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.util.Log;

public class Sound implements NotificationComponent {

    private boolean soundState;

    private ToneGenerator toneGen;

    private Thread soundThread;
    private Runnable toogleSound;

    public Sound(){
        init();
    }

    @Override
    public void init() {
        toneGen = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
    }

    @Override
    public boolean isAvailable(){
        return true;
    }

    @Override
    public void turnOn(final long activeMillis, final long pauseMillis) {
        if(!soundState){
            toogleSound = new Runnable() {
                public void run() {
                    try {
                        while(soundState && !Thread.currentThread().isInterrupted()){
                            toneGen.startTone(
                                    ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, (int) activeMillis);
                            Thread.sleep(pauseMillis);
                        }
                    } catch (InterruptedException e){
                        Log.e(LOG_TAG, e.getMessage());
                        Thread.currentThread().interrupt();
                    }
                }
            };
            soundThread = new Thread(toogleSound);
            soundState = true;
            soundThread.start();
        }
    }

    @Override
    public void turnOff() {
        soundState = false;
    }
}

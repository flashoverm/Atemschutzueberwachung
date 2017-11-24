package de.thral.atemschutzueberwachung.hardware;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Handler;

public class Sound implements Hardware {

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
    public void turnOn(final long activeMillis, final long pauseMillis) {
        if(!soundState){
            toogleSound = new Runnable() {
                public void run() {
                    try {
                        while(soundState){
                            toneGen.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, (int) activeMillis);
                            Thread.sleep(pauseMillis);
                        }
                    } catch (Exception e){
                        e.printStackTrace();
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

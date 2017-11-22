package de.thral.atemschutzueberwachung;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;

public class HardwareInterface {

    //TODO change states to mode: off -> reminder/alarm is allowed | reminder -> alarm is allowd
    //Generalized function for reminder / alarm  //Off: Turns all of


    private Context context;

    private boolean alarmState;
    private boolean reminderState;

    private boolean flashAvailable;
    private CameraManager cameraManager;
    private String cameraID;
    private Handler flashHandler;
    private Runnable toggleFlash;
    private boolean flashState;
    private boolean flashBlinkState;

    private boolean vibratorAvailable;
    private Vibrator vibrator;
    private boolean vibratorState;

    private ToneGenerator toneGen;
    private Handler soundHandler;
    private Runnable toogleSound;
    private boolean soundState;

    public HardwareInterface(Context context){
        this.context = context;

        initFlash();
        initVibrator();
        initSound();
    }

    public void turnOnReminder(){
        if(!reminderState){
            reminderState = true;
            if(!alarmState){
                turnOnFlashBlink(1000);
                turnOnVibration(500, 3000);
                turnOnSound(600, 6000);
            }
        }
    }

    public void turnOnAlarm(){
        if(!alarmState){
            if(reminderState){
                turnOffReminder();
            }
            alarmState = true;
            turnOnFlashBlink(100);
            turnOnVibration(500, 500);
            turnOnSound(600, 2000);
        }
    }

    public void turnOffAlarm(){
        if(alarmState){
            alarmState = false;
            turnOffFlashBlink();
            turnOffVibration();
            turnOffSound();
            if(reminderState){
                turnOnReminder();
            }
        }
    }

    public void turnOffReminder(){
        if(reminderState){
            reminderState = false;
            if(!alarmState){
                turnOffFlashBlink();
                turnOffVibration();
                turnOffSound();
            }
        }
    }

    private void initFlash(){
        flashAvailable = context.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraID = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void turnOnFlashBlink(final long blinkDelayMillis){
        if (flashAvailable
                && !flashBlinkState
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flashHandler = new Handler();
            toggleFlash = new Runnable() {
                public void run() {
                    toggleFlash();
                    flashHandler.postDelayed(this, blinkDelayMillis);
                }
            };
            flashHandler.postDelayed(toggleFlash, blinkDelayMillis);
            flashBlinkState = true;
        }
    }

    private void toggleFlash(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                if (flashState) {
                    cameraManager.setTorchMode(cameraID, false);
                    flashState = false;
                } else {
                    cameraManager.setTorchMode(cameraID, true);
                    flashState = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void turnOffFlashBlink(){
        if(flashAvailable && flashBlinkState){
            flashHandler.removeCallbacks(toggleFlash);
            flashBlinkState = false;
        }
    }

    private void initVibrator(){
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibratorAvailable = vibrator.hasVibrator();
    }

    private void turnOnVibration(long vibrateMillis, long pauseMillis){
        if(vibratorAvailable && !vibratorState){
            vibrator.vibrate(new long[]{0, vibrateMillis, pauseMillis}, 0);
            vibratorState = true;
        }
    }

    private void turnOffVibration(){
        if(vibratorAvailable && vibratorState){
            vibrator.cancel();
            vibratorState = false;
        }
    }

    private void initSound(){
        toneGen = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
    }

    private void turnOnSound(final int toneMillis, final int pauseMillis){
        if(!soundState){
            soundHandler = new Handler();
            toogleSound = new Runnable() {
                public void run() {
                    toneGen.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, toneMillis);
                    soundHandler.postDelayed(this, pauseMillis);
                }
            };
            soundHandler.postDelayed(toogleSound, 0);
            soundState = true;
        }
    }

    private void turnOffSound(){
        if(soundState){
            flashHandler.removeCallbacks(toggleFlash);
            soundState = false;
        }
    }
}

package de.thral.draegermanObservation.deviceNotification;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.util.Log;

public class Flash implements DeviceNotification {

    private Context context;

    private boolean flashAvailable;
    private boolean flashState;
    private boolean flashBlinkState;

    private CameraManager cameraManager;
    private String cameraID;

    private Thread flashThread;
    private Runnable toggleFlash;

    public Flash(Context context){
        this.context = context;
        init();
    }

    @Override
    public void init() {
        flashAvailable = context.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        flashAvailable = flashAvailable && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
        if(flashAvailable){
            cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            try {
                cameraID = cameraManager.getCameraIdList()[0];
            } catch (CameraAccessException e) {
                Log.e(LOG_TAG, e.getMessage());
                flashAvailable = false;
            }
        }
    }

    @Override
    public boolean isAvailable() {
        return flashAvailable;
    }

    @Override
    public void turnOn(final long activeMillis, final long pauseMillis) {
        if (flashAvailable && !flashBlinkState) {
            toggleFlash = new Runnable() {
                public void run() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        try {
                            while((flashBlinkState || flashState)
                                    && !Thread.currentThread().isInterrupted()){
                                if (flashState) {
                                    cameraManager.setTorchMode(cameraID, false);
                                    flashState = false;
                                    Thread.sleep(pauseMillis);
                                } else {
                                    cameraManager.setTorchMode(cameraID, true);
                                    flashState = true;
                                    Thread.sleep(activeMillis);
                                }
                            }
                        } catch (CameraAccessException e) {
                            Log.e(LOG_TAG, e.getMessage());
                        } catch (InterruptedException e){
                            Log.e(LOG_TAG, e.getMessage());
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            };
            flashThread = new Thread(toggleFlash);
            flashBlinkState = true;
            flashThread.start();
        }
    }

    @Override
    public void turnOff() {
        flashBlinkState = false;
    }
}

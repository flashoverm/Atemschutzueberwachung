package de.thral.draegermanObservation.hardware;

import android.content.Context;
import android.content.SharedPreferences;

public class HardwareInterface {

    public enum Settings {flash, vibration, sound};

    private static final String PREF_ENABLE_FLASH = "de.thral.atemschutzueberwachung.flash";
    private static final String PREF_ENABLE_VIBRATION = "de.thral.atemschutzueberwachung.vibration";
    private static final String PREF_ENABLE_SOUND = "de.thral.atemschutzueberwachung.sound";

    private Context context;

    private int alarmState;
    private int reminderState;

    private boolean flashEnabled;
    private Flash flash;
    private boolean vibrationEnabled;
    private Vibration vibration;
    private boolean soundEnabled;
    private Sound sound;

    public HardwareInterface(Context context){
        this.context = context;
        initSettings();
        flash = new Flash(context);
        vibration = new Vibration(context);
        sound = new Sound();
    }

    /** Loads settings from the shared preferences
     *
     */
    private void initSettings(){
        flashEnabled = context.getSharedPreferences(
                PREF_ENABLE_FLASH, Context.MODE_PRIVATE).getBoolean(PREF_ENABLE_FLASH, false);
        vibrationEnabled = context.getSharedPreferences(
                PREF_ENABLE_VIBRATION, Context.MODE_PRIVATE).getBoolean(PREF_ENABLE_VIBRATION, false);
        soundEnabled = context.getSharedPreferences(
                PREF_ENABLE_SOUND, Context.MODE_PRIVATE).getBoolean(PREF_ENABLE_SOUND, false);
    }

    /** Gets state (on/off) of the components)
     *
     * @return array with flash, vibration, sound in this order
     */
    public boolean[] getSettings(){
        return new boolean[]{flashEnabled, vibrationEnabled, soundEnabled};
    }

    public void setSetting(Settings setting, boolean state){
        SharedPreferences.Editor editor = null;
        if(setting.equals(Settings.flash)){
            this.flashEnabled = state;
            editor = context.getSharedPreferences(PREF_ENABLE_FLASH, Context.MODE_PRIVATE).edit();
            editor.putBoolean(PREF_ENABLE_FLASH, flashEnabled);
        } else if(setting.equals(Settings.vibration)){
            this.vibrationEnabled = state;
            editor = context.getSharedPreferences(PREF_ENABLE_VIBRATION, Context.MODE_PRIVATE).edit();
            editor.putBoolean(PREF_ENABLE_VIBRATION, vibrationEnabled);
        } else if(setting.equals(Settings.sound)){
            this.soundEnabled = state;
            editor = context.getSharedPreferences(PREF_ENABLE_SOUND, Context.MODE_PRIVATE).edit();
            editor.putBoolean(PREF_ENABLE_SOUND, soundEnabled);
        }
        editor.commit();
    }

    /** Returns availability of the components
     *
     * @return array of flash, vibration, sound in this order
     */
    public boolean[] getAvailability(){
        return new boolean[]{
                flash.isAvailable(),
                vibration.isAvailable(),
                sound.isAvailable()
        };
    }

    public void turnOnReminder(){
        if(reminderState == 0){
            if(alarmState == 0){
                if(flashEnabled) {
                    flash.turnOn(100, 1000);
                }
                if(vibrationEnabled){
                    vibration.turnOn(500, 3000);
                }
                if(soundEnabled){
                    sound.turnOn(600, 6000);
                }
            }
        }
        reminderState ++;
    }

    public void turnOnAlarm(){
        if(alarmState == 0){
            if(reminderState >0){
                turnOffReminder();
            }
            if(flashEnabled) {
                flash.turnOn(100, 100);
            }
            if(vibrationEnabled){
                vibration.turnOn(500, 500);
            }
            if(soundEnabled) {
                sound.turnOn(600, 2000);
            }
        }
        alarmState ++;
    }

    public void turnOffAlarm(){
        if(alarmState >0){
            alarmState--;
            if(alarmState == 0){
                turnOff();
                if(reminderState >0){
                    turnOnReminder();
                }
            }
        }
    }

    public void turnOffReminder(){
        if(reminderState > 0){
            reminderState--;
            if(reminderState == 0 && alarmState == 0){
                turnOff();
            }
        }
    }

    private void turnOff(){
        if(flashEnabled){
            flash.turnOff();
        }
        if(vibrationEnabled){
            vibration.turnOff();
        }
        if(soundEnabled){
            sound.turnOff();
        }
    }
}

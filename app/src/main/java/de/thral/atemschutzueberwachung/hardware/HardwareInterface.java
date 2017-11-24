package de.thral.atemschutzueberwachung.hardware;

import android.content.Context;

public class HardwareInterface {

    private int alarmState;
    private int reminderState;

    private Flash flash;
    private Vibrate vibrate;
    private Sound sound;

    public HardwareInterface(Context context){
        flash = new Flash(context);
        vibrate = new Vibrate(context);
        sound = new Sound();
    }

    public void turnOnReminder(){
        if(reminderState == 0){
            if(alarmState == 0){
                flash.turnOn(100, 1000);
                vibrate.turnOn(500, 3000);
                sound.turnOn(600, 6000);
            }
        }
        reminderState ++;
    }

    public void turnOnAlarm(){
        if(alarmState == 0){
            if(reminderState >0){
                turnOffReminder();
            }
            flash.turnOn(100, 100);
            vibrate.turnOn(500, 500);
            sound.turnOn(600, 2000);
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
        flash.turnOff();
        vibrate.turnOff();
        sound.turnOff();
    }
}

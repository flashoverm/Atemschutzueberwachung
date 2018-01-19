package de.thral.draegermanObservation.business;

import android.os.CountDownTimer;

public class OperationTimer {

    private transient CountDownTimer timer;

    private long timerValue;
    private final int secOneThird;
    private final int secTwoThird;

    private long timestampTimerStart;
    private long timerValueStart;

    private boolean reminderActive;
    private boolean alarmUnconfirmed;
    private transient TimerChangeListener timerListener;

    public OperationTimer(OperatingTime operatingTime){
        this.timerValue = operatingTime.getTime()*60000;

        this.secOneThird = operatingTime.getTime()*20;
        this.secTwoThird = operatingTime.getTime()*40;
        this.reminderActive = false;

        this.timestampTimerStart = -1;
        this.timerValueStart = -1;
    }

    public long getValue() {
        return timerValue;
    }

    public String getValueAsClock() {
        String clock = "";

        int seconds = (int)timerValue/1000;
        int minutes = seconds/60;
        seconds = seconds - minutes*60;

        if(minutes < 10){
            clock = "0" + minutes;
        } else {
            clock = minutes+"";
        }
        clock += ":";
        if(seconds <10){
            clock += "0"+seconds;
        } else {
            clock += seconds+"";
        }
        return clock;
    }

    public boolean isReminderActive(){
        return reminderActive;
    }

    public boolean isAlarmUnconfirmed(){
        return alarmUnconfirmed;
    }

    public void setTimerListener(TimerChangeListener timerListener){
        this.timerListener = timerListener;
    }

    public void deactivateReminder(){
        this.reminderActive = false;
    }

    public void confirmAlarm(){
        this.alarmUnconfirmed = false;
    }

    public void start(){
        if(timerValue != 0){
            timer = new CountDownTimer(timerValue, 1000) {
                public void onTick(long millisUntilFinished) {
                    OperationTimer.this.timerValue = millisUntilFinished;
                    if(timerListener != null){
                        timerListener.onTimerUpdate(getValueAsClock());
                    }

                    if((timerValue/1000) == secTwoThird || (timerValue/1000) == secOneThird){
                        reminderActive = true;
                        if(timerListener != null){
                            timerListener.onTimerReachedMark(false);
                        }
                    }
                }
                public void onFinish() {
                    OperationTimer.this.timerValue = 0;
                    alarmUnconfirmed = true;
                    if(timerListener != null){
                        timerListener.onTimerUpdate("00:00");
                        timerListener.onTimerReachedMark(true);
                    }
                }
            };
            timestampTimerStart = System.currentTimeMillis();
            timerValueStart = timerValue;
            timer.start();
        }
    }

    public void cancel(){
        timestampTimerStart = -1;
        timerValue = -1;
        if(timer != null){
            timer.cancel();
        }
    }

    /** Resumes timer after error if it ran before the error
     *
     */
    public void resumeAfterError(){
        if(timestampTimerStart != -1 && timerValueStart != -1) {
            timerValue = Math.max(0,
                    timerValueStart-(System.currentTimeMillis()-timestampTimerStart));
            start();
        }
    }
}

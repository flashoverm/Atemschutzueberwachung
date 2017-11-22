package de.thral.atemschutzueberwachung.domain;

import android.os.CountDownTimer;

public class OperationTimer {

    private transient CountDownTimer timer;
    private long timerValue;
    private long timestampTimerStart;
    private final int secOneThird;
    private final int secTwoThird;
    private boolean reminder;
    private transient TimerChangeListener timerListener;

    public OperationTimer(OperatingTime operatingTime){
        this.timerValue = operatingTime.getTime()*60000;

        this.secOneThird = operatingTime.getTime()*20;
        this.secTwoThird = operatingTime.getTime()*40;
        this.reminder = false;
        this.timestampTimerStart = -1;
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
        return reminder;
    }

    public void setTimerListener(TimerChangeListener timerListener){
        this.timerListener = timerListener;
    }

    public void deactiveReminder(){
        this.reminder = false;
    }

    public void start(){
        timer = new CountDownTimer(timerValue, 1000) {
            public void onTick(long millisUntilFinished) {
                OperationTimer.this.timerValue = millisUntilFinished;
                timerListener.onTimerUpdate(getValueAsClock());

                if((timerValue/1000) == secTwoThird || (timerValue/1000) == secOneThird){
                    reminder = true;
                    timerListener.onTimerReachedMark(false);
                }
            }
            public void onFinish() {
                OperationTimer.this.timerValue = 0;
                timerListener.onTimerUpdate("00:00");
                timerListener.onTimerReachedMark(true);
            }
        };
        timestampTimerStart = System.currentTimeMillis();
        timer.start();
    }

    public void cancel(){
        timestampTimerStart = -1;
        if(timer != null){
            timer.cancel();
        }
    }

    public void resumeAfterError(){
        if(timestampTimerStart != -1) {
            timerValue = timerValue - (System.currentTimeMillis() - timestampTimerStart);
            start();
        }
    }
}

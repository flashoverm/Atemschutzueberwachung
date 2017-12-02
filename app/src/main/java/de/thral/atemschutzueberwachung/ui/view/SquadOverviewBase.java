package de.thral.atemschutzueberwachung.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import de.thral.atemschutzueberwachung.R;

public abstract class SquadOverviewBase extends SquadViewBase {

    public SquadOverviewBase(Context context) {
        super(context);
    }

    public SquadOverviewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void activateViewReminder(){
        final AnimationDrawable drawable = new AnimationDrawable();
        final Handler handler = new Handler();
        drawable.addFrame(
                new ColorDrawable(ContextCompat.getColor(getContext(), R.color.ral3024)), 500);
        drawable.addFrame(new ColorDrawable(Color.WHITE), 500);
        drawable.setOneShot(false);
        infoView.setBackground(drawable);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                drawable.start();
            }
        }, 100);
    }

    @Override
    protected void deactivateViewReminder(){
        infoView.setBackground(new ColorDrawable(Color.WHITE));
    }

    @Override
    protected void setReminderAlarm() {
        if (squad.isReminderActive()) {
            activateViewReminder();
        } else {
            deactivateViewReminder();
        }
        if (squad.isTimerExpired()) {
            if(squad.isAlarmUnconfirmed()){
                activateViewReminder();
            } else {
                deactivateViewReminder();
            }
            activateViewAlarm();
        } else {
            deactivateViewAlarm();
        }
    }

    @Override
    protected void timerReachedMark(boolean expired) {
        if (expired) {
            if(squad.isAlarmUnconfirmed()){
                activateViewReminder();
            } else {
                deactivateViewReminder();
            }
            activateViewAlarm();
            hardwareInterface.turnOnAlarm();
        } else {
            activateViewReminder();
            hardwareInterface.turnOnReminder();
        }
    }
}

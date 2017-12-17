package de.thral.draegermanObservation.ui.view;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import de.thral.draegermanObservation.R;

public abstract class SquadOverviewBase extends SquadViewBase {

    public SquadOverviewBase(Context context) {
        super(context);
    }

    public SquadOverviewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void activateViewReminder(){
        System.out.println("ANIMATION START");
        infoView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red));
        final ValueAnimator valueAnimator = ObjectAnimator.ofInt(infoView, "backgroundColor",
                ContextCompat.getColor(getContext(), R.color.red),
                ContextCompat.getColor(getContext(), R.color.white));
        valueAnimator.setDuration(800);
        valueAnimator.setEvaluator(new ArgbEvaluator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();
    }

    @Override
    protected void deactivateViewReminder(){
        infoView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
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

package de.thral.draegermanObservation.ui.view;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;

import de.thral.draegermanObservation.R;
import de.thral.draegermanObservation.ui.activity.MonitoringBaseActivity;

public abstract class SquadOverviewBase extends SquadViewBase {

    private ObjectAnimator backgroundAnimation;

    public SquadOverviewBase(Context context) {
        super(context);
    }

    public SquadOverviewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void activateViewReminder(){
        if(backgroundAnimation == null){
            backgroundAnimation = ObjectAnimator.ofInt(infoView, "backgroundColor",
                    getResources().getColor(R.color.red),
                    getResources().getColor(R.color.white));
            backgroundAnimation.setDuration(800);
            backgroundAnimation.setEvaluator(new ArgbEvaluator());
            backgroundAnimation.setRepeatCount(ValueAnimator.INFINITE);
        }
        backgroundAnimation.start();
    }

    @Override
    protected void deactivateViewReminder(){
        if(backgroundAnimation != null){
            backgroundAnimation.end();
        }
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
        if(getContext() instanceof MonitoringBaseActivity){
            ((MonitoringBaseActivity)getContext()).updateOperation();
        }
        if (expired) {
            if(squad.isAlarmUnconfirmed()){
                activateViewReminder();
            } else {
                deactivateViewReminder();
            }
            activateViewAlarm();
            notificationManager.turnOnAlarm();
        } else {
            activateViewReminder();
            notificationManager.turnOnReminder();
        }
    }
}

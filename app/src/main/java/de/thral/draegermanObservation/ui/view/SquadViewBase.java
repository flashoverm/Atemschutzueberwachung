package de.thral.draegermanObservation.ui.view;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import de.thral.draegermanObservation.DraegermanObservationApplication;
import de.thral.draegermanObservation.R;
import de.thral.draegermanObservation.business.Squad;
import de.thral.draegermanObservation.deviceNotification.DeviceNotificationInterface;
import de.thral.draegermanObservation.ui.activity.MonitoringBaseActivity;

public abstract class SquadViewBase extends LinearLayout {

    protected View infoView;
    protected Squad squad;

    protected ValueAnimator colorAnimator;

    protected DeviceNotificationInterface deviceNotificationInterface;

    protected TextView timer, squadname, state, leaderName, memberName;

    public SquadViewBase(Context context) {
        super(context);
        init();
    }

    public SquadViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        this.deviceNotificationInterface = DraegermanObservationApplication
                .getDeviceNotificationInterface(getContext());
        initView();
    }

    protected abstract void initView();

    public abstract void setSquad(Squad squad);

    protected void setReminderAlarm() {
        if (squad.isReminderActive()) {
            activateViewReminder();
        } else {
            deactivateViewReminder();
        }
        if (squad.isTimerExpired()) {
            if(squad.isReminderActive()){
                deactivateViewReminder();
            }
            activateViewAlarm();
        } else {
            deactivateViewAlarm();
        }
    }

    protected void timerReachedMark(boolean expired) {
        if(getContext() instanceof MonitoringBaseActivity){
            ((MonitoringBaseActivity)getContext()).updateOperation();
        }
        if (expired) {
            deactivateViewReminder();
            activateViewAlarm();
            deviceNotificationInterface.turnOnAlarm();
        } else {
            activateViewReminder();
            deviceNotificationInterface.turnOnReminder();
        }
    }

    protected void activateViewAlarm(){
        colorAnimator = ObjectAnimator.ofInt(timer, "textColor",
                ContextCompat.getColor(getContext(), R.color.red),
                ContextCompat.getColor(getContext(), R.color.black));
        colorAnimator.setDuration(800);
        colorAnimator.setEvaluator(new ArgbEvaluator());
        colorAnimator.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimator.start();
    }

    protected void deactivateViewAlarm(){
        if(colorAnimator != null){
            colorAnimator.end();
        }
    }

    protected abstract void activateViewReminder();
    protected abstract void deactivateViewReminder();

}

package de.thral.atemschutzueberwachung.ui.view;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.thral.atemschutzueberwachung.DraegermanObservationApplication;
import de.thral.atemschutzueberwachung.business.Squad;
import de.thral.atemschutzueberwachung.hardware.HardwareInterface;

public abstract class SquadViewBase extends LinearLayout {

    protected View infoView;
    protected Squad squad;

    protected ValueAnimator colorAnimator;

    protected HardwareInterface hardwareInterface;

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
        this.hardwareInterface = DraegermanObservationApplication.getHardwareInterface(getContext());
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
            deactivateViewReminder();
            activateViewAlarm();
        } else {
            deactivateViewAlarm();
        }
    }

    protected void timerReachedMark(boolean expired) {
        if (expired) {
            deactivateViewReminder();
            activateViewAlarm();
            hardwareInterface.turnOnAlarm();
        } else {
            activateViewReminder();
            hardwareInterface.turnOnReminder();
        }
    }

    protected void activateViewAlarm(){
        colorAnimator = ObjectAnimator.ofInt(timer, "textColor", Color.RED, Color.GRAY);
        colorAnimator.setDuration(800);
        colorAnimator.setEvaluator(new ArgbEvaluator());
        colorAnimator.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimator.start();
    }

    protected void deactivateViewAlarm(){
        if(colorAnimator != null){
            colorAnimator.cancel();
        }
    }

    protected abstract void activateViewReminder();
    protected abstract void deactivateViewReminder();

}

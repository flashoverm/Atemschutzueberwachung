package de.thral.atemschutzueberwachung.activity.Views;

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
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.domain.Squad;

public abstract class OverviewBase extends GridLayout {

    protected Context context;
    protected View overview;

    protected TextView timer, squadname, state, leaderName, memberName;

    public OverviewBase(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public OverviewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    protected abstract void init();
    public abstract void setSquad(Squad squad);

    protected void timerReachedMark(boolean expired){
        if(expired){
            deactivateReminder();
            activateAlarm();
        } else {
            activateReminder();
        }
    }

    protected void activateAlarm(){
        ValueAnimator colorAnim = ObjectAnimator.ofInt(timer, "textColor", Color.RED, Color.GRAY);
        colorAnim.setDuration(800);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(ValueAnimator.INFINITE);
        colorAnim.start();
    }

    protected void activateReminder(){
        final AnimationDrawable drawable = new AnimationDrawable();
        final Handler handler = new Handler();
        drawable.addFrame(new ColorDrawable(ContextCompat.getColor(context, R.color.ral3024)), 500);
        drawable.addFrame(new ColorDrawable(Color.WHITE), 500);
        drawable.setOneShot(false);
        overview.setBackground(drawable);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                drawable.start();
            }
        }, 100);
    }

    protected void deactivateReminder(){
        overview.setBackground(new ColorDrawable(Color.WHITE));
    }
}

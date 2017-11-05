package de.thral.atemschutzueberwachung.activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.domain.Event;
import de.thral.atemschutzueberwachung.domain.Squad;

public abstract class MonitoringActivity extends AppCompatActivity {

    protected void initInfoView(ViewGroup layout, Squad squad){
        TextView timer = (TextView)layout.findViewById(R.id.timer);
        TextView squadname = (TextView)layout.findViewById(R.id.squadname);
        TextView state = (TextView)layout.findViewById(R.id.state);
        TextView leader = (TextView)layout.findViewById(R.id.leaderName);
        TextView member = (TextView)layout.findViewById(R.id.memberName);

        timer.setText(squad.getTimerValueAsClock());
        squadname.setText(squad.getName());
        state.setText(squad.getState().getStateDescription(getApplicationContext()));
        leader.setText(squad.getLeader().getDisplayName());
        member.setText(squad.getMember().getDisplayName());

        if(squad.getTimerValue() == 0){
            //activateAlarm(layout);
        }
    }

    protected void updateReturnPressure(ViewGroup layout, Squad squad){
        TextView leaderReturnPressure = (TextView)layout.findViewById(R.id.leaderReturnPressure);
        TextView memberReturnPressure = (TextView)layout.findViewById(R.id.memberReturnPressure);

        if(squad.getLeaderReturnPressure() != -1
                && squad.getMemberReturnPressure() != -1){
            leaderReturnPressure.setText(squad.getLeaderReturnPressure()+"");
            memberReturnPressure.setText(squad.getMemberReturnPressure()+"");
            leaderReturnPressure.setVisibility(View.VISIBLE);
            memberReturnPressure.setVisibility(View.VISIBLE);
        } else {
            leaderReturnPressure.setVisibility(View.INVISIBLE);
            memberReturnPressure.setVisibility(View.INVISIBLE);
        }
    }
/*
    protected void timerReachedMark(ViewGroup layout, Squad squad, boolean expired){
        if(expired){
            deactivateReminder(layout);
            activateAlarm(layout);
        } else {
            activateReminder(layout);
        }
    }

    protected void activateAlarm(ViewGroup layout){
        TextView timer = (TextView)layout.findViewById(R.id.timer);
        ValueAnimator colorAnim = ObjectAnimator.ofInt(timer, "textColor", Color.RED, Color.GRAY);
        colorAnim.setDuration(800);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(ValueAnimator.INFINITE);
        colorAnim.start();
    }

    protected void activateReminder(ViewGroup layout){
        final AnimationDrawable drawable = new AnimationDrawable();
        final Handler handler = new Handler();

        drawable.addFrame(new ColorDrawable(
                ContextCompat.getColor(getApplicationContext(), R.color.ral3024)), 500);
        drawable.addFrame(new ColorDrawable(Color.WHITE), 500);
        drawable.setOneShot(false);

        layout.setBackground(drawable);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                drawable.start();
            }
        }, 100);
    }

    protected void deactivateReminder(ViewGroup layout){
        layout.setBackground(new ColorDrawable(Color.WHITE));
    }
*/
}

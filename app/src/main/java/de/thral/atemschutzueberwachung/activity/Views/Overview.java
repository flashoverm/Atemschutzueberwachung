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
import de.thral.atemschutzueberwachung.domain.Event;
import de.thral.atemschutzueberwachung.domain.Squad;
import de.thral.atemschutzueberwachung.domain.TimerChangeListener;

public class Overview extends GridLayout {

    private Context context;
    private View overview;

    private TextView timer, squadname, state, leaderName, memberName, lastPressureTime,
            leaderPressure, memberPressure, leaderReturnPressure, memberReturnPressure;

    public Overview(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public Overview(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        overview = inflate(context, R.layout.monitor_overview, this);
        timer = (TextView)overview.findViewById(R.id.timer);
        squadname = (TextView)overview.findViewById(R.id.squadname);
        state = (TextView)overview.findViewById(R.id.state);
        leaderName = (TextView)overview.findViewById(R.id.leaderName);
        memberName = (TextView)overview.findViewById(R.id.memberName);
        lastPressureTime = (TextView)overview.findViewById(R.id.lastPressureTime);
        leaderPressure = (TextView)overview.findViewById(R.id.leaderPressure);
        memberPressure = (TextView)overview.findViewById(R.id.memberPressure);
        leaderReturnPressure = (TextView)overview.findViewById(R.id.leaderReturnPressure);
        memberReturnPressure = (TextView)overview.findViewById(R.id.memberReturnPressure);
    }

    public void setSquad(Squad squad){
        timer.setText(squad.getTimerValueAsClock());
        squadname.setText(squad.getName());
        state.setText(squad.getState().getStateDescription(context));
        leaderName.setText(squad.getLeader().getDisplayName());
        memberName.setText(squad.getMember().getDisplayName());

        Event[] events = squad.getLastPressureValues();
        int time = (int)events[0].getRemainingOperationTime()/1000/60;
        lastPressureTime.setText(time+" Min.");
        leaderPressure.setText(events[0].getPressureLeader()+"");
        memberPressure.setText(events[0].getPressureMember()+"");

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

        if(squad.isReminderActive()){
            activateReminder();
        } else {
            deactivateReminder();
        }

        squad.setTimerListener(new TimerChangeListener() {
            @Override
            public void onTimerUpdate(Squad squad) {
                timer.setText(squad.getTimerValueAsClock());
            }

            @Override
            public void onTimerReachedMark(Squad squad, boolean expired) {
                timerReachedMark(expired);
            }
        });
    }



    //TODO FOR PARENT CLASS

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

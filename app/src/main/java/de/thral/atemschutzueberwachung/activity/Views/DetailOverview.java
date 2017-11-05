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

public class DetailOverview extends GridLayout {

    private Context context;
    private View detailOverview;

    private TextView timer, squadname, state, leaderName, memberName,
            leaderPressureInfo, memberPressureInfo;

    public DetailOverview(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public DetailOverview(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init(){
        detailOverview = inflate(context, R.layout.monitor_detail_overview, this);
        timer = (TextView) detailOverview.findViewById(R.id.timer);
        squadname = (TextView) detailOverview.findViewById(R.id.squadname);
        state = (TextView) detailOverview.findViewById(R.id.state);
        leaderName = (TextView) detailOverview.findViewById(R.id.leaderName);
        memberName = (TextView) detailOverview.findViewById(R.id.memberName);
        leaderPressureInfo = (TextView) detailOverview.findViewById(R.id.leaderPressureInfo);
        memberPressureInfo = (TextView) detailOverview.findViewById(R.id.memberPressureInfo);
    }

    public void setSquad(Squad squad){
        timer.setText(squad.getTimerValueAsClock());
        squadname.setText(squad.getName());
        state.setText(squad.getState().getStateDescription(context));
        leaderName.setText(squad.getLeader().getDisplayName());
        memberName.setText(squad.getMember().getDisplayName());

        Event[] events = squad.getLastPressureValues();
        int time = (int)events[0].getRemainingOperationTime()/1000/60;

        String leaderInfo = events[0].getPressureLeader()+"("+time+" Min)";
        String memberInfo = events[0].getPressureMember()+"("+time+" Min)";
        if(squad.getLeaderReturnPressure() != -1
                && squad.getMemberReturnPressure() != -1){
            leaderInfo += "/" + squad.getLeaderReturnPressure();
            memberInfo += "/" + squad.getMemberReturnPressure();
        }
        leaderPressureInfo.setText(leaderInfo);
        memberPressureInfo.setText(memberInfo);

        if(squad.isReminderActive()){
            activateReminder();
        } else {
            deactivateReminder();
        }
        if(squad.getTimerValue() == 0){
            activateAlarm();
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
        detailOverview.setBackground(drawable);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                drawable.start();
            }
        }, 100);
    }

    protected void deactivateReminder(){
        detailOverview.setBackground(new ColorDrawable(Color.WHITE));
    }
}

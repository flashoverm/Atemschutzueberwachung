package de.thral.atemschutzueberwachung.activity.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.domain.Event;
import de.thral.atemschutzueberwachung.domain.Squad;
import de.thral.atemschutzueberwachung.domain.TimerChangeListener;

public class Overview extends OverviewBase {

    private TextView lastPressureTime, leaderPressure, memberPressure,
            leaderReturnPressure, memberReturnPressure;

    public Overview(Context context) {
        super(context);
    }

    public Overview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void init() {
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
        lastPressureTime.setText(time+" "+context.getString(R.string.minutesShort));
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
        if(squad.getTimerValue() == 0){
            deactivateReminder();
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
}

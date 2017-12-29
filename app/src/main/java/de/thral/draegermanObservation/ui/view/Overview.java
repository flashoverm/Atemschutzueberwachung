package de.thral.draegermanObservation.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import de.thral.draegermanObservation.R;
import de.thral.draegermanObservation.business.Event;
import de.thral.draegermanObservation.business.Squad;
import de.thral.draegermanObservation.business.TimerChangeListener;

public class Overview extends SquadOverviewBase {

    private TextView lastPressureTime, leaderPressure, memberPressure,
            leaderReturnPressure, memberReturnPressure;

    public Overview(Context context) {
        super(context);
    }

    public Overview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView() {
        infoView = inflate(getContext(), R.layout.monitor_overview, this);
        infoView.setBackgroundColor(getResources().getColor(R.color.white));
        timer = infoView.findViewById(R.id.timer);
        squadname = infoView.findViewById(R.id.squadname);
        state = infoView.findViewById(R.id.state);
        leaderName = infoView.findViewById(R.id.leaderName);
        memberName = infoView.findViewById(R.id.memberName);
        lastPressureTime = infoView.findViewById(R.id.lastPressureTime);
        leaderPressure = infoView.findViewById(R.id.leaderPressure);
        memberPressure = infoView.findViewById(R.id.memberPressure);
        leaderReturnPressure = infoView.findViewById(R.id.leaderReturnPressure);
        memberReturnPressure = infoView.findViewById(R.id.memberReturnPressure);
    }

    @Override
    public void setSquad(Squad squad){
        this.squad = squad;
        timer.setText(squad.getTimerValueAsClock());
        squadname.setText(squad.getName());
        state.setText(squad.getState().getStateDescription(getContext()));
        leaderName.setText(squad.getLeader().getDisplayName());
        memberName.setText(squad.getMember().getDisplayName());

        Event[] events = squad.getLastPressureValues(1);
        int time = (int)events[0].getRemainingOperationTime()/1000/60;
        lastPressureTime.setText(time+" "+getContext().getString(R.string.minutesShort));
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

        squad.setTimerListener(new TimerChangeListener() {
            @Override
            public void onTimerUpdate(String timer) {
                Overview.this.timer.setText(timer);
            }

            @Override
            public void onTimerReachedMark(boolean expired) {
                timerReachedMark(expired);
            }
        });

        setReminderAlarm();
    }
}

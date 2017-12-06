package de.thral.draegermanObservation.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import de.thral.draegermanObservation.R;
import de.thral.draegermanObservation.business.Event;
import de.thral.draegermanObservation.business.Squad;
import de.thral.draegermanObservation.business.TimerChangeListener;

public class DetailOverview extends SquadOverviewBase {

    private TextView leaderPressureInfo, memberPressureInfo;

    public DetailOverview(Context context) {
        super(context);
    }

    public DetailOverview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView(){
        infoView = inflate(getContext(), R.layout.monitor_detail_overview, this);
        timer = infoView.findViewById(R.id.timer);
        squadname = infoView.findViewById(R.id.squadname);
        state = infoView.findViewById(R.id.state);
        leaderName = infoView.findViewById(R.id.leaderName);
        memberName = infoView.findViewById(R.id.memberName);
        leaderPressureInfo = infoView.findViewById(R.id.leaderPressureInfo);
        memberPressureInfo = infoView.findViewById(R.id.memberPressureInfo);
    }

    @Override
    public void setSquad(Squad squad){
        this.squad = squad;
        timer.setText(squad.getTimerValueAsClock());
        squadname.setText(squad.getName());
        state.setText(squad.getState().getStateDescription(getContext()));
        leaderName.setText(squad.getLeader().getDisplayName());
        memberName.setText(squad.getMember().getDisplayName());

        Event[] events = squad.getLastPressureValues(3);
        int time = (int)events[0].getRemainingOperationTime()/1000/60;

        String leaderInfo = events[0].getPressureLeader()
                +"("+time+" " + getContext().getString(R.string.minutesShort) +")/";
        String memberInfo = events[0].getPressureMember()
                +"("+time+" " + getContext().getString(R.string.minutesShort) +")/";
        if(squad.getLeaderReturnPressure() != -1
                && squad.getMemberReturnPressure() != -1){
            leaderInfo += squad.getLeaderReturnPressure();
            memberInfo += squad.getMemberReturnPressure();
        } else {
            leaderInfo += " -";
            memberInfo += " -";
        }
        leaderPressureInfo.setText(leaderInfo);
        memberPressureInfo.setText(memberInfo);

        squad.setTimerListener(new TimerChangeListener() {
            @Override
            public void onTimerUpdate(String timer) {
                DetailOverview.this.timer.setText(timer);
            }

            @Override
            public void onTimerReachedMark(boolean expired) {
                timerReachedMark(expired);
            }
        });

        setReminderAlarm();
    }
}

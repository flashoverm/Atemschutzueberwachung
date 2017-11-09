package de.thral.atemschutzueberwachung.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.domain.Event;
import de.thral.atemschutzueberwachung.domain.Squad;
import de.thral.atemschutzueberwachung.domain.TimerChangeListener;

public class DetailOverview extends OverviewBase {

    private TextView leaderPressureInfo, memberPressureInfo;

    public DetailOverview(Context context) {
        super(context);
    }

    public DetailOverview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void init(){
        overview = inflate(context, R.layout.monitor_detail_overview, this);
        timer = (TextView) overview.findViewById(R.id.timer);
        squadname = (TextView) overview.findViewById(R.id.squadname);
        state = (TextView) overview.findViewById(R.id.state);
        leaderName = (TextView) overview.findViewById(R.id.leaderName);
        memberName = (TextView) overview.findViewById(R.id.memberName);
        leaderPressureInfo = (TextView) overview.findViewById(R.id.leaderPressureInfo);
        memberPressureInfo = (TextView) overview.findViewById(R.id.memberPressureInfo);
    }

    public void setSquad(Squad squad){
        timer.setText(squad.getTimerValueAsClock());
        squadname.setText(squad.getName());
        state.setText(squad.getState().getStateDescription(context));
        leaderName.setText(squad.getLeader().getDisplayName());
        memberName.setText(squad.getMember().getDisplayName());

        Event[] events = squad.getLastPressureValues(3);
        int time = (int)events[0].getRemainingOperationTime()/1000/60;

        String leaderInfo = events[0].getPressureLeader()
                +"("+time+" " + context.getString(R.string.minutesShort) +")/ -";
        String memberInfo = events[0].getPressureMember()
                +"("+time+" " + context.getString(R.string.minutesShort) +")/ -";
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
        if(squad.isTimerExpired()){
            deactivateReminder();
            activateAlarm();
        }

        squad.setTimerListener(new TimerChangeListener() {
            @Override
            public void onTimerUpdate(Squad squad) {
                timer.setText(squad.getTimerValueAsClock());
            }

            @Override
            public void onTimerReachedMark(boolean expired) {
                timerReachedMark(expired);
            }
        });
    }
}

package de.thral.draegermanObservation.ui.view;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.thral.draegermanObservation.R;
import de.thral.draegermanObservation.business.Event;
import de.thral.draegermanObservation.business.EventType;
import de.thral.draegermanObservation.business.Squad;
import de.thral.draegermanObservation.business.SquadChangeListener;
import de.thral.draegermanObservation.business.TimerChangeListener;
import de.thral.draegermanObservation.ui.dialog.EnterPressureDialog;
import de.thral.draegermanObservation.ui.dialog.PressureWarningDialog;

public class DetailView extends SquadViewBase{

    public interface StartButtonListener {
        void onStartButtonClick();
    }

    private View detailView;
    private StartButtonListener startButtonListener;

    private TextView order, leaderReturnPressure, memberReturnPressure,
            pressureTime1, leaderPressure1, memberPressure1,
            pressureTime2, leaderPressure2, memberPressure2,
            pressureTime3, leaderPressure3, memberPressure3;
    private Button start, arrive, enterPressure, retreat, end;

    private AlertDialog.Builder startBuilder;
    private ObjectAnimator buttonAnimation;

    public DetailView(Context context) {
        super(context);
    }

    public DetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private FragmentManager getFragmentManager(){
        try{
            final Activity activity = (Activity) getContext();
            return activity.getFragmentManager();

        } catch (ClassCastException e) {
            throw new ClassCastException("Can't get the fragment manager with this");
        }
    }

    public void setStartButtonClickListener(StartButtonListener listener){
        this.startButtonListener = listener;
    }

    @Override
    protected void initView() {
        initInfoView();
        initButtons();
    }

    private void initInfoView() {
        detailView = inflate(getContext(), R.layout.monitor_detail, this);
        infoView = detailView.findViewById(R.id.infoScreen);
        timer = infoView.findViewById(R.id.timer);
        squadname = infoView.findViewById(R.id.squadname);
        state = infoView.findViewById(R.id.state);
        order = infoView.findViewById(R.id.order);
        leaderName = infoView.findViewById(R.id.leaderName);
        memberName = infoView.findViewById(R.id.memberName);
        leaderReturnPressure = infoView.findViewById(R.id.leaderReturnPressure);
        memberReturnPressure = infoView.findViewById(R.id.memberReturnPressure);
        pressureTime1 = infoView.findViewById(R.id.pressureTime1);
        leaderPressure1 = infoView.findViewById(R.id.leaderPressure1);
        memberPressure1 = infoView.findViewById(R.id.memberPressure1);
        pressureTime2 = infoView.findViewById(R.id.pressureTime2);
        leaderPressure2 = infoView.findViewById(R.id.leaderPressure2);
        memberPressure2 = infoView.findViewById(R.id.memberPressure2);
        pressureTime3 = infoView.findViewById(R.id.pressureTime3);
        leaderPressure3 = infoView.findViewById(R.id.leaderPressure3);
        memberPressure3 = infoView.findViewById(R.id.memberPressure3);

        detailView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
    }

    private void initButtons(){
        start = detailView.findViewById(R.id.buttonStart);
        arrive = detailView.findViewById(R.id.buttonArrive);
        enterPressure = detailView.findViewById(R.id.buttonEnterPressure);
        retreat = detailView.findViewById(R.id.buttonRetreat);
        end = detailView.findViewById(R.id.buttonEnd);

        buttonAnimation = ObjectAnimator.ofInt(enterPressure, "backgroundColor",
                ContextCompat.getColor(getContext(), R.color.red),
                ContextCompat.getColor(getContext(), R.color.blue));
        buttonAnimation.setDuration(1500);
        buttonAnimation.setRepeatCount(ValueAnimator.INFINITE);
        buttonAnimation.setEvaluator(new ArgbEvaluator());

        startBuilder = new AlertDialog.Builder(getContext());
        startBuilder.setTitle(R.string.timerInfoTitle)
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog start = startBuilder.create();
                start.show();
            }
        });
        arrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEnterPressure(EventType.Arrive);
            }
        });
        enterPressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEnterPressure(EventType.Timer);
            }
        });
        retreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEnterPressure(EventType.Retreat);
            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEnterPressure(EventType.End);
            }
        });
    }

    private void showEnterPressure(EventType type){
        if(squad.isReminderActive()){
            hardwareInterface.turnOffReminder();
        }
        EnterPressureDialog dialog = EnterPressureDialog.newInstance(
                getContext(), type);
        dialog.show(getFragmentManager(), "PressureDialog");
    }

    @Override
    public void setSquad(Squad squad){
        this.squad = squad;
        timer.setText(squad.getTimerValueAsClock());
        squadname.setText(squad.getName());
        state.setText(squad.getState().getStateDescription(getContext()));
        order.setText(squad.getOrder().toString(getContext()));
        leaderName.setText(squad.getLeader().getDisplayName());
        memberName.setText(squad.getMember().getDisplayName());

        squad.setTimerListener(new TimerChangeListener() {
            @Override
            public void onTimerUpdate(String timer) {
                DetailView.this.timer.setText(timer);
            }

            @Override
            public void onTimerReachedMark(boolean expired) {
                timerReachedMark(expired);
                showAlarmMessage();
            }
        });
        squad.setChangeListener(new SquadChangeListener() {
            @Override
            public void onStateUpdate(Squad squad) {
                state.setText(squad.getState().getStateDescription(getContext()));
                updateButtons();
            }

            @Override
            public void onPressureUpdate(Squad squad) {
                Event[] events = squad.getLastPressureValues(3);
                updateDetailPressure(events);
                deactivateViewReminder();
            }

            @Override
            public void onCalculatedReturnPressure(Squad squad) {
                updateReturnPressure();
            }

            @Override
            public void onPressureInfo(Squad squad, boolean underShot) {
                PressureWarningDialog dialog = PressureWarningDialog.newInstance(underShot);
                dialog.show(getFragmentManager(), "PressureWarningDialog");
            }
        });

        updateDetailPressure(squad.getLastPressureValues(3));
        updateReturnPressure();
        updateButtons();
        setReminderAlarm();
        showAlarmMessage();
    }

    private void updateDetailPressure(Event[] events){
        if(events[2] != null){
            pressureTime1.setText(((int)events[2].getRemainingOperationTime()/1000/60)
                    +" "+getContext().getString(R.string.minutesShort));
            leaderPressure1.setText(events[2].getPressureLeader()+"");
            memberPressure1.setText(events[2].getPressureMember()+"");
            pressureTime2.setText(((int)events[1].getRemainingOperationTime()/1000/60)
                    +" "+getContext().getString(R.string.minutesShort));
            leaderPressure2.setText(events[1].getPressureLeader()+"");
            memberPressure2.setText(events[1].getPressureMember()+"");
            pressureTime3.setText(((int)events[0].getRemainingOperationTime()/1000/60)
                    +" "+getContext().getString(R.string.minutesShort));
            leaderPressure3.setText(events[0].getPressureLeader()+"");
            memberPressure3.setText(events[0].getPressureMember()+"");
            pressureTime2.setVisibility(View.VISIBLE);
            leaderPressure2.setVisibility(View.VISIBLE);
            memberPressure2.setVisibility(View.VISIBLE);
            pressureTime3.setVisibility(View.VISIBLE);
            leaderPressure3.setVisibility(View.VISIBLE);
            memberPressure3.setVisibility(View.VISIBLE);
        } else if(events[1] != null){
            pressureTime1.setText(((int)events[1].getRemainingOperationTime()/1000/60)
                    +" "+getContext().getString(R.string.minutesShort));
            leaderPressure1.setText(events[1].getPressureLeader()+"");
            memberPressure1.setText(events[1].getPressureMember()+"");
            pressureTime2.setText(((int)events[0].getRemainingOperationTime()/1000/60)
                    +" "+getContext().getString(R.string.minutesShort));
            leaderPressure2.setText(events[0].getPressureLeader()+"");
            memberPressure2.setText(events[0].getPressureMember()+"");
            pressureTime2.setVisibility(View.VISIBLE);
            leaderPressure2.setVisibility(View.VISIBLE);
            memberPressure2.setVisibility(View.VISIBLE);
            pressureTime3.setVisibility(View.INVISIBLE);
            leaderPressure3.setVisibility(View.INVISIBLE);
            memberPressure3.setVisibility(View.INVISIBLE);
        } else {
            pressureTime1.setText(((int)events[0].getRemainingOperationTime()/1000/60)
                    +" "+getContext().getString(R.string.minutesShort));
            leaderPressure1.setText(events[0].getPressureLeader()+"");
            memberPressure1.setText(events[0].getPressureMember()+"");
            pressureTime2.setVisibility(View.INVISIBLE);
            leaderPressure2.setVisibility(View.INVISIBLE);
            memberPressure2.setVisibility(View.INVISIBLE);
            pressureTime3.setVisibility(View.INVISIBLE);
            leaderPressure3.setVisibility(View.INVISIBLE);
            memberPressure3.setVisibility(View.INVISIBLE);
        }
    }

    private void updateReturnPressure(){
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

    private void updateButtons(){
        EventType state = squad.getState();
        switch(state){
            case Register: setButtonState(1, false, false, false, true);
                break;
            case Begin: setButtonState(2, true, true, true, true);
                break;
            case Arrive: setButtonState(2, false, true, true, true);
                break;
            case Retreat: setButtonState(2, false, true, false, true);
                break;
            case PauseTimer: setButtonState(3, false, false, false, true);
                break;
        }
    }

    private void setButtonState(int startButton, boolean arriveButton, boolean pressureButton,
                                boolean retreatButton, boolean endButton){
        arrive.setEnabled(arriveButton);
        enterPressure.setEnabled(pressureButton);
        retreat.setEnabled(retreatButton);
        end.setEnabled(endButton);
        setStartButtonState(startButton);
    }

    private void setStartButtonState(int startButton){
        switch(startButton) {
            case 1: start.setText(R.string.startButton);
                startBuilder.setMessage(R.string.startSquadText)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                squad.beginOperation();
                                startButtonListener.onStartButtonClick();
                            }
                        });
                break;
            case 2: start.setText(R.string.pauseButton);
                startBuilder.setMessage(R.string.pauseSquadText)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                squad.pauseOperation();
                                startButtonListener.onStartButtonClick();
                            }
                        });
                break;
            case 3: start.setText(R.string.resumeButton);
                startBuilder.setMessage(R.string.resumeSquadText)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                squad.resumeOperation();
                                startButtonListener.onStartButtonClick();
                            }
                        });
                break;
        }
    }

    private void showAlarmMessage(){
        if(squad.isAlarmUnconfirmed()){
            String message = getContext().getString(R.string.alarmMessageText1) + " "
                    + squad.getName() + " " + getContext().getString(R.string.alarmMessageText2);
            AlertDialog alarmMessage = new AlertDialog.Builder(getContext())
                    .setTitle(R.string.alarmMessageTitle)
                    .setMessage(message)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            squad.confirmAlarm();
                            hardwareInterface.turnOffAlarm();
                        }
                    }).create();
            alarmMessage.show();
        }
    }

    @Override
    protected void activateViewReminder(){
        buttonAnimation.start();
    }

    @Override
    protected void deactivateViewReminder(){
        buttonAnimation.end();
    }

}

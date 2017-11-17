package de.thral.atemschutzueberwachung.ui.view;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.ui.dialog.EnterPressureDialog;
import de.thral.atemschutzueberwachung.ui.dialog.PressureWarningDialog;
import de.thral.atemschutzueberwachung.domain.Event;
import de.thral.atemschutzueberwachung.domain.EventType;
import de.thral.atemschutzueberwachung.domain.Squad;
import de.thral.atemschutzueberwachung.domain.SquadChangeListener;
import de.thral.atemschutzueberwachung.domain.TimerChangeListener;

public class DetailView extends RelativeLayout{

    public interface StartButtonListener {
        void onStartButtonClick();
    }

    private Context context;
    private View detailView;
    private View detailInfoView;
    private Squad squad;

    private StartButtonListener startButtonListener;

    private TextView timer, squadname, state, order, leaderName, memberName, leaderReturnPressure,
            memberReturnPressure, pressureTime1, leaderPressure1, memberPressure1, pressureTime2,
            leaderPressure2, memberPressure2, pressureTime3, leaderPressure3, memberPressure3;
    private Button start, arrive, enterPressure, retreat, end;

    private AlertDialog.Builder startBuilder;
    private ValueAnimator buttonAnimation;

    public DetailView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public DetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private FragmentManager getFragmentManager(){
        try{
            final Activity activity = (Activity) context;
            return activity.getFragmentManager();

        } catch (ClassCastException e) {
            throw new ClassCastException("Can't get the fragment manager with this");
        }
    }

    public void setStartButtonClickListener(StartButtonListener listener){
        this.startButtonListener = listener;
    }

    private void init(){
        initView();
        initButtons();
    }

    private void initView() {
        detailView = inflate(context, R.layout.monitor_detail, this);
        detailInfoView = detailView.findViewById(R.id.infoScreen);
        timer = (TextView) detailInfoView.findViewById(R.id.timer);
        squadname = (TextView) detailInfoView.findViewById(R.id.squadname);
        state = (TextView) detailInfoView.findViewById(R.id.state);
        order = (TextView) detailInfoView.findViewById(R.id.order);
        leaderName = (TextView) detailInfoView.findViewById(R.id.leaderName);
        memberName = (TextView) detailInfoView.findViewById(R.id.memberName);
        leaderReturnPressure = (TextView) detailInfoView.findViewById(R.id.leaderReturnPressure);
        memberReturnPressure = (TextView) detailInfoView.findViewById(R.id.memberReturnPressure);
        pressureTime1 = (TextView) detailInfoView.findViewById(R.id.pressureTime1);
        leaderPressure1 = (TextView) detailInfoView.findViewById(R.id.leaderPressure1);
        memberPressure1 = (TextView) detailInfoView.findViewById(R.id.memberPressure1);
        pressureTime2 = (TextView) detailInfoView.findViewById(R.id.pressureTime2);
        leaderPressure2 = (TextView) detailInfoView.findViewById(R.id.leaderPressure2);
        memberPressure2 = (TextView) detailInfoView.findViewById(R.id.memberPressure2);
        pressureTime3 = (TextView) detailInfoView.findViewById(R.id.pressureTime3);
        leaderPressure3 = (TextView) detailInfoView.findViewById(R.id.leaderPressure3);
        memberPressure3 = (TextView) detailInfoView.findViewById(R.id.memberPressure3);
    }

    private void initButtons(){
        start = (Button) detailView.findViewById(R.id.buttonStart);
        arrive = (Button) detailView.findViewById(R.id.buttonArrive);
        enterPressure = (Button) detailView.findViewById(R.id.buttonEnterPressure);
        retreat = (Button) detailView.findViewById(R.id.buttonRetreat);
        end = (Button) detailView.findViewById(R.id.buttonEnd);

        buttonAnimation = ValueAnimator.ofInt(Color.RED, Color.LTGRAY);
        buttonAnimation.setDuration(1500);
        buttonAnimation.setRepeatCount(ValueAnimator.INFINITE);
        buttonAnimation.setEvaluator(new ArgbEvaluator());
        buttonAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                enterPressure.getBackground().setColorFilter(
                        (int)valueAnimator.getAnimatedValue(), PorterDuff.Mode.MULTIPLY);
            }
        });

        startBuilder = new AlertDialog.Builder(context);
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
                EnterPressureDialog dialog = EnterPressureDialog.newInstance(
                        context, EventType.Arrive);
                dialog.show(getFragmentManager(), "PressureDialog");
            }
        });
        enterPressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnterPressureDialog dialog = EnterPressureDialog.newInstance(
                        context, EventType.Timer);
                dialog.show(getFragmentManager(), "PressureDialog");
            }
        });
        retreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnterPressureDialog dialog = EnterPressureDialog.newInstance(
                        context, EventType.Retreat);
                dialog.show(getFragmentManager(), "PressureDialog");
            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnterPressureDialog dialog = EnterPressureDialog.newInstance(
                        context, EventType.End);
                dialog.show(getFragmentManager(), "PressureDialog");
            }
        });
    }

    public void setSquad(Squad squad){
        this.squad = squad;
        timer.setText(squad.getTimerValueAsClock());
        squadname.setText(squad.getName());
        state.setText(squad.getState().getStateDescription(context));
        order.setText(squad.getOrder().toString(context));
        leaderName.setText(squad.getLeader().getDisplayName());
        memberName.setText(squad.getMember().getDisplayName());

        if(squad.isTimerExpired()){
            activateAlarm();
        }
        squad.setTimerListener(new TimerChangeListener() {
            @Override
            public void onTimerUpdate(String timer) {
                DetailView.this.timer.setText(timer);
            }

            @Override
            public void onTimerReachedMark(boolean expired) {
                if(expired){
                    activateAlarm();
                    buttonAnimation.end();
                } else {
                    buttonAnimation.start();
                }
            }
        });
        squad.setChangeListener(new SquadChangeListener() {
            @Override
            public void onStateUpdate(Squad squad) {
                state.setText(squad.getState().getStateDescription(context));
                updateButtons();
            }

            @Override
            public void onPressureUpdate(Squad squad) {
                Event[] events = squad.getLastPressureValues(3);
                updateDetailPressure(events);
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
    }

    private void updateDetailPressure(Event[] events){
        if(events[2] != null){
            pressureTime1.setText(((int)events[2].getRemainingOperationTime()/1000/60)
                    +" "+context.getString(R.string.minutesShort));
            leaderPressure1.setText(events[2].getPressureLeader()+"");
            memberPressure1.setText(events[2].getPressureMember()+"");
            pressureTime2.setText(((int)events[1].getRemainingOperationTime()/1000/60)
                    +" "+context.getString(R.string.minutesShort));
            leaderPressure2.setText(events[1].getPressureLeader()+"");
            memberPressure2.setText(events[1].getPressureMember()+"");
            pressureTime3.setText(((int)events[0].getRemainingOperationTime()/1000/60)
                    +" "+context.getString(R.string.minutesShort));
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
                    +" "+context.getString(R.string.minutesShort));
            leaderPressure1.setText(events[1].getPressureLeader()+"");
            memberPressure1.setText(events[1].getPressureMember()+"");
            pressureTime2.setText(((int)events[0].getRemainingOperationTime()/1000/60)
                    +" "+context.getString(R.string.minutesShort));
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
                    +" "+context.getString(R.string.minutesShort));
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
        if(squad.isReminderActive()){
            buttonAnimation.start();

        } else {
            buttonAnimation.end();
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

    protected void activateAlarm(){
        ValueAnimator colorAnim = ObjectAnimator.ofInt(timer, "textColor", Color.RED, Color.GRAY);
        colorAnim.setDuration(800);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(ValueAnimator.INFINITE);
        colorAnim.start();
    }
}

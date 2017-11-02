package de.thral.atemschutzueberwachung.activity;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.thral.atemschutzueberwachung.AtemschutzueberwachungApplication;
import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.activity.dialog.EnterPressureDialog;
import de.thral.atemschutzueberwachung.activity.dialog.PressureWarningDialog;
import de.thral.atemschutzueberwachung.domain.Event;
import de.thral.atemschutzueberwachung.domain.EventType;
import de.thral.atemschutzueberwachung.domain.Operation;
import de.thral.atemschutzueberwachung.domain.Squad;
import de.thral.atemschutzueberwachung.domain.SquadChangeListener;
import de.thral.atemschutzueberwachung.persistence.OperationDAO;

public class MonitoringDetailActivity extends MonitoringActivity
        implements EnterPressureDialog.EnteredPressureListener{

    public static final String DETAIL_KEY = "DETAIL";

    private Squad selected;
    private Squad[] overview;

    private OperationDAO operationDAO;

    private Button start, arrive, enterPressure, retreat, end;
    private AlertDialog.Builder startBuilder;
    private ValueAnimator buttonAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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

        startBuilder = new AlertDialog.Builder(MonitoringDetailActivity.this);
        startBuilder.setTitle(R.string.timerInfoTitle)
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        Intent intent = getIntent();
        operationDAO = ((AtemschutzueberwachungApplication)getApplication()).getOperationDAO();
        initSquads(intent.getExtras().getInt(DETAIL_KEY));
        initOverviewSmall(1);
        initOverviewSmall(2);
        initOverviewSmall(3);

        initDetailView();
    }

    private RelativeLayout getLayoutOf(Squad squad){
        if(squad == selected){
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.detail);
            return (RelativeLayout) layout.findViewById(R.id.infoScreen);
        } else if(squad == overview[0]) {
            return (RelativeLayout) findViewById(R.id.overview_small_1);
        } else if(squad == overview[1]) {
            return (RelativeLayout) findViewById(R.id.overview_small_2);
        } else if(squad == overview[2]) {
            return (RelativeLayout) findViewById(R.id.overview_small_3);
        }
        return null;
    }

    private void initSquads(int selectedSquad){
        overview = new Squad[Operation.MAX_SQUAD_COUNT-1];
        Squad[] squads = operationDAO.getActive().getActiveSquads();
        int j=0;
        for(int i=0; i<squads.length; i++){
            if(i+1 == selectedSquad){
                selected = squads[i];
            } else {
                overview[j] = squads[i];
                j++;
            }
        }
    }

    private void initOverviewSmall(int overviewNumber){
        Squad squad = overview[overviewNumber-1];
        RelativeLayout layout = null;
        switch (overviewNumber){
            case 1: layout = (RelativeLayout) findViewById(R.id.overview_small_1);
                break;
            case 2: layout = (RelativeLayout) findViewById(R.id.overview_small_2);
                break;
            case 3: layout = (RelativeLayout) findViewById(R.id.overview_small_3);
                break;
        }

        if(squad != null){
            layout.setVisibility(View.VISIBLE);

            initInfoView(layout, squad);
            updateOverviewPressure(layout, squad);
            if(squad.isReminderActive()){
                activateReminder(layout);
            } else {
                deactivateReminder(layout);
            }

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MonitoringDetailActivity.this,
                            MonitoringOverviewActivity.class);
                    //intent.putExtra(MonitoringDetailActivity.DETAIL_KEY, overviewNumber);
                    startActivity(intent);
                }
            });

            //TODO devide in setOverViewListener (Time, ReachedMark) and setDetailListener(Rest)
            squad.setChangeListener(new SquadChangeListener() {

                @Override
                public void onTimerUpdate(Squad squad) {
                    RelativeLayout layout = getLayoutOf(squad);
                    if(layout != null){
                        TextView timer = (TextView)layout.findViewById(R.id.timer);
                        timer.setText(squad.getTimerValueAsClock());
                    }
                }

                @Override
                public void onStateUpdate(Squad squad) {

                }

                @Override
                public void onPressureUpdate(Squad squad) {
                }

                @Override
                public void onCalculatedReturnPressure(Squad squad) {
                    RelativeLayout layout = getLayoutOf(squad);
                    updateReturnPressure(layout, squad);
                }

                @Override
                public void onTimerReachedMark(Squad squad, boolean expired) {
                    timerReachedMark(getLayoutOf(squad), squad, expired);
                }

                @Override
                public void onPressureInfo(Squad squad, boolean underShot) {
                }
            });
            return;
        }
        layout.setVisibility(View.INVISIBLE);
    }


    private void initDetailView(){
        RelativeLayout infoLayout = getLayoutOf(selected);
        initInfoView(infoLayout, selected);
        TextView order = (TextView) infoLayout.findViewById(R.id.order);
        order.setText(selected.getOrder().toString(getApplicationContext()));
        updateDetailPressure(infoLayout, selected.getLastPressureValues());

        selected.setChangeListener(new SquadChangeListener() {
            @Override
            public void onTimerUpdate(Squad squad) {
                RelativeLayout layout = getLayoutOf(squad);
                TextView timer = (TextView)layout.findViewById(R.id.timer);
                timer.setText(squad.getTimerValueAsClock());
            }

            @Override
            public void onStateUpdate(Squad squad) {
                RelativeLayout layout = getLayoutOf(squad);
                TextView state = (TextView)layout.findViewById(R.id.state);
                state.setText(squad.getState().getStateDescription(getApplicationContext()));
                initButtons();
            }

            @Override
            public void onPressureUpdate(Squad squad) {
                RelativeLayout layout = getLayoutOf(squad);
                Event[] events = squad.getLastPressureValues();
                updateDetailPressure(layout, events);
            }

            @Override
            public void onCalculatedReturnPressure(Squad squad) {
                RelativeLayout layout = getLayoutOf(squad);
                updateReturnPressure(layout, selected);
            }

            @Override
            public void onTimerReachedMark(Squad squad, boolean expired) {
                if(expired){
                    activateAlarm(getLayoutOf(squad));
                } else {
                    buttonAnimation.start();
                }
            }

            @Override
            public void onPressureInfo(Squad squad, boolean underShot) {
                PressureWarningDialog dialog = PressureWarningDialog.newInstance(underShot);
                dialog.show(getFragmentManager(), "PressureWarningDialog");
            }
        });
        initButtons();
    }

    private void updateDetailPressure(RelativeLayout layout, Event[] events){
        TextView pressureTime1 = (TextView)layout.findViewById(R.id.pressureTime1);
        TextView leaderPressure1 = (TextView)layout.findViewById(R.id.leaderPressure1);
        TextView memberPressure1 = (TextView)layout.findViewById(R.id.memberPressure1);
        TextView pressureTime2 = (TextView)layout.findViewById(R.id.pressureTime2);
        TextView leaderPressure2 = (TextView)layout.findViewById(R.id.leaderPressure2);
        TextView memberPressure2 = (TextView)layout.findViewById(R.id.memberPressure2);
        TextView pressureTime3 = (TextView)layout.findViewById(R.id.pressureTime3);
        TextView leaderPressure3 = (TextView)layout.findViewById(R.id.leaderPressure3);
        TextView memberPressure3 = (TextView)layout.findViewById(R.id.memberPressure3);

        if(events[2] != null){
            pressureTime1.setText(((int)events[2].getRemainingOperationTime()/1000/60)+"");
            leaderPressure1.setText(events[2].getPressureLeader()+"");
            memberPressure1.setText(events[2].getPressureMember()+"");
            pressureTime2.setText(((int)events[1].getRemainingOperationTime()/1000/60)+"");
            leaderPressure2.setText(events[1].getPressureLeader()+"");
            memberPressure2.setText(events[1].getPressureMember()+"");
            pressureTime3.setText(((int)events[0].getRemainingOperationTime()/1000/60)+"");
            leaderPressure3.setText(events[0].getPressureLeader()+"");
            memberPressure3.setText(events[0].getPressureMember()+"");
            pressureTime2.setVisibility(View.VISIBLE);
            leaderPressure2.setVisibility(View.VISIBLE);
            memberPressure2.setVisibility(View.VISIBLE);
            pressureTime3.setVisibility(View.VISIBLE);
            leaderPressure3.setVisibility(View.VISIBLE);
            memberPressure3.setVisibility(View.VISIBLE);
        } else if(events[1] != null){
            pressureTime1.setText(((int)events[1].getRemainingOperationTime()/1000/60)+"");
            leaderPressure1.setText(events[1].getPressureLeader()+"");
            memberPressure1.setText(events[1].getPressureMember()+"");
            pressureTime2.setText(((int)events[0].getRemainingOperationTime()/1000/60)+"");
            leaderPressure2.setText(events[0].getPressureLeader()+"");
            memberPressure2.setText(events[0].getPressureMember()+"");
            pressureTime2.setVisibility(View.VISIBLE);
            leaderPressure2.setVisibility(View.VISIBLE);
            memberPressure2.setVisibility(View.VISIBLE);
            pressureTime3.setVisibility(View.INVISIBLE);
            leaderPressure3.setVisibility(View.INVISIBLE);
            memberPressure3.setVisibility(View.INVISIBLE);
        } else {
            pressureTime1.setText(((int)events[0].getRemainingOperationTime()/1000/60)+"");
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

    private void initButtons(){
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.detail);
        start = (Button) layout.findViewById(R.id.buttonStart);
        arrive = (Button) layout.findViewById(R.id.buttonArrive);
        enterPressure = (Button) layout.findViewById(R.id.buttonEnterPressure);
        retreat = (Button) layout.findViewById(R.id.buttonRetreat);
        end = (Button) layout.findViewById(R.id.buttonEnd);

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
                        getApplicationContext(), EventType.Arrive);
                dialog.show(getFragmentManager(), "PressureDialog");
            }
        });
        enterPressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnterPressureDialog dialog = EnterPressureDialog.newInstance(
                        getApplicationContext(), EventType.Timer);
                dialog.show(getFragmentManager(), "PressureDialog");
            }
        });
        retreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnterPressureDialog dialog = EnterPressureDialog.newInstance(
                        getApplicationContext(), EventType.Retreat);
                dialog.show(getFragmentManager(), "PressureDialog");
            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnterPressureDialog dialog = EnterPressureDialog.newInstance(
                        getApplicationContext(), EventType.End);
                dialog.show(getFragmentManager(), "PressureDialog");
            }
        });

        EventType state = selected.getState();
        switch(state){
            case Register: setButtonState(1, false, false, false, false);
            break;
            case Begin: setButtonState(2, true, true, true, true);
            break;
            case Arrive: setButtonState(2, false, true, true, true);
            break;
            case Retreat: setButtonState(2, false, true, false, true);
            break;
            case PauseTimer: setButtonState(3, false, false, false, false);
            break;
        }
        if(selected.isReminderActive()){
            buttonAnimation.start();

        } else {
            buttonAnimation.end();
//                    enterPressure.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
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
                                selected.beginOperation();
                            }
                        });
                break;    //Registered - Button to Beginn
            case 2: start.setText(R.string.pauseButton);
                startBuilder.setMessage(R.string.pauseSquadText)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                selected.pauseOperation();
                            }
                        });
                break;    //Beginn(Resume) -> Button to Pause -> Start Timer
            case 3: start.setText(R.string.resumeButton);
                startBuilder.setMessage(R.string.resumeSquadText)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                selected.resumeOperation();
                            }
                        });
                break;    //Pause -> Button to Resume
        }
    }

    @Override
    public void onEnteredPressure(EventType event, int leaderPressure, int memberPressure) {
        switch(event){
            case Arrive: selected.arriveTarget(leaderPressure, memberPressure);
                        break;
            case Timer: selected.addPressureValues(EventType.Timer, leaderPressure, memberPressure);
                        break;
            case Retreat: selected.retreat(leaderPressure, memberPressure);
                        break;
            case End: selected.endOperation(leaderPressure, memberPressure);
                        Intent intent = new Intent(MonitoringDetailActivity.this, MonitoringOverviewActivity.class);
                        startActivity(intent);
                        break;
        }

    }

}

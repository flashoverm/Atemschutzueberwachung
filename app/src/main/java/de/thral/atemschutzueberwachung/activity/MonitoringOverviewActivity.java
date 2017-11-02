package de.thral.atemschutzueberwachung.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import de.thral.atemschutzueberwachung.AtemschutzueberwachungApplication;
import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.activity.dialog.EndOperationDialog;
import de.thral.atemschutzueberwachung.activity.dialog.RegisterSquadDialog;
import de.thral.atemschutzueberwachung.domain.Squad;
import de.thral.atemschutzueberwachung.domain.SquadChangeListener;
import de.thral.atemschutzueberwachung.persistence.OperationDAO;

public class MonitoringOverviewActivity extends MonitoringActivity
        implements RegisterSquadDialog.SquadRegisteredListener,
        EndOperationDialog.EndOperationListener{

    private OperationDAO operationDAO;
    private Squad[] overview;
    private Button register;
    private Button endOperation;

    private boolean registerDisplayed;
    private View.OnClickListener registerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        operationDAO = ((AtemschutzueberwachungApplication)getApplication()).getOperationDAO();

        initView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initView();
    }

    private RelativeLayout getLayoutOf(Squad squad){
        if(squad == overview[0]){
            return (RelativeLayout) findViewById(R.id.overview_1);
        } else if(squad == overview[1]) {
            return (RelativeLayout) findViewById(R.id.overview_2);
        } else if(squad == overview[2]) {
            return (RelativeLayout) findViewById(R.id.overview_3);
        } else if(squad == overview[3]) {
            return (RelativeLayout) findViewById(R.id.overview_4);
        }
        return null;
    }

    private void initView(){
        registerDisplayed = false;
        registerListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterSquadDialog dialog
                        = RegisterSquadDialog.newInstance();
                dialog.show(getFragmentManager(), "RegisterSquad");
            }
        };
        overview = operationDAO.getActive().getActiveSquads();
        initOverview(1);
        initOverview(2);
        initOverview(3);
        initOverview(4);
    }

    private void initOverview(final int overviewNumber){
        Squad squad = overview[overviewNumber-1];
        RelativeLayout layout = null;
        ViewFlipper flipper = null;
        switch(overviewNumber){
            case 1: layout = (RelativeLayout) findViewById(R.id.overview_1);
                    flipper = (ViewFlipper)findViewById(R.id.flipper_1);
                    break;
            case 2: layout = (RelativeLayout) findViewById(R.id.overview_2);
                    flipper = (ViewFlipper)findViewById(R.id.flipper_2);
                    break;
            case 3: layout = (RelativeLayout) findViewById(R.id.overview_3);
                    flipper = (ViewFlipper)findViewById(R.id.flipper_3);
                    break;
            case 4: layout = (RelativeLayout) findViewById(R.id.overview_4);
                    flipper = (ViewFlipper)findViewById(R.id.flipper_4);
                    if(onNoRegisteredSquad(flipper)){ return; }
                    break;
        }

        if(squad != null) {
            flipper.setDisplayedChild(0);
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
                    Intent intent = new Intent(MonitoringOverviewActivity.this,
                            MonitoringDetailActivity.class);
                    intent.putExtra(MonitoringDetailActivity.DETAIL_KEY, overviewNumber);
                    startActivity(intent);
                }
            });
            squad.setChangeListener(new SquadChangeListener() {

                @Override
                public void onTimerUpdate(Squad squad) {
                    RelativeLayout layout = getLayoutOf(squad);
                    TextView timer = (TextView) layout.findViewById(R.id.timer);
                    timer.setText(squad.getTimerValueAsClock());
                }

                @Override
                public void onStateUpdate(Squad squad) {
                }

                @Override
                public void onPressureUpdate(Squad squad) {
                }

                @Override
                public void onCalculatedReturnPressure(Squad squad) {
                }

                @Override
                public void onTimerReachedMark(Squad squad, boolean expired) {
                    timerReachedMark(getLayoutOf(squad), squad, expired);
                }

                @Override
                public void onPressureInfo(Squad squad, boolean underShot) {
                }
            });

        } else if(!registerDisplayed){
            flipper.setDisplayedChild(1);
            register = (Button) flipper.findViewById(R.id.buttonRegister);
            register.setOnClickListener(registerListener);
            registerDisplayed = true;
        } else {
            flipper.setDisplayedChild(0);
            layout.setVisibility(View.INVISIBLE);
        }
    }

    public boolean onNoRegisteredSquad(ViewFlipper viewFlipper){
        if(overview[0]==null){
            viewFlipper.setDisplayedChild(2);
            endOperation = (Button) viewFlipper.findViewById(R.id.buttonEndOperation);
            endOperation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EndOperationDialog dialog = EndOperationDialog.newInstance();
                    dialog.show(getFragmentManager(), "EndOperation");
                }
            });
            return true;
        }
        return false;
    }

    @Override
    public void onSquadRegistered(Squad registeredSquad) {
        operationDAO.getActive().registerSquad(registeredSquad);
        initView();
    }

    @Override
    public void onOperationEnd(String observer, String operationType, String location, String unit) {
        if(operationDAO.getActive().complete(operationType, location, observer, unit)){
            Intent intent = new Intent(MonitoringOverviewActivity.this, MenuActivity.class);
            startActivity(intent);
        } else {
            //TODO squads-active dialog
        }
    }
}

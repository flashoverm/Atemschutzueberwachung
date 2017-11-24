package de.thral.atemschutzueberwachung.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import de.thral.atemschutzueberwachung.DraegermanObservationApplication;
import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.business.Event;
import de.thral.atemschutzueberwachung.business.EventType;
import de.thral.atemschutzueberwachung.business.Operation;
import de.thral.atemschutzueberwachung.business.Squad;
import de.thral.atemschutzueberwachung.hardware.HardwareInterface;
import de.thral.atemschutzueberwachung.persistence.OperationDAO;
import de.thral.atemschutzueberwachung.ui.dialog.EnterPressureDialog;
import de.thral.atemschutzueberwachung.ui.view.DetailOverviewView;
import de.thral.atemschutzueberwachung.ui.view.DetailView;
import de.thral.atemschutzueberwachung.ui.view.LayoutClickListener;

public class MonitoringDetailActivity extends AppCompatActivity
        implements EnterPressureDialog.EnteredPressureListener,
        LayoutClickListener, DetailView.StartButtonListener {

    public static final String DETAIL_KEY = "DETAIL";

    private Squad selected;
    private Squad[] overviewSquads;

    private OperationDAO operationDAO;
    private HardwareInterface hardwareInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();
        this.operationDAO = ((DraegermanObservationApplication)getApplication()).getOperationDAO();
        this.hardwareInterface = ((DraegermanObservationApplication) this.getApplication())
                .getHardwareInterface();
        initSquads(intent.getExtras().getInt(DETAIL_KEY));
        initOverviews();
        initDetailView();
    }

    private void initSquads(int selectedSquad){
        overviewSquads = new Squad[Operation.MAX_SQUAD_COUNT-1];
        Squad[] squads = operationDAO.getActive().getActiveSquads();
        int j=0;
        for(int i=0; i<squads.length; i++){
            if(i+1 == selectedSquad){
                selected = squads[i];
            } else {
                overviewSquads[j] = squads[i];
                j++;
            }
        }
    }

    private void initOverviews(){
        DetailOverviewView overview = findViewById(R.id.detailOverview);
        overview.setListener(this);
        overview.setSquads(overviewSquads);
    }

    private void initDetailView(){
        DetailView detailView = findViewById(R.id.detail);
        detailView.setStartButtonClickListener(this);
        detailView.setSquad(selected);
    }

    @Override
    public void onLayoutClick(int layoutNumber){
        Intent intent = new Intent(MonitoringDetailActivity.this,
                MonitoringOverviewActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onEnteredPressure(EventType event, int leaderPressure, int memberPressure) {
        Event lastPressure = selected.getLastPressureValues(1)[0];
        if(lastPressure.getPressureLeader() >= leaderPressure
                && lastPressure.getPressureMember() >= memberPressure){
            switch(event){
                case Arrive: selected.arriveTarget(leaderPressure, memberPressure);
                    operationDAO.updateActive();
                    break;
                case Timer: selected.addPressureValues(EventType.Timer, leaderPressure, memberPressure);
                    operationDAO.updateActive();
                    break;
                case Retreat: selected.retreat(leaderPressure, memberPressure);
                    operationDAO.updateActive();
                    break;
                case End: selected.endOperation(leaderPressure, memberPressure);
                    operationDAO.updateActive();
                    this.finish();
                    break;
            }
            return true;
        }
        return false;
    }

    @Override
    public void onStartButtonClick() {
        operationDAO.updateActive();
    }
}

package de.thral.atemschutzueberwachung.activity;

import android.content.Intent;
import android.os.Bundle;

import de.thral.atemschutzueberwachung.DraegermanObservationApplication;
import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.activity.view.DetailOverviewView;
import de.thral.atemschutzueberwachung.activity.view.DetailView;
import de.thral.atemschutzueberwachung.activity.view.LayoutClickListener;
import de.thral.atemschutzueberwachung.activity.dialog.EnterPressureDialog;
import de.thral.atemschutzueberwachung.domain.Event;
import de.thral.atemschutzueberwachung.domain.EventType;
import de.thral.atemschutzueberwachung.domain.Operation;
import de.thral.atemschutzueberwachung.domain.Squad;
import de.thral.atemschutzueberwachung.persistence.OperationDAO;

public class MonitoringDetailActivity extends MonitoringActivity
        implements EnterPressureDialog.EnteredPressureListener,
        LayoutClickListener{

    public static final String DETAIL_KEY = "DETAIL";

    private Squad selected;
    private Squad[] overviewSquads;

    private OperationDAO operationDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        operationDAO = ((DraegermanObservationApplication)getApplication()).getOperationDAO();
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
        detailView.setSquad(selected);
    }

    @Override
    public void onLayoutClick(int layoutNumber){
        Intent intent = new Intent(MonitoringDetailActivity.this,
                MonitoringOverviewActivity.class);
        //intent.putExtra(MonitoringDetailActivity.DETAIL_KEY, overviewNumber);
        startActivity(intent);
    }

    @Override
    public boolean onEnteredPressure(EventType event, int leaderPressure, int memberPressure) {
        Event lastPressure = selected.getLastPressureValue();
        if(lastPressure.getPressureLeader() >= leaderPressure
                && lastPressure.getPressureMember() >= memberPressure){
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
            return true;
        }
        return false;
    }

}

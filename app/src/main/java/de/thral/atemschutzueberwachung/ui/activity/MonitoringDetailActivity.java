package de.thral.atemschutzueberwachung.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import de.thral.atemschutzueberwachung.DraegermanObservationApplication;
import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.domain.Event;
import de.thral.atemschutzueberwachung.domain.EventType;
import de.thral.atemschutzueberwachung.domain.Operation;
import de.thral.atemschutzueberwachung.domain.Squad;
import de.thral.atemschutzueberwachung.persistence.OperationDAO;
import de.thral.atemschutzueberwachung.ui.dialog.EnterPressureDialog;
import de.thral.atemschutzueberwachung.ui.view.DetailOverviewView;
import de.thral.atemschutzueberwachung.ui.view.DetailView;
import de.thral.atemschutzueberwachung.ui.view.LayoutClickListener;

public class MonitoringDetailActivity extends AppCompatActivity
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
        Event lastPressure = selected.getLastPressureValues(1)[0];
        if(lastPressure.getPressureLeader() >= leaderPressure
                && lastPressure.getPressureMember() >= memberPressure){
            switch(event){
                case Arrive: selected.arriveTarget(leaderPressure, memberPressure);
                    operationDAO.update();
                    break;
                case Timer: selected.addPressureValues(EventType.Timer, leaderPressure, memberPressure);
                    operationDAO.update();
                    break;
                case Retreat: selected.retreat(leaderPressure, memberPressure);
                    operationDAO.update();
                    break;
                case End: selected.endOperation(leaderPressure, memberPressure);
                    operationDAO.update();
                    Intent intent = new Intent(MonitoringDetailActivity.this, MonitoringOverviewActivity.class);
                    startActivity(intent);
                    break;
            }
            return true;
        }
        return false;
    }

}

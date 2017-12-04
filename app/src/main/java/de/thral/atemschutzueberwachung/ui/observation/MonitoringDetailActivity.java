package de.thral.atemschutzueberwachung.ui.observation;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import de.thral.atemschutzueberwachung.DraegermanObservationApplication;
import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.business.Event;
import de.thral.atemschutzueberwachung.business.EventType;
import de.thral.atemschutzueberwachung.business.Operation;
import de.thral.atemschutzueberwachung.business.Squad;
import de.thral.atemschutzueberwachung.ui.observation.dialog.EnterPressureDialog;
import de.thral.atemschutzueberwachung.ui.observation.view.DetailOverviewView;
import de.thral.atemschutzueberwachung.ui.observation.view.DetailView;
import de.thral.atemschutzueberwachung.ui.observation.view.LayoutClickListener;

public class MonitoringDetailActivity extends MonitoringBaseActivity
        implements EnterPressureDialog.EnteredPressureListener,
        LayoutClickListener, DetailView.StartButtonListener {

    public static final String DETAIL_KEY = "DETAIL";

    private Squad selected;
    private Squad[] overviewSquads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();
        this.activeOperationDAO = ((DraegermanObservationApplication)getApplication())
                .getActiveOperationDAO();
        initSquads(intent.getExtras().getInt(DETAIL_KEY));
        initOverviews();
        initDetailView();
    }

    private void initSquads(int selectedSquad){
        overviewSquads = new Squad[Operation.MAX_SQUAD_COUNT-1];
        Squad[] squads = activeOperationDAO.get().getActiveSquads();
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
                    new UpdateOperationTask().execute();
                    break;
                case Timer: selected.addPressureValues(EventType.Timer, leaderPressure, memberPressure);
                    new UpdateOperationTask().execute();
                    break;
                case Retreat: selected.retreat(leaderPressure, memberPressure);
                    new UpdateOperationTask().execute();
                    break;
                case End: selected.endOperation(leaderPressure, memberPressure);
                    new UpdateOperationTask().execute();
                    this.finish();
                    break;
            }
            return true;
        }
        return false;
    }

    @Override
    public void onStartButtonClick() {
        new UpdateOperationTask().execute();
    }


}

package de.thral.draegermanObservation.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import de.thral.draegermanObservation.R;
import de.thral.draegermanObservation.business.Event;
import de.thral.draegermanObservation.business.EventType;
import de.thral.draegermanObservation.business.Operation;
import de.thral.draegermanObservation.business.Squad;
import de.thral.draegermanObservation.ui.dialog.EnterPressureDialog;
import de.thral.draegermanObservation.ui.view.DetailOverviewView;
import de.thral.draegermanObservation.ui.view.DetailView;
import de.thral.draegermanObservation.ui.view.LayoutClickListener;

public class MonitoringDetailActivity extends MonitoringBaseActivity
        implements EnterPressureDialog.EnteredPressureListener, LayoutClickListener {

    public static final String DETAIL_KEY = "DETAIL";

    private Squad selected;
    private Squad[] overviewSquads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
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
        detailView.setSquad(selected);
    }

    @Override
    public void onLayoutClick(int layoutNumber){
        this.finish();
    }

    @Override
    public boolean onEnteredPressure(EventType event, int leaderPressure, int memberPressure) {
        Event lastPressure = selected.getLastPressureValues(1)[0];
        if(lastPressure.getPressureLeader() >= leaderPressure
                && lastPressure.getPressureMember() >= memberPressure){

            switch(event){
                case Arrive: selected.arriveTarget(leaderPressure, memberPressure);
                    break;
                case Timer: selected.pressureOnTime(leaderPressure, memberPressure);
                    break;
                case Retreat: selected.retreat(leaderPressure, memberPressure);
                    break;
                case End: selected.endOperation(leaderPressure, memberPressure);
                    this.finish();
                    break;
            }
            updateOperation();
            return true;
        }
        return false;
    }
}

package de.thral.atemschutzueberwachung.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import de.thral.atemschutzueberwachung.AtemschutzueberwachungApplication;
import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.activity.Views.LayoutClickListener;
import de.thral.atemschutzueberwachung.activity.Views.OverviewView;
import de.thral.atemschutzueberwachung.activity.dialog.EndOperationDialog;
import de.thral.atemschutzueberwachung.activity.dialog.RegisterSquadDialog;
import de.thral.atemschutzueberwachung.domain.Squad;
import de.thral.atemschutzueberwachung.persistence.OperationDAO;

public class MonitoringOverviewActivity extends MonitoringActivity
        implements RegisterSquadDialog.SquadRegisteredListener,
        EndOperationDialog.EndOperationListener,
        LayoutClickListener{

    private OverviewView overviewView;
    private OperationDAO operationDAO;

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

    private void initView(){
        overviewView = findViewById(R.id.overview);
        overviewView.setListener(
                this,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RegisterSquadDialog dialog
                                = RegisterSquadDialog.newInstance();
                        dialog.show(getFragmentManager(), "RegisterSquad");
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EndOperationDialog dialog = EndOperationDialog.newInstance();
                        dialog.show(getFragmentManager(), "EndOperation");
                    }
                }
        );
        overviewView.setSquads(operationDAO.getActive().getActiveSquads());
    }

    public void onLayoutClick(int layoutNumber){
        Intent intent = new Intent(MonitoringOverviewActivity.this,
                MonitoringDetailActivity.class);
        intent.putExtra(MonitoringDetailActivity.DETAIL_KEY, layoutNumber+1);
        startActivity(intent);
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

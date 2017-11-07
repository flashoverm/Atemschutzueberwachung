package de.thral.atemschutzueberwachung.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import de.thral.atemschutzueberwachung.DraegermanObservationApplication;
import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.activity.view.LayoutClickListener;
import de.thral.atemschutzueberwachung.activity.view.OverviewView;
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

        operationDAO = ((DraegermanObservationApplication)getApplication()).getOperationDAO();
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
                        if(!operationDAO.getActive().isSquadActive()){
                            EndOperationDialog dialog = EndOperationDialog.newInstance();
                            dialog.show(getFragmentManager(), "EndOperation");
                            return;
                        }
                        new AlertDialog.Builder(MonitoringOverviewActivity.this)
                                .setTitle(R.string.endOperationTitle)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                })
                                .setMessage(R.string.endOperationWarning)
                                .create().show();
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
            operationDAO.getActive().complete(operationType, location, observer, unit);
            Intent intent = new Intent(MonitoringOverviewActivity.this, MenuActivity.class);
            startActivity(intent);
    }
}

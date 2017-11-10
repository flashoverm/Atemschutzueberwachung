package de.thral.atemschutzueberwachung.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import de.thral.atemschutzueberwachung.DraegermanObservationApplication;
import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.domain.EventType;
import de.thral.atemschutzueberwachung.domain.Squad;
import de.thral.atemschutzueberwachung.persistence.OperationDAO;
import de.thral.atemschutzueberwachung.ui.dialog.EndOperationDialog;
import de.thral.atemschutzueberwachung.ui.dialog.RegisterSquadDialog;
import de.thral.atemschutzueberwachung.ui.view.LayoutClickListener;
import de.thral.atemschutzueberwachung.ui.view.OverviewView;

public class MonitoringOverviewActivity extends AppCompatActivity
        implements RegisterSquadDialog.SquadRegisteredListener,
        EndOperationDialog.EndOperationListener,
        LayoutClickListener{

    public static final String KEY_RESUMED = "OperationResumed";

    private Context context;
    private OverviewView overviewView;
    private OperationDAO operationDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        this.context = getApplicationContext();
        operationDAO = ((DraegermanObservationApplication)getApplication()).getOperationDAO();
        Intent intent = getIntent();
        initView();

        if(intent.getBooleanExtra(KEY_RESUMED, false)){
            for(Squad squad : operationDAO.getActive().getActiveSquads()){
                if(squad != null){
                    if(!squad.getState().equals(EventType.PauseTimer)
                            && !squad.getState().equals(EventType.Register)){
                        squad.resumeAfterError();
                    }
                }
            }
            Toast.makeText(context, context.getString(R.string.toastOperationResumed),
                    Toast.LENGTH_LONG ).show();
        }
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
        operationDAO.update();
        initView();
    }

    @Override
    public void onOperationEnd(String observer, String operationType, String location, String unit) {
            operationDAO.getActive().complete(operationType, location, observer, unit);
            operationDAO.endOperation();
            Intent intent = new Intent(MonitoringOverviewActivity.this, MenuActivity.class);
            startActivity(intent);
    }
}

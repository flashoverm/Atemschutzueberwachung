package de.thral.atemschutzueberwachung.ui.observation;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import de.thral.atemschutzueberwachung.DraegermanObservationApplication;
import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.business.EventType;
import de.thral.atemschutzueberwachung.business.Squad;
import de.thral.atemschutzueberwachung.persistence.ActiveOperationDAO;
import de.thral.atemschutzueberwachung.persistence.CompleteOperationsDAO;
import de.thral.atemschutzueberwachung.ui.observation.dialog.EndOperationDialog;
import de.thral.atemschutzueberwachung.ui.observation.dialog.RegisterSquadDialog;
import de.thral.atemschutzueberwachung.ui.observation.view.LayoutClickListener;
import de.thral.atemschutzueberwachung.ui.observation.view.OverviewView;

public class MonitoringOverviewActivity extends AppCompatActivity
        implements RegisterSquadDialog.SquadRegisteredListener,
        EndOperationDialog.EndOperationListener,
        LayoutClickListener{

    public static final String KEY_RESUMED = "OperationResumed";

    private OverviewView overviewView;
    private ActiveOperationDAO activeOperationDAO;
    private CompleteOperationsDAO completeOperationsDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        activeOperationDAO = ((DraegermanObservationApplication)getApplication())
                .getActiveOperationDAO();
        completeOperationsDAO = ((DraegermanObservationApplication)getApplication())
                .getCompleteOperationsDAO();

        Intent intent = getIntent();
        if(intent.getBooleanExtra(KEY_RESUMED, false)){
            for(Squad squad : activeOperationDAO.get().getActiveSquads()){
                if(squad != null){
                    if(!squad.getState().equals(EventType.PauseTimer)
                            && !squad.getState().equals(EventType.Register)){
                        squad.resumeAfterError();
                    }
                }
            }
            Toast.makeText(getApplicationContext(),
                    R.string.toastOperationResumed, Toast.LENGTH_LONG ).show();
        }
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
                        if(!activeOperationDAO.get().isSquadActive()){
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
        overviewView.setSquads(activeOperationDAO.get().getActiveSquads());
    }

    public void onLayoutClick(int layoutNumber){
        Intent intent = new Intent(MonitoringOverviewActivity.this,
                MonitoringDetailActivity.class);
        intent.putExtra(MonitoringDetailActivity.DETAIL_KEY, layoutNumber+1);
        startActivity(intent);
    }

    @Override
    public void onSquadRegistered(Squad registeredSquad) {
        activeOperationDAO.get().registerSquad(registeredSquad);
        new UpdateOperationTask().execute();
        initView();
    }

    @Override
    public void onOperationEnd(String observer, String operationType, String location, String unit) {
        if(activeOperationDAO.get().complete(operationType, location, observer, unit)){
            new EndOperationTask().execute();
        }
    }

    private class UpdateOperationTask extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... params) {
            return activeOperationDAO.save();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(!result){
                Toast.makeText(getApplicationContext(),
                        R.string.toastOperationNotSaved, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class EndOperationTask extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean ok = completeOperationsDAO.add(activeOperationDAO.get());
            ok = ok && activeOperationDAO.end();
            return ok;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(!result){
                Toast.makeText(getApplicationContext(),
                        R.string.toastOperationNotSaved, Toast.LENGTH_LONG).show();
            }
            MonitoringOverviewActivity.this.finish();
        }
    }
}

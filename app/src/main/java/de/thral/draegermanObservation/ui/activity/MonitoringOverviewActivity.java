package de.thral.draegermanObservation.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import de.thral.draegermanObservation.DraegermanObservationApplication;
import de.thral.draegermanObservation.R;
import de.thral.draegermanObservation.business.EventType;
import de.thral.draegermanObservation.business.Squad;
import de.thral.draegermanObservation.persistence.CompleteOperationsDAO;
import de.thral.draegermanObservation.ui.dialog.EndOperationDialog;
import de.thral.draegermanObservation.ui.dialog.RegisterSquadDialog;
import de.thral.draegermanObservation.ui.view.LayoutClickListener;
import de.thral.draegermanObservation.ui.view.OverviewView;

public class MonitoringOverviewActivity extends MonitoringBaseActivity
        implements RegisterSquadDialog.SquadRegisteredListener,
        EndOperationDialog.EndOperationListener,
        LayoutClickListener{

    public static final String KEY_RESUMED = "OperationResumed";

    private OverviewView overviewView;
    private CompleteOperationsDAO completeOperationsDAO;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);


        completeOperationsDAO = DraegermanObservationApplication.getCompleteOperationsDAO(this);

        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        Intent intent = getIntent();
        if(intent.getBooleanExtra(KEY_RESUMED, false)){
            resumeAfterError();
        }
        initView();
    }

    private void resumeAfterError(){
        for(Squad squad : activeOperationDAO.get().getActiveSquads()){
            if(squad != null){
                if(!squad.getState().equals(EventType.PauseTimer)
                        && !squad.getState().equals(EventType.Register)){
                    squad.resumeAfterError();
                }
            }
        }
        displayInfo(R.string.toastOperationResumed);
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
        updateOperation();
        initView();
    }

    @Override
    public void onOperationEnd(String observer, String operationType, String location, String unit) {
        if(activeOperationDAO.get().complete(operationType, location, observer, unit)){
            new EndOperationTask().execute();
        }
    }

    private class EndOperationTask extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            overviewView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean ok = completeOperationsDAO.add(activeOperationDAO.get());
            ok = ok && activeOperationDAO.delete();
            return ok;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(!result){
                displayInfo(R.string.toastOperationNotSaved);
            }
            MonitoringOverviewActivity.this.finish();
        }
    }
}

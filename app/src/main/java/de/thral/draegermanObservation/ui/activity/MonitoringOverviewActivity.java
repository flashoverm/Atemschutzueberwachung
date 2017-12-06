package de.thral.draegermanObservation.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
    private RelativeLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        activeOperationDAO = ((DraegermanObservationApplication)getApplication())
                .getActiveOperationDAO();
        completeOperationsDAO = ((DraegermanObservationApplication)getApplication())
                .getCompleteOperationsDAO();

        progressBar = findViewById(R.id.operationProgress);
        progressBar.setVisibility(View.INVISIBLE);

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

    private class EndOperationTask extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected void onPreExecute() {
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
                Toast.makeText(getApplicationContext(),
                        R.string.toastOperationNotSaved, Toast.LENGTH_LONG).show();
            } else {
                System.out.println("SAVED!!!!!");
            }
            MonitoringOverviewActivity.this.finish();
        }
    }
}
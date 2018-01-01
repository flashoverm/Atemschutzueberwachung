package de.thral.draegermanObservation.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import de.thral.draegermanObservation.DraegermanObservationApplication;
import de.thral.draegermanObservation.R;
import de.thral.draegermanObservation.persistence.ActiveOperationDAO;

public abstract class MonitoringBaseActivity extends AppCompatActivity {

    protected ActiveOperationDAO activeOperationDAO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.activeOperationDAO = DraegermanObservationApplication.getActiveOperationDAO(this);
    }

    private class UpdateOperationTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            return activeOperationDAO.update();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(!result){
                displayInfo(R.string.toastOperationNotSaved);
            }
        }
    }

    public void updateOperation(){
        if(activeOperationDAO != null){
            new UpdateOperationTask().execute();
        }
    }

    protected void displayInfo(int resId){
        Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_LONG).show();
    }
}

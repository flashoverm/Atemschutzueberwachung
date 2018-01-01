package de.thral.draegermanObservation.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import de.thral.draegermanObservation.DraegermanObservationApplication;
import de.thral.draegermanObservation.R;
import de.thral.draegermanObservation.persistence.ActiveOperationDAO;
import de.thral.draegermanObservation.persistence.DraegermanDAO;

public class SplashActivity extends AppCompatActivity {

    private ActiveOperationDAO activeOperationDAO;
    private DraegermanDAO draegermanDAO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        this.activeOperationDAO = DraegermanObservationApplication.getActiveOperationDAO(this);
        this.draegermanDAO = DraegermanObservationApplication.getDraegermanDAO(this);

        new LoadDataTask().execute();
    }

    private class LoadDataTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            activeOperationDAO.load();
            draegermanDAO.getAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Intent intent = new Intent(SplashActivity.this, MenuActivity.class);
            SplashActivity.this.startActivity(intent);
            finish();
        }
    }
}

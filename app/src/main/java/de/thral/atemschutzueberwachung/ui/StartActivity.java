package de.thral.atemschutzueberwachung.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import de.thral.atemschutzueberwachung.DraegermanObservationApplication;
import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.persistence.ActiveOperationDAO;

public class StartActivity extends AppCompatActivity {

    private ActiveOperationDAO activeOperationDAO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        this.activeOperationDAO = ((DraegermanObservationApplication)getApplication())
                .getActiveOperationDAO();

        new LoadOperationTask().execute();
    }

    private class LoadOperationTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            activeOperationDAO.load();
            return null;
        }

        @Override
        protected void onPreExecute() {
            Intent intent = new Intent(StartActivity.this, MenuActivity.class);
            StartActivity.this.startActivity(intent);

        }
    }
}

package de.thral.atemschutzueberwachung.ui.observation;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.persistence.ActiveOperationDAO;

public abstract class MonitoringBaseActivity extends AppCompatActivity {

    protected ActiveOperationDAO activeOperationDAO;

    protected class UpdateOperationTask extends AsyncTask<Void, Void, Boolean> {

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

}

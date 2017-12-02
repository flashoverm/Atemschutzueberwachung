package de.thral.atemschutzueberwachung.ui.activity;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import de.thral.atemschutzueberwachung.DraegermanObservationApplication;
import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.persistence.CompleteOperationsDAO;
import de.thral.atemschutzueberwachung.ui.adapter.OperationListViewAdapter;

public class OperationAdminActivity extends AppCompatActivity implements
        MenuItem.OnMenuItemClickListener{

    private CompleteOperationsDAO completeOperationsDAO;
    private ListView completedOperations;
    private TextView noOperations;
    private RelativeLayout progressBar;
    private OperationListViewAdapter adapter;

    private SparseBooleanArray checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_operation);

        Toolbar toolbar = findViewById(R.id.toolbarOperationAdmin);
        setSupportActionBar(toolbar);

        completeOperationsDAO = ((DraegermanObservationApplication)getApplication())
                .getCompleteOperationsDAO();
        completedOperations = findViewById(R.id.operationList);
        noOperations = findViewById(R.id.noOperations);
        progressBar = findViewById(R.id.operationsProgress);
        noOperations.setVisibility(View.INVISIBLE);

        new LoadOperationsTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_operation_admin, menu);
        for(int i=0; i<menu.size(); i++){
            menu.getItem(i).setOnMenuItemClickListener(this);
        }
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        super.onOptionsItemSelected(menuItem);
        checked = adapter.getCheckedItemPositions();

        if(checked.size() == 0){
            Toast.makeText(this, R.string.toastNothingSelected, Toast.LENGTH_LONG).show();
            return true;
        }

        switch (menuItem.getItemId()) {
            case R.id.menuExport: {
                new ExportOperationsTask().execute(checked);
                break;
            }
            case R.id.menuDelete: {
                AlertDialog deleteDialog = new AlertDialog.Builder(this)
                        .setTitle(R.string.deleteWarningTitle)
                        .setMessage(R.string.deleteWarningText)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new DeleteOperationsTask().execute(checked);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        }).create();
                deleteDialog.show();
                break;
            }
        }
        return true;
    }

    private void setVisibility(){
        if(completeOperationsDAO.getAll().size() == 0){
            noOperations.setVisibility(View.VISIBLE);
            completedOperations.setVisibility(View.INVISIBLE);
        } else {
            noOperations.setVisibility(View.INVISIBLE);
            completedOperations.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == 387) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new ExportOperationsTask().execute(checked);
            } else {
                Toast.makeText(this, R.string.toastExportDeactivated, Toast.LENGTH_LONG).show();
            }
            return;
        }
    }

    private class LoadOperationsTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            adapter = new OperationListViewAdapter(
                    OperationAdminActivity.this,
                    R.layout.listitem_operation_completed,
                    completeOperationsDAO.getAll());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.INVISIBLE);
            completedOperations.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            completedOperations.setAdapter(adapter);
            setVisibility();
        }
    }

    private class ExportOperationsTask extends AsyncTask<SparseBooleanArray, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(SparseBooleanArray... params) {
            boolean exportedAll = false;
            if(completeOperationsDAO.setupStorage(OperationAdminActivity.this)){
                exportedAll = true;
                for (int i = completedOperations.getCount()-1; i >=0 ; i--) {
                    if (params[0].get(i)){
                        exportedAll = exportedAll
                                && completeOperationsDAO.export(adapter.getItem(i));
                    }
                }
                params[0].clear();
                return exportedAll;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressBar.setVisibility(View.INVISIBLE);
            if(result != null){
                if(result){
                    Toast.makeText(OperationAdminActivity.this,
                            R.string.toastExportSucceeded, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(OperationAdminActivity.this,
                            R.string.toastExportFailed, Toast.LENGTH_LONG).show();
                }
                adapter.notifyDataSetChanged();
                completedOperations.clearChoices();
            }
        }
    }

    private class DeleteOperationsTask extends AsyncTask<SparseBooleanArray, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(SparseBooleanArray... params) {
            boolean deletedAll = true;
            for (int i = completedOperations.getCount()-1; i >=0 ; i--) {
                if (params[0].get(i)) {
                    deletedAll = deletedAll
                            && completeOperationsDAO.remove(adapter.getItem(i));
                }
            }
            params[0].clear();
            return deletedAll;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressBar.setVisibility(View.INVISIBLE);

            if(!result){
                Toast.makeText(OperationAdminActivity.this,
                        R.string.toastDeletingFailed, Toast.LENGTH_LONG).show();
            }
            adapter.notifyDataSetChanged();
            completedOperations.clearChoices();
            setVisibility();
        }
    }
}

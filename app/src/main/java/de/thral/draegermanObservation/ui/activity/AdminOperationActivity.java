package de.thral.draegermanObservation.ui.activity;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import de.thral.draegermanObservation.DraegermanObservationApplication;
import de.thral.draegermanObservation.R;
import de.thral.draegermanObservation.persistence.CompleteOperationsDAO;
import de.thral.draegermanObservation.ui.adapter.OperationListViewAdapter;

public class AdminOperationActivity extends AdminBaseActivity
        implements MenuItem.OnMenuItemClickListener{

    private CompleteOperationsDAO completeOperationsDAO;
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
        listView = findViewById(R.id.operationList);
        noEntry = findViewById(R.id.noOperations);
        progressBar = findViewById(R.id.progress);

        new LoadOperationsTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_operation_admin, menu);
        setMenuItemClickListener(menu, this);
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
        protected void onPreExecute() {
            showProgress(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            adapter = new OperationListViewAdapter(
                    AdminOperationActivity.this,
                    R.layout.listitem_operation_completed,
                    completeOperationsDAO.getAll());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            showProgress(false);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            listView.setAdapter(adapter);
            setVisibility(completeOperationsDAO.getAll().size() >0);
        }
    }

    private class ExportOperationsTask extends AsyncTask<SparseBooleanArray, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            showProgress(true);
        }

        @Override
        protected Boolean doInBackground(SparseBooleanArray... params) {
            boolean exportedAll = false;
            if(completeOperationsDAO.setupStorage(AdminOperationActivity.this)){
                exportedAll = true;
                for (int i = listView.getCount()-1; i >=0 ; i--) {
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
            showProgress(false);
            if(result != null){
                if(result){
                    Toast.makeText(AdminOperationActivity.this,
                            R.string.toastExportSucceeded, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AdminOperationActivity.this,
                            R.string.toastExportFailed, Toast.LENGTH_LONG).show();
                }
                adapter.notifyDataSetChanged();
                listView.clearChoices();
                setVisibility(completeOperationsDAO.getAll().size() >0);
            }
        }
    }

    private class DeleteOperationsTask extends AsyncTask<SparseBooleanArray, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            showProgress(true);
        }

        @Override
        protected Boolean doInBackground(SparseBooleanArray... params) {
            boolean deletedAll = true;
            for (int i = listView.getCount()-1; i >=0 ; i--) {
                if (params[0].get(i)) {
                    deletedAll = deletedAll
                            && completeOperationsDAO.delete(adapter.getItem(i));
                }
            }
            params[0].clear();
            return deletedAll;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            showProgress(false);
            if(!result){
                Toast.makeText(AdminOperationActivity.this,
                        R.string.toastDeletingFailed, Toast.LENGTH_LONG).show();
            }
            adapter.notifyDataSetChanged();
            listView.clearChoices();
            setVisibility(completeOperationsDAO.getAll().size() >0);
        }
    }
}

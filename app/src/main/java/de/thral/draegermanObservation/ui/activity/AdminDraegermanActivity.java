package de.thral.draegermanObservation.ui.activity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import de.thral.draegermanObservation.DraegermanObservationApplication;
import de.thral.draegermanObservation.R;
import de.thral.draegermanObservation.business.Draegerman;
import de.thral.draegermanObservation.persistence.DraegermanDAO;
import de.thral.draegermanObservation.ui.dialog.AddDraegermanDialog;

public class AdminDraegermanActivity extends AdminBaseActivity
        implements AddDraegermanDialog.AddDraegermanListener, MenuItem.OnMenuItemClickListener{

    private DraegermanDAO draegermanDAO;
    private ArrayAdapter<Draegerman> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_draegerman);

        Toolbar toolbar = findViewById(R.id.toolbarDraegermanAdmin);
        setSupportActionBar(toolbar);

        draegermanDAO = ((DraegermanObservationApplication)getApplication()).getDraegermanDAO();
        listView = findViewById(R.id.draegermanList);
        noEntry = findViewById(R.id.noDraegerman);
        progressBar = findViewById(R.id.progress);

        new LoadDraegermanTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_draegerman_admin, menu);
        setMenuItemClickListener(menu, this);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAddDraegerman: {
                AddDraegermanDialog dialog = AddDraegermanDialog.newInstance();
                dialog.show(getFragmentManager(), "AddDraegermanDialog");
                break;
            }
            case R.id.menuDelete: {
                final SparseBooleanArray checked = listView.getCheckedItemPositions();
                if(checked.size() == 0){
                    Toast.makeText(this, R.string.toastNothingSelected, Toast.LENGTH_LONG).show();
                    return true;
                }
                AlertDialog deleteDialog = new AlertDialog.Builder(this)
                        .setTitle(R.string.deleteWarningTitle)
                        .setMessage(R.string.deleteWarningText)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new DeleteDraegermanTask().execute(checked);
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
    public void onAddDraegerman(Draegerman draegerman){
        new AddDraegermanTask().execute(draegerman);
    }

    private class LoadDraegermanTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            showProgress(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            adapter = new ArrayAdapter<>(AdminDraegermanActivity.this,
                    android.R.layout.simple_list_item_multiple_choice,
                    draegermanDAO.getAll());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            showProgress(false);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            listView.setAdapter(adapter);
            setVisibility(draegermanDAO.getAll().size() >0);
        }
    }

    private class AddDraegermanTask extends AsyncTask<Draegerman, Void, Boolean>{
        @Override
        protected void onPreExecute() {
            showProgress(true);
        }

        @Override
        protected Boolean doInBackground(Draegerman... params) {
            return draegermanDAO.add(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            showProgress(false);
            if(!result){
                Toast.makeText(AdminDraegermanActivity.this,
                        R.string.toastDraegermanAddError, Toast.LENGTH_LONG).show();
            }
            adapter.notifyDataSetChanged();
            setVisibility(draegermanDAO.getAll().size() >0);
        }
    }

    private class DeleteDraegermanTask extends AsyncTask<SparseBooleanArray, Void, Boolean>{

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
                            && draegermanDAO.remove(adapter.getItem(i));
                }
            }
            params[0].clear();
            return deletedAll;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            showProgress(false);
            if(!result){
                Toast.makeText(AdminDraegermanActivity.this,
                        R.string.toastDeletingFailed, Toast.LENGTH_LONG).show();
            }
            adapter.notifyDataSetChanged();
            listView.clearChoices();
            setVisibility(draegermanDAO.getAll().size() >0);
        }
    }

}

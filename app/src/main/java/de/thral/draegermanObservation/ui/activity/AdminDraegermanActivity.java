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
        progressBar = findViewById(R.id.draegermanProgress);

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
                AddDraegermanDialog dialog = new AddDraegermanDialog();
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
    public boolean onAddDraegerman(Draegerman draegerman){
        if(draegermanDAO.add(draegerman, this)){
            adapter.notifyDataSetChanged();
            setVisibility(draegermanDAO.getAll().size());
            return true;
        }
        return false;
    }

    private class LoadDraegermanTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            showProgressBar();
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
            hideProgressBar();
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            listView.setAdapter(adapter);
            setVisibility(draegermanDAO.getAll().size());
        }
    }

    private class DeleteDraegermanTask extends AsyncTask<SparseBooleanArray, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            showProgressBar();
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
            hideProgressBar();

            if(!result){
                Toast.makeText(AdminDraegermanActivity.this,
                        R.string.toastDeletingFailed, Toast.LENGTH_LONG).show();
            }
            adapter.notifyDataSetChanged();
            listView.clearChoices();
            setVisibility(draegermanDAO.getAll().size());
        }
    }

}
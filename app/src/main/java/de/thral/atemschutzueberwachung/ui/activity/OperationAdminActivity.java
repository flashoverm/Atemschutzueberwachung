package de.thral.atemschutzueberwachung.ui.activity;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import de.thral.atemschutzueberwachung.DraegermanObservationApplication;
import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.business.Operation;
import de.thral.atemschutzueberwachung.persistence.OperationDAO;
import de.thral.atemschutzueberwachung.ui.adapter.OperationListViewAdapter;

public class OperationAdminActivity extends AppCompatActivity implements
        MenuItem.OnMenuItemClickListener{

    private OperationDAO operationDAO;
    private ListView completedOperations;
    private OperationListViewAdapter adapter;

    private SparseBooleanArray checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_operation);

        Toolbar toolbar = findViewById(R.id.toolbarOperationAdmin);
        toolbar.setTitle(R.string.operationAdminLabel);
        toolbar.setTitleTextColor(Color.LTGRAY);
        setSupportActionBar(toolbar);

        operationDAO = ((DraegermanObservationApplication)getApplication()).getOperationDAO();
        completedOperations = findViewById(R.id.operationList);

        adapter = new OperationListViewAdapter(this, R.layout.listitem_operation,
                operationDAO.getCompletedOperations());
        completedOperations.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        completedOperations.setAdapter(adapter);
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
                exportSelectedOperations(checked);
                break;
            }
            case R.id.menuDelete: {
                AlertDialog deleteDialog = new AlertDialog.Builder(this)
                        .setTitle(R.string.deleteWarningTitle)
                        .setMessage(R.string.deleteWarningText)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteSelectedOperations(checked);
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

    private void exportSelectedOperations(SparseBooleanArray selected){
        if(operationDAO.setupStorage(this)){
            for (int i = 0; i < selected.size(); i++) {
                int position = selected.keyAt(i);
                if (selected.valueAt(i)){
                    operationDAO.exportOperation(adapter.getItem(position));
                    adapter.notifyDataSetChanged();
                }
            }
            Toast.makeText(this, R.string.toastExportSucceeded, Toast.LENGTH_LONG).show();
        }
    }

    private void deleteSelectedOperations(SparseBooleanArray selected){
        for (int i = 0; i < selected.size(); i++) {
            int position = selected.keyAt(i);
            if (selected.valueAt(i)){
                operationDAO.removeCompletedOperation(adapter.getItem(position));
                adapter.clear();
                adapter.addAll(operationDAO.getCompletedOperations());
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == 387) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportSelectedOperations(checked);
            } else {
                Toast.makeText(this, R.string.toastExportDeactivated, Toast.LENGTH_LONG).show();
            }
            return;
        }
    }
}

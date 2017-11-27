package de.thral.atemschutzueberwachung.ui.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import de.thral.atemschutzueberwachung.DraegermanObservationApplication;
import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.business.Draegerman;
import de.thral.atemschutzueberwachung.persistence.DraegermanDAO;
import de.thral.atemschutzueberwachung.ui.dialog.AddDraegermanDialog;

public class DraegermanAdminActivity extends AppCompatActivity implements
        AddDraegermanDialog.AddDraegermanListener, MenuItem.OnMenuItemClickListener{

    private DraegermanDAO draegermanDAO;
    private ListView draegermen;
    private TextView noDraegerman;
    private ArrayAdapter<Draegerman> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_draegerman);

        Toolbar toolbar = findViewById(R.id.toolbarDraegermanAdmin);
        setSupportActionBar(toolbar);

        draegermanDAO = ((DraegermanObservationApplication)getApplication()).getDraegermanDAO();
        draegermen = findViewById(R.id.draegermanList);
        noDraegerman = findViewById(R.id.noDraegerman);
        setVisibility();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice,
                draegermanDAO.getAll());
        draegermen.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        draegermen.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_draegerman_admin, menu);
        for(int i=0; i<menu.size(); i++){
            menu.getItem(i).setOnMenuItemClickListener(this);
        }
        return true;    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAddDraegerman: {
                AddDraegermanDialog dialog = new AddDraegermanDialog();
                dialog.show(getFragmentManager(), "AddDraegermanDialog");
                break;
            }
            case R.id.menuDelete: {
                final SparseBooleanArray checked = draegermen.getCheckedItemPositions();
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
                                deleteSelectedDraegermen(checked);
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

    private void deleteSelectedDraegermen(SparseBooleanArray selected){
        for (int i = draegermen.getCount()-1; i >=0 ; i--) {
            if (selected.get(i)) {
                draegermanDAO.remove(adapter.getItem(i));
            }
        }
        selected.clear();
        adapter.notifyDataSetChanged();
        draegermen.clearChoices();
        setVisibility();
    }

    @Override
    public boolean onAddDraegerman(String firstname, String lastname) {
        if(draegermanDAO.add(new Draegerman(firstname, lastname))){
            adapter.notifyDataSetChanged();
            setVisibility();
            return true;
        }
        return false;
    }

    private void setVisibility(){
        if(draegermanDAO.getAll().size() == 0){
            noDraegerman.setVisibility(View.VISIBLE);
            draegermen.setVisibility(View.INVISIBLE);
        } else {
            noDraegerman.setVisibility(View.INVISIBLE);
            draegermen.setVisibility(View.VISIBLE);
        }
    }
}

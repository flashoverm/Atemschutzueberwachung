package de.thral.draegermanObservation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import de.thral.draegermanObservation.DraegermanObservationApplication;
import de.thral.draegermanObservation.R;

public class MenuActivity extends MonitoringBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        this.activeOperationDAO = DraegermanObservationApplication.getActiveOperationDAO(this);

        Button startOperation = findViewById(R.id.startOperation);
        Button management = findViewById(R.id.administration);

        startOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, MonitoringOverviewActivity.class);
                if(activeOperationDAO.get() == null){
                    activeOperationDAO.add();
                    updateOperation();
                }
                startActivity(intent);
            }
        });
        management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, AdminMenuActivity.class);
                startActivity(intent);
            }
        });

        if(activeOperationDAO.get() != null){
            Intent intent = new Intent(MenuActivity.this, MonitoringOverviewActivity.class);
            intent.putExtra(MonitoringOverviewActivity.KEY_RESUMED, true);
            startActivity(intent);
        }
    }
}

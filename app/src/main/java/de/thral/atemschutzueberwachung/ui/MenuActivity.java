package de.thral.atemschutzueberwachung.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import de.thral.atemschutzueberwachung.DraegermanObservationApplication;
import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.persistence.ActiveOperationDAO;
import de.thral.atemschutzueberwachung.ui.administration.activity.AdminMenuActivity;
import de.thral.atemschutzueberwachung.ui.observation.MonitoringOverviewActivity;

public class MenuActivity extends AppCompatActivity {

    private ActiveOperationDAO activeOperationDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        this.activeOperationDAO = ((DraegermanObservationApplication)getApplication())
                .getActiveOperationDAO();

        Button startOperation = findViewById(R.id.startOperation);
        Button management = findViewById(R.id.administration);

        startOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, MonitoringOverviewActivity.class);
                if(activeOperationDAO.get() == null){
                    ((DraegermanObservationApplication)getApplication())
                            .getActiveOperationDAO().create();
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

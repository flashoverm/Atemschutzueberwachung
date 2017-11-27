package de.thral.atemschutzueberwachung.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import de.thral.atemschutzueberwachung.DraegermanObservationApplication;
import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.persistence.OperationDAO;

public class MenuActivity extends AppCompatActivity {

    private OperationDAO operationDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        this.operationDAO = ((DraegermanObservationApplication)getApplication()).getOperationDAO();

        Button startOperation = findViewById(R.id.startOperation);
        Button management = findViewById(R.id.administration);

        startOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, MonitoringOverviewActivity.class);
                if(operationDAO.getActive() == null){
                    ((DraegermanObservationApplication)getApplication())
                            .getOperationDAO().createOperation();
                }
                startActivity(intent);
            }
        });
        management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, AdministrationActivity.class);
                startActivity(intent);
            }
        });

        if(operationDAO.getActive() != null){
            Intent intent = new Intent(MenuActivity.this, MonitoringOverviewActivity.class);
            intent.putExtra(MonitoringOverviewActivity.KEY_RESUMED, true);
            startActivity(intent);
        }
    }
}

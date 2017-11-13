package de.thral.atemschutzueberwachung.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import de.thral.atemschutzueberwachung.DraegermanObservationApplication;
import de.thral.atemschutzueberwachung.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button startOperation = (Button) findViewById(R.id.startOperation);
        Button management = (Button) findViewById(R.id.administration);

        if(((DraegermanObservationApplication)getApplication())
                .getOperationDAO().getActive() != null){
            Intent intent = new Intent(MenuActivity.this, MonitoringOverviewActivity.class);
            intent.putExtra(MonitoringOverviewActivity.KEY_RESUMED, true);
            startActivity(intent);
        }

        startOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DraegermanObservationApplication)getApplication())
                        .getOperationDAO().createOperation();
                Intent intent = new Intent(MenuActivity.this, MonitoringOverviewActivity.class);
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

    }
}

package de.thral.atemschutzueberwachung.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import de.thral.atemschutzueberwachung.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button startOperation = (Button) findViewById(R.id.startOperation);
        Button management = (Button) findViewById(R.id.administration);

        startOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //switch to Detail
                Intent intent = new Intent(MenuActivity.this, MonitoringOverviewActivity.class);
                startActivity(intent);
            }
        });

        management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO switch to administration menu
            }
        });

    }
}

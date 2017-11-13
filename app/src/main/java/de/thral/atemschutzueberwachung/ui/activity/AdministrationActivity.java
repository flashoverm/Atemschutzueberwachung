package de.thral.atemschutzueberwachung.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import de.thral.atemschutzueberwachung.DraegermanObservationApplication;
import de.thral.atemschutzueberwachung.R;

public class AdministrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administration);

        Button operationAdministrator = (Button) findViewById(R.id.operationAdministrationButton);
        Button draegermanAdministrator = (Button) findViewById(R.id.draegermanAdministrationButton);

        operationAdministrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdministrationActivity.this, OperationAdminActivity.class);
                startActivity(intent);
            }
        });

        draegermanAdministrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdministrationActivity.this, DraegermanAdminActivity.class);
                startActivity(intent);
            }
        });

    }
}

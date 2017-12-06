package de.thral.draegermanObservation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import de.thral.draegermanObservation.R;

public class AdminMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administration);

        Button operationAdministrator = findViewById(R.id.operationAdministrationButton);
        Button draegermanAdministrator = findViewById(R.id.draegermanAdministrationButton);
        Button settings = findViewById(R.id.settingsButton);

        operationAdministrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMenuActivity.this, AdminOperationActivity.class);
                startActivity(intent);
            }
        });

        draegermanAdministrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMenuActivity.this, AdminDraegermanActivity.class);
                startActivity(intent);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMenuActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}

package de.thral.draegermanObservation.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import de.thral.draegermanObservation.DraegermanObservationApplication;
import de.thral.draegermanObservation.R;
import de.thral.draegermanObservation.notification.NotificationManager;

public class SettingsActivity extends AppCompatActivity {

    private NotificationManager notificationManager;

    private CheckBox enableFlash;
    private CheckBox enableVibration;
    private CheckBox enableSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);

        notificationManager = DraegermanObservationApplication.getNotificationManager(this);

        enableFlash = findViewById(R.id.enableFlash);
        enableVibration = findViewById(R.id.enableVibration);
        enableSound = findViewById(R.id.enableSound);

        boolean[] availability = notificationManager.getAvailability();
        enableFlash.setEnabled(availability[0]);
        enableVibration.setEnabled(availability[1]);
        enableSound.setEnabled(availability[2]);

        boolean[] preferences = notificationManager.getSettings();
        enableFlash.setChecked(preferences[0]);
        enableVibration.setChecked(preferences[1]);
        enableSound.setChecked(preferences[2]);

        enableFlash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                notificationManager.setSetting(NotificationManager.Settings.flash, b);
            }
        });
        enableVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                notificationManager.setSetting(NotificationManager.Settings.vibration, b);
            }
        });
        enableSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                notificationManager.setSetting(NotificationManager.Settings.sound, b);
            }
        });
    }
}

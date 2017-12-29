package de.thral.draegermanObservation.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import de.thral.draegermanObservation.DraegermanObservationApplication;
import de.thral.draegermanObservation.R;
import de.thral.draegermanObservation.deviceNotification.DeviceNotificationInterface;

public class SettingsActivity extends AppCompatActivity {

    private DeviceNotificationInterface deviceNotificationInterface;

    private CheckBox enableFlash;
    private CheckBox enableVibration;
    private CheckBox enableSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);

        deviceNotificationInterface = ((DraegermanObservationApplication)getApplication())
                .getDeviceNotificationInterface();

        enableFlash = findViewById(R.id.enableFlash);
        enableVibration = findViewById(R.id.enableVibration);
        enableSound = findViewById(R.id.enableSound);

        boolean[] availability = deviceNotificationInterface.getAvailability();
        enableFlash.setEnabled(availability[0]);
        enableVibration.setEnabled(availability[1]);
        enableSound.setEnabled(availability[2]);

        boolean[] preferences = deviceNotificationInterface.getSettings();
        enableFlash.setChecked(preferences[0]);
        enableVibration.setChecked(preferences[1]);
        enableSound.setChecked(preferences[2]);

        enableFlash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                deviceNotificationInterface.setSetting(DeviceNotificationInterface.Settings.flash, b);
            }
        });
        enableVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                deviceNotificationInterface.setSetting(DeviceNotificationInterface.Settings.vibration, b);
            }
        });
        enableSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                deviceNotificationInterface.setSetting(DeviceNotificationInterface.Settings.sound, b);
            }
        });
    }
}

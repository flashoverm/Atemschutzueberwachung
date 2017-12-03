package de.thral.atemschutzueberwachung.ui.administration;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import de.thral.atemschutzueberwachung.DraegermanObservationApplication;
import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.hardware.HardwareInterface;

public class SettingsActivity extends AppCompatActivity {

    private HardwareInterface hardwareInterface;

    private CheckBox enableFlash;
    private CheckBox enableVibration;
    private CheckBox enableSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);

        hardwareInterface = ((DraegermanObservationApplication)getApplication())
                .getHardwareInterface();

        enableFlash = findViewById(R.id.enableFlash);
        enableVibration = findViewById(R.id.enableVibration);
        enableSound = findViewById(R.id.enableSound);

        boolean[] availability = hardwareInterface.getAvailability();
        enableFlash.setEnabled(availability[0]);
        enableVibration.setEnabled(availability[1]);
        enableSound.setEnabled(availability[2]);

        boolean[] preferences = hardwareInterface.getSettings();
        enableFlash.setChecked(preferences[0]);
        enableVibration.setChecked(preferences[1]);
        enableSound.setChecked(preferences[2]);

        enableFlash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                hardwareInterface.setSetting(HardwareInterface.Settings.flash, b);
            }
        });
        enableVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                hardwareInterface.setSetting(HardwareInterface.Settings.vibration, b);
            }
        });
        enableSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                hardwareInterface.setSetting(HardwareInterface.Settings.sound, b);
            }
        });
    }
}

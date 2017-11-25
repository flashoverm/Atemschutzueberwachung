package de.thral.atemschutzueberwachung.ui.activity;

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

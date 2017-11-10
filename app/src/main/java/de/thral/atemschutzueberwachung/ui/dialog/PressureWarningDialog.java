package de.thral.atemschutzueberwachung.ui.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import de.thral.atemschutzueberwachung.R;

/**
 * Created by Markus Thral on 30.10.2017.
 */

public class PressureWarningDialog extends DialogFragment {

    public static final String UNTERSHOT_KEY = "undershot";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.pressureWarningTitle)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        if(getArguments().getBoolean(UNTERSHOT_KEY)){
            builder.setMessage(R.string.returnPressureAlarmText);
        } else {
            builder.setMessage(R.string.returnPressureWarningText);
        }
        return builder.create();
    }

    public static PressureWarningDialog newInstance(boolean underShot) {
        Bundle args = new Bundle();
        args.putBoolean(UNTERSHOT_KEY, underShot);

        PressureWarningDialog fragment = new PressureWarningDialog();
        fragment.setArguments(args);
        return fragment;
    }
}

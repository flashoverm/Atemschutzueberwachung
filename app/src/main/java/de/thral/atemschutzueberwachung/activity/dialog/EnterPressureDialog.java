package de.thral.atemschutzueberwachung.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.domain.EventType;

/**
 * Created by Markus Thral on 29.10.2017.
 */

public class EnterPressureDialog extends DialogFragment {

    public interface EnteredPressureListener{
        public void onEnteredPressure(EventType event, int leaderPressure, int memberPressure);
    }

    public static final String EVENT_KEY = "event";

    private Spinner spinner;
    private EditText leaderPressure;
    private EditText memberPressure;
    private EnteredPressureListener listener;

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_enter_pressure, null);

        leaderPressure = (EditText) dialogView.findViewById(R.id.edit_leaderpressure);
        memberPressure = (EditText) dialogView.findViewById(R.id.edit_memberpressure);
        spinner = (Spinner) dialogView.findViewById(R.id.edit_event);

        ArrayAdapter<String> adapter = EventType.getArrayAdapter(getActivity());
        spinner.setAdapter(adapter);
        spinner.setSelection(
                adapter.getPosition(getArguments().getString(EVENT_KEY))
        );

        builder.setView(dialogView)
                .setTitle(getResources().getString(R.string.enterPressureTitle))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        int leader = Integer.parseInt(leaderPressure.getText().toString());
                        int member = Integer.parseInt(memberPressure.getText().toString());
                        String event = spinner.getSelectedItem().toString();
                        listener.onEnteredPressure(
                                EventType.getEventType(getActivity(), event), leader, member);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EnterPressureDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (EnteredPressureListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement EnteredPressureListener");
        }
    }

    public static EnterPressureDialog newInstance(Context context, EventType event) {

        Bundle args = new Bundle();
        args.putString(EVENT_KEY, event.toString(context));

        EnterPressureDialog fragment = new EnterPressureDialog();
        fragment.setArguments(args);
        return fragment;
    }
}

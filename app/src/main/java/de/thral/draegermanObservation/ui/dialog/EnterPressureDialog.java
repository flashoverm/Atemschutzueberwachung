package de.thral.draegermanObservation.ui.dialog;

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
import android.widget.Toast;

import de.thral.draegermanObservation.R;
import de.thral.draegermanObservation.business.EventType;

public class EnterPressureDialog extends DialogFragment {

    public interface EnteredPressureListener{
        boolean onEnteredPressure(EventType event, int leaderPressure, int memberPressure);
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

        leaderPressure = dialogView.findViewById(R.id.edit_leaderpressure);
        memberPressure = dialogView.findViewById(R.id.edit_memberpressure);
        spinner = dialogView.findViewById(R.id.edit_event);

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
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EnterPressureDialog.this.getDialog().cancel();
                    }
                });
        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                final AlertDialog enterPressure = (AlertDialog) dialogInterface;
                enterPressure.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(attempEnterPressure()){
                                    enterPressure.dismiss();
                                }
                            }
                        });
            }
        });
        return dialog;
    }

    private boolean attempEnterPressure(){
        String leader = leaderPressure.getText().toString();
        String member = memberPressure.getText().toString();

        leaderPressure.setError(null);
        memberPressure.setError(null);

        EventType event = EventType.getEventType(
                getActivity(), spinner.getSelectedItem().toString());

        if(leader.equals("")){
            leaderPressure.setError(getString(R.string.toastNoPressureLeader));
            return false;
        }
        if(member.equals("")){
            memberPressure.setError(getString(R.string.toastNoPressureMember));
            return false;
        }
        int leaderPressureValue;
        int memberPressureValue;
        try{
            leaderPressureValue = Integer.parseInt(leader);
        } catch(NumberFormatException e){
            leaderPressure.setError(getString(R.string.errorInvalidPressure));
            return false;
        }
        try{
            memberPressureValue = Integer.parseInt(member);
        } catch(NumberFormatException e){
            memberPressure.setError(getString(R.string.errorInvalidPressure));
            return false;
        }

        if(leaderPressureValue < 0 ){
            leaderPressure.setError(getString(R.string.toastPressureUnderZero));
            return false;
        }
        if(memberPressureValue < 0){
            memberPressure.setError(getString(R.string.toastPressureUnderZero));
            return false;
        }
        if(!listener.onEnteredPressure(event, leaderPressureValue, memberPressureValue)){
            Toast.makeText(getActivity(), R.string.toastHigherPressure, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
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

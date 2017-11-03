package de.thral.atemschutzueberwachung.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.domain.Draegerman;
import de.thral.atemschutzueberwachung.domain.OperatingTime;
import de.thral.atemschutzueberwachung.domain.Order;
import de.thral.atemschutzueberwachung.domain.Squad;

public class EndOperationDialog extends DialogFragment {

    public interface EndOperationListener {
        public void onOperationEnd(String observer, String operation, String location, String unit);
    }

    private EndOperationListener listener;

    private EditText observerEdit;
    private EditText operationEdit;
    private EditText locationEdit;
    private EditText unitEdit;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_end_operation, null);

        observerEdit = (EditText) view.findViewById(R.id.edit_observer);
        operationEdit = (EditText) view.findViewById(R.id.edit_operation);
        locationEdit = (EditText) view.findViewById(R.id.edit_location);
        unitEdit = (EditText) view.findViewById(R.id.edit_unit);

        builder.setView(view)
                .setTitle(getResources().getString(R.string.registerSquadTitle))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EndOperationDialog.this.getDialog().cancel();
                    }
                });

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                final AlertDialog endOperation = (AlertDialog) dialogInterface;
                endOperation.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String observer = observerEdit.getText().toString();
                                String operation = operationEdit.getText().toString();
                                String location = locationEdit.getText().toString();
                                String unit = unitEdit.getText().toString();


                                if(observer.equals("")){
                                    Toast.makeText(getActivity(), R.string.toastNoObserver,
                                            Toast.LENGTH_LONG).show();
                                    return;
                                }
                                if(operation.equals("")){
                                    Toast.makeText(getActivity(), R.string.toastNoOperation,
                                            Toast.LENGTH_LONG).show();
                                    return;
                                }
                                if(location.equals("")){
                                    Toast.makeText(getActivity(), R.string.toastNoLocation,
                                            Toast.LENGTH_LONG).show();
                                    return;
                                }
                                listener.onOperationEnd(observer, operation, location, unit);
                                endOperation.dismiss();
                            }
                        });
            }
        });
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (EndOperationDialog.EndOperationListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement EnteredPressureListener");
        }
    }

    public static EndOperationDialog newInstance() {
        EndOperationDialog fragment = new EndOperationDialog();
        return fragment;
    }
}

package de.thral.draegermanObservation.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import de.thral.draegermanObservation.R;

public class EndOperationDialog extends DialogFragment {

    public interface EndOperationListener {
        void onOperationEnd(String observer, String operation, String location, String unit);
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

        observerEdit = view.findViewById(R.id.edit_observer);
        operationEdit = view.findViewById(R.id.edit_operation);
        locationEdit = view.findViewById(R.id.edit_location);
        unitEdit = view.findViewById(R.id.edit_unit);

        builder.setView(view)
                .setTitle(getResources().getString(R.string.endOperationTitle))
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
                                if(attempEndOperation()){
                                    endOperation.dismiss();
                                }
                            }
                        });
            }
        });
        return dialog;
    }

    private boolean attempEndOperation(){
        String observer = observerEdit.getText().toString();
        String operation = operationEdit.getText().toString();
        String location = locationEdit.getText().toString();
        String unit = unitEdit.getText().toString();

        observerEdit.setError(null);
        operationEdit.setError(null);
        locationEdit.setError(null);

        if(observer.equals("")){
            observerEdit.setError(getString(R.string.errorNoObserver));
            return false;
        }
        if(operation.equals("")){
            operationEdit.setError(getString(R.string.errorNoOperation));
            return false;
        }
        if(location.equals("")){
            locationEdit.setError(getString(R.string.errorNoLocation));
            return false;
        }
        listener.onOperationEnd(observer, operation, location, unit);
        return true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (EndOperationDialog.EndOperationListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement EndOperationListener");
        }
    }

    public static EndOperationDialog newInstance() {
        EndOperationDialog fragment = new EndOperationDialog();
        return fragment;
    }
}

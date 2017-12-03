package de.thral.atemschutzueberwachung.ui.administration;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import de.thral.atemschutzueberwachung.DraegermanObservationApplication;
import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.business.Draegerman;
import de.thral.atemschutzueberwachung.persistence.DraegermanDAO;

public class AddDraegermanDialog extends DialogFragment {

    public interface AddDraegermanListener{
        boolean onAddDraegerman(Draegerman draegerman) ;
    }

    private DraegermanDAO draegermanDAO;
    private EditText firstname;
    private EditText lastname;
    private AddDraegermanListener listener;

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
        View dialogView = inflater.inflate(R.layout.dialog_add_draegerman, null);

        draegermanDAO = ((DraegermanObservationApplication)getActivity().getApplication())
                .getDraegermanDAO();

        firstname = dialogView.findViewById(R.id.edit_firstname);
        lastname = dialogView.findViewById(R.id.edit_lastname);

        builder.setView(dialogView)
                .setTitle(getResources().getString(R.string.enterPressureTitle))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddDraegermanDialog.this.getDialog().cancel();
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
                                String firstnameString = firstname.getText().toString();
                                String lastnameString = lastname.getText().toString();

                                if(firstnameString.equals("")){
                                    Toast.makeText(getActivity(), R.string.toastNoFirstname,
                                            Toast.LENGTH_LONG).show();
                                    return;
                                }
                                if(lastnameString.equals("")){
                                    Toast.makeText(getActivity(), R.string.toastNoLastname,
                                            Toast.LENGTH_LONG).show();
                                    return;
                                }
                                Draegerman add = draegermanDAO.prepareAdd(
                                        firstnameString, lastnameString);
                                if(add == null) {
                                    String toast = firstnameString + " " + lastnameString + " "
                                            + getString(R.string.toastDraegermanAlreadyExisiting);
                                    Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
                                    return;
                                }
                                if (!listener.onAddDraegerman(add)) {
                                    Toast.makeText(getActivity(), R.string.toastDraegermanAddError,
                                            Toast.LENGTH_LONG).show();
                                    return;
                                }
                                enterPressure.dismiss();
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
            listener = (AddDraegermanListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AddDraegermanListener");
        }
    }
}

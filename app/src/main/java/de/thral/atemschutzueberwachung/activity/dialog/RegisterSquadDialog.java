package de.thral.atemschutzueberwachung.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.activity.adapter.DraegermanSpinnerAdapter;
import de.thral.atemschutzueberwachung.domain.Draegerman;
import de.thral.atemschutzueberwachung.domain.OperatingTime;
import de.thral.atemschutzueberwachung.domain.Order;
import de.thral.atemschutzueberwachung.domain.Squad;
import de.thral.atemschutzueberwachung.persistence.DraegermanDAOImplDummy;

/**
 * Created by Markus Thral on 31.10.2017.
 */

public class RegisterSquadDialog extends DialogFragment {

    public interface SquadRegisteredListener {
        public void onSquadRegistered(Squad registeredSquad);
    }

    private SquadRegisteredListener listener;

    private EditText squadName;
    private Spinner operatingTimeSpinner;
    private Spinner leaderSpinner;
    private EditText leaderPressure;
    private Spinner memberSpinner;
    private EditText memberPressure;
    private Spinner orderSpinner;

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_register_squad, null);

        squadName = (EditText) view.findViewById(R.id.edit_squadname);
        operatingTimeSpinner = (Spinner) view.findViewById(R.id.edit_operatingtime);
        leaderSpinner = (Spinner) view.findViewById(R.id.edit_leader);
        leaderPressure = (EditText) view.findViewById(R.id.edit_leaderpressure);
        memberSpinner = (Spinner) view.findViewById(R.id.edit_member);
        memberPressure = (EditText) view.findViewById(R.id.edit_memberpressure);
        orderSpinner = (Spinner) view.findViewById(R.id.edit_order);

        ArrayAdapter<String> operatingTimeAdapter = OperatingTime.getArrayAdapter(getActivity());
        operatingTimeSpinner.setAdapter(operatingTimeAdapter);
        operatingTimeSpinner.setSelection(
                operatingTimeAdapter.getPosition(getString(R.string.operationTimeNormal)));

        ArrayAdapter<String> orderAdapter = Order.getArrayAdapter(getActivity());
        orderSpinner.setAdapter(orderAdapter);

        DraegermanSpinnerAdapter draegermanSpinnerAdapter = new DraegermanSpinnerAdapter(getActivity(),
                R.layout.listitem_spinner, new DraegermanDAOImplDummy().getAll());
        leaderSpinner.setAdapter(draegermanSpinnerAdapter);
        memberSpinner.setAdapter(draegermanSpinnerAdapter);

        builder.setView(view)
                .setTitle(getResources().getString(R.string.registerSquadTitle))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String squadnameText = squadName.getText().toString();
                        Draegerman leader = (Draegerman)leaderSpinner.getSelectedItem();
                        int initLeaderPressure = Integer.parseInt(leaderPressure.getText().toString());
                        Draegerman member = (Draegerman)memberSpinner.getSelectedItem();
                        int initMemberPressure = Integer.parseInt(memberPressure.getText().toString());
                        String operatingTime = operatingTimeSpinner.getSelectedItem().toString();
                        String order = orderSpinner.getSelectedItem().toString();

                        Squad squad = new Squad(
                                squadnameText,
                                leader, initLeaderPressure,
                                member, initMemberPressure,
                                OperatingTime.getOperatingTime(getActivity(), operatingTime),
                                Order.getOrder(getActivity(), order)
                        );
                        listener.onSquadRegistered(squad);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RegisterSquadDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (RegisterSquadDialog.SquadRegisteredListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement EnteredPressureListener");
        }
    }

    public static RegisterSquadDialog newInstance() {
        RegisterSquadDialog fragment = new RegisterSquadDialog();
        return fragment;
    }
}
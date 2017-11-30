package de.thral.atemschutzueberwachung.ui.dialog;

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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import de.thral.atemschutzueberwachung.DraegermanObservationApplication;
import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.ui.adapter.DraegermanSpinnerAdapter;
import de.thral.atemschutzueberwachung.business.Draegerman;
import de.thral.atemschutzueberwachung.business.OperatingTime;
import de.thral.atemschutzueberwachung.business.Order;
import de.thral.atemschutzueberwachung.business.Squad;

public class RegisterSquadDialog extends DialogFragment {

    public interface SquadRegisteredListener {
        void onSquadRegistered(Squad registeredSquad);
    }

    private SquadRegisteredListener listener;

    private EditText squadName;
    private Spinner operatingTimeSpinner;

    private EditText leaderEdit;
    private Spinner leaderSpinner;
    private ImageButton leaderEditButton;
    private EditText leaderPressure;
    private boolean leaderEditEnabled;

    private EditText memberEdit;
    private Spinner memberSpinner;
    private ImageButton memberEditButton;
    private boolean memberEditEnabled;

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

        squadName = view.findViewById(R.id.edit_squadname);
        operatingTimeSpinner = view.findViewById(R.id.spinner_operatingtime);

        leaderEdit = view.findViewById(R.id.edit_leadername);
        leaderSpinner = view.findViewById(R.id.spinner_leader);
        leaderEditButton = view.findViewById(R.id.leaderEditButton);
        leaderEdit.setVisibility(View.INVISIBLE);
        leaderEditEnabled = false;

        memberEdit = view.findViewById(R.id.edit_membername);
        memberSpinner = view.findViewById(R.id.spinner_member);
        memberEditButton = view.findViewById(R.id.memberEditButton);
        memberEdit.setVisibility(View.INVISIBLE);
        memberEditEnabled = false;

        leaderPressure = view.findViewById(R.id.edit_leaderpressure);
        memberPressure = view.findViewById(R.id.edit_memberpressure);
        orderSpinner = view.findViewById(R.id.spinner_order);

        ArrayAdapter<String> operatingTimeAdapter = OperatingTime.getArrayAdapter(getActivity());
        operatingTimeSpinner.setAdapter(operatingTimeAdapter);
        operatingTimeSpinner.setSelection(
                operatingTimeAdapter.getPosition(getString(R.string.operationTimeNormal)));

        ArrayAdapter<String> orderAdapter = Order.getArrayAdapter(getActivity());
        orderSpinner.setAdapter(orderAdapter);

        DraegermanSpinnerAdapter draegermanSpinnerAdapter = new DraegermanSpinnerAdapter(
                getActivity(), R.layout.listitem_spinner,
                ((DraegermanObservationApplication)getActivity().getApplication())
                        .getDraegermanDAO().getAll());
        leaderSpinner.setAdapter(draegermanSpinnerAdapter);
        leaderSpinner.setSelection(draegermanSpinnerAdapter.getCount()+1);
        memberSpinner.setAdapter(draegermanSpinnerAdapter);
        memberSpinner.setSelection(draegermanSpinnerAdapter.getCount()+1);

        leaderEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toogleEdit(true);
            }
        });
        memberEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toogleEdit(false);
            }
        });

        builder.setView(view)
                .setTitle(getResources().getString(R.string.registerSquadTitle))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {}})
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RegisterSquadDialog.this.getDialog().cancel();
                    }
                });
        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                final AlertDialog registerDialog = (AlertDialog) dialogInterface;
                registerDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String squadnameText = squadName.getText().toString();
                        String initLeaderPressure = leaderPressure.getText().toString();
                        String initMemberPressure = memberPressure.getText().toString();
                        String operatingTime = operatingTimeSpinner.getSelectedItem().toString();
                        String order = orderSpinner.getSelectedItem().toString();

                        Draegerman leader;
                        Draegerman member;


                        if(squadnameText.equals("")){
                            Toast.makeText(getActivity(), R.string.toastNoSquadname,
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(leaderEditEnabled){
                            String leaderName = leaderEdit.getText().toString();
                            if(leaderName.equals("")){
                                Toast.makeText(getActivity(), R.string.toastNoLeaderName,
                                        Toast.LENGTH_LONG).show();
                                return;
                            }
                            leader = new Draegerman(leaderName);
                        } else {
                            if(leaderSpinner.getSelectedItemPosition() > leaderSpinner.getCount()){
                                Toast.makeText(getActivity(), R.string.toastNoLeader,
                                        Toast.LENGTH_LONG).show();
                                return;
                            } else {
                                leader = (Draegerman)leaderSpinner.getSelectedItem();
                            }
                        }
                        if(memberEditEnabled){
                            String memberName = memberEdit.getText().toString();
                            if(memberName.equals("")){
                                Toast.makeText(getActivity(), R.string.toastNoMemberName,
                                        Toast.LENGTH_LONG).show();
                                return;
                            }
                            member = new Draegerman(memberName);
                        } else {
                            if(memberSpinner.getSelectedItemPosition() > memberSpinner.getCount()) {
                                Toast.makeText(getActivity(), R.string.toastNoMember,
                                        Toast.LENGTH_LONG).show();
                                return;
                            } else {
                                member = (Draegerman) memberSpinner.getSelectedItem();
                            }
                        }

                        if(!leaderEditEnabled && !memberEditEnabled && leader.equals(member)){
                            Toast.makeText(getActivity(), R.string.toastLeaderMemberEqual,
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        if(initLeaderPressure.equals("")){
                            Toast.makeText(getActivity(), R.string.toastNoPressureLeader,
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(initMemberPressure.equals("")){
                            Toast.makeText(getActivity(), R.string.toastNoPressureMember,
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        int leaderPressure = Integer.parseInt(initLeaderPressure);
                        int memberPressure = Integer.parseInt(initMemberPressure);
                        if(leaderPressure < 0 || memberPressure < 0){
                            Toast.makeText(getActivity(), R.string.toastPressureUnderZero,
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        Squad squad = new Squad(
                                squadnameText, leader, leaderPressure, member, memberPressure,
                                OperatingTime.getOperatingTime(getActivity(), operatingTime),
                                Order.getOrder(getActivity(), order)
                        );
                        listener.onSquadRegistered(squad);
                        registerDialog.dismiss();
                    }
                });
            }
        });
        return dialog;
    }

    private void toogleEdit(boolean leader){
        if(leader){
            if(leaderEditEnabled){
                leaderEditEnabled = false;
                leaderEdit.setVisibility(View.INVISIBLE);
                leaderSpinner.setVisibility(View.VISIBLE);
                leaderEditButton.setImageResource(android.R.drawable.ic_menu_edit);
            } else {
                leaderEditEnabled = true;
                leaderEdit.setVisibility(View.VISIBLE);
                leaderSpinner.setVisibility(View.INVISIBLE);
                leaderEditButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
            }
        } else {
            if(memberEditEnabled){
                memberEditEnabled = false;
                memberEdit.setVisibility(View.INVISIBLE);
                memberSpinner.setVisibility(View.VISIBLE);
                memberEditButton.setImageResource(android.R.drawable.ic_menu_edit);
            }else{
                memberEditEnabled = true;
                memberEdit.setVisibility(View.VISIBLE);
                memberSpinner.setVisibility(View.INVISIBLE);
                memberEditButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
            }
        }
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

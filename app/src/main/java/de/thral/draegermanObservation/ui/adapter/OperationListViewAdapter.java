package de.thral.draegermanObservation.ui.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import de.thral.draegermanObservation.R;
import de.thral.draegermanObservation.persistence.CompleteOperation;

public class OperationListViewAdapter extends ArrayAdapter<CompleteOperation>
        implements CompoundButton.OnCheckedChangeListener{

    private CheckBox operationCheckBox;
    private SparseBooleanArray checkedStates;
    private List<CompleteOperation> objects;

    public OperationListViewAdapter(Context context, int resource, List<CompleteOperation> objects) {
        super(context, resource, objects);
        this.objects = objects;
        checkedStates = new SparseBooleanArray(objects.size());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.listitem_operation_completed, parent, false);
        TextView unexported = rowView.findViewById(R.id.operationUnexported);
        TextView operationIdentifier = rowView.findViewById(R.id.operationIdentifier);
        operationCheckBox = rowView.findViewById(R.id.operationCheckBox);

        CompleteOperation operation = getItem(position);

        if(operation.isExported()){
            unexported.setVisibility(View.INVISIBLE);
        } else {
            unexported.setVisibility(View.VISIBLE);
        }
        operationIdentifier.setText(operation.getIdentifier());

        operationCheckBox.setTag(position);
        operationCheckBox.setOnCheckedChangeListener(this);
        return rowView;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        checkedStates.put((int)compoundButton.getTag(), b);
    }

    public SparseBooleanArray getCheckedItemPositions(){
        return checkedStates;
    }

}

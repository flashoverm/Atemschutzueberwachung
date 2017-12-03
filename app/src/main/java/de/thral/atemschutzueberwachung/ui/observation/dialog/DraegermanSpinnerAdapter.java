package de.thral.atemschutzueberwachung.ui.observation.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.business.Draegerman;

public class DraegermanSpinnerAdapter extends ArrayAdapter<Draegerman> {

    private Context context;
    private List<Draegerman> content;

    public DraegermanSpinnerAdapter(Context context, int resource, List<Draegerman> list) {
        super(context, resource, list);
        this.context = context;
        this.content = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.listitem_spinner, parent, false);
        TextView displayname = rowView.findViewById(R.id.displayname);
        displayname.setTextSize(16);

        if(position == getCount()+1){
            displayname.setText("");
            displayname.setHint(R.string.spinnerHint);
        }else{
            Draegerman draegerman = getItem(position);
            displayname.setText(draegerman.toString());
        }
        return rowView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listitem_spinner, parent, false);
        TextView displayname = rowView.findViewById(R.id.displayname);
        displayname.setTextSize(16);

        Draegerman draegerman = getItem(position);
        displayname.setText(draegerman.toString());

        return rowView;    }
}

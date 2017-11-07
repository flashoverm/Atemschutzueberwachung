package de.thral.atemschutzueberwachung.domain;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import de.thral.atemschutzueberwachung.R;

public enum Order{
    Explore(R.string.orderListExplore),
    Firefighting(R.string.orderListFirefighting),
    Search(R.string.orderListSearch),
    Hazmat(R.string.orderListHazmat),
    Protection(R.string.orderListProtection),
    Other(R.string.orderListOther);

    private int resourceID;

    Order(int resourceID){
        this.resourceID = resourceID;
    }

    public String toString(Context context){
        return context.getString(resourceID);
    }

    public static Order getOrder(Context context, String string){
        for(Order order: Order.values()){
            if(order.toString(context).equals(string)){
                return order;
            }
        }
        throw new Resources.NotFoundException(string);
    }

    public static ArrayAdapter<String> getArrayAdapter(Activity context){
        ArrayList<String> values = new ArrayList<>();
        for(Order order: Order.values()){
            values.add(order.toString(context));
        }
        return new ArrayAdapter<String>(context, R.layout.listitem_spinner, values);
    }
}


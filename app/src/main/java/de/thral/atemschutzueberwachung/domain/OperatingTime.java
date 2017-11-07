package de.thral.atemschutzueberwachung.domain;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import de.thral.atemschutzueberwachung.R;

public enum OperatingTime {
    Debug(R.string.operationTimeDebug, 3),
    Short(R.string.operationTimeShort, 20),
    Normal(R.string.operationTimeNormal, 30),
    Long(R.string.operationTimeLong, 60);

    private int resourceID;
    private int time;

    OperatingTime(int resourceID, int time){
        this.resourceID = resourceID;
        this.time = time;
    }

    public String toString(Context context){
        return context.getString(resourceID);
    }

    public int getTime(){
        return time;
    }

    public static OperatingTime getOperatingTime(Context context, String string){
        for(OperatingTime operatingTime: OperatingTime.values()){
            if(operatingTime.toString(context).equals(string)){
                return operatingTime;
            }
        }
        throw new Resources.NotFoundException(string);
    }

    public static ArrayAdapter<String> getArrayAdapter(Activity context){
        ArrayList<String> values = new ArrayList<>();
        for(OperatingTime operatingTime: OperatingTime.values()){
            values.add(operatingTime.toString(context));
        }
        return new ArrayAdapter<String>(context, R.layout.listitem_spinner, values);
    }
}


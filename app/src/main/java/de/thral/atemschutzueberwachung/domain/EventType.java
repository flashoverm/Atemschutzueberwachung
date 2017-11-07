package de.thral.atemschutzueberwachung.domain;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import de.thral.atemschutzueberwachung.R;

public enum EventType {
    Register(R.string.eventListRegister, R.string.stateListRegister),
    Begin(R.string.eventListBegin, R.string.stateListBegin),
    Arrive(R.string.eventListArrive, R.string.stateListArrive),
    Timer(R.string.eventListTimer, R.string.stateListTimer),
    Retreat(R.string.eventListRetreat, R.string.stateListRetreat),
    End(R.string.eventListEnd, R.string.stateListEnd),
    PauseTimer(R.string.eventListPause, R.string.stateListPause),
    ResumeTimer(R.string.eventListResume, R.string.stateListResume);

    private int resourceID;
    private int stateID;

    EventType(int resourceID, int stateID){
        this.resourceID = resourceID;
        this.stateID = stateID;
    }

    public String toString(Context context){
        return context.getString(resourceID);
    }

    public String getStateDescription(Context context){
        return context.getString(stateID);
    }

    public int getRessourceID() {
        return resourceID;
    }

    public static EventType getEventType(Context context, String string){
        for(EventType event: EventType.values()){
            if(event.toString(context).equals(string)){
                return event;
            }
        }
        throw new Resources.NotFoundException(string);
    }

    public static ArrayAdapter<String> getArrayAdapter(Activity context){
        ArrayList<String> values = new ArrayList<>();
        for(EventType event: EventType.values()){
            values.add(event.toString(context));
        }
        return new ArrayAdapter<String>(context, R.layout.listitem_spinner, values);
    }
}

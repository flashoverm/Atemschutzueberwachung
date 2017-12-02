package de.thral.atemschutzueberwachung.business;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Operation {

    public static final int MAX_SQUAD_COUNT = 4;

    private String operation;
    private String location;
    private String observer;
    private String unit;
    private final long startTime;
    private long endTime;

    private List<Squad> squadList;

    private boolean isExported;

    public Operation(){
        this.startTime = System.currentTimeMillis();
        this.squadList = new ArrayList<>();
    }

    public List<Squad> getSquadList() {
        return squadList;
    }

    public String getOperation() {
        return operation;
    }

    public String getLocation() {
        return location;
    }

    public String getObserver() {
        return observer;
    }

    public String getUnit() {
        return unit;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public Squad[] getActiveSquads() {
        Squad[] activeSquads = new Squad[MAX_SQUAD_COUNT];
        int i=0;
        for(Squad squad:squadList){
            if(squad.getState() != EventType.End){
                activeSquads[i] = squad;
                i++;
            }
        }
        for(i=i; i<MAX_SQUAD_COUNT; i++){
            activeSquads[i] = null;
        }
        return activeSquads;
    }

    public boolean isExported(){
        return isExported;
    }

    public boolean isSquadActive(){
        Squad[] active = getActiveSquads();
        for(int i=0; i<MAX_SQUAD_COUNT; i++){
            if(active[i] != null){
                return true;
            }
        }
        return false;
    }

    public boolean registerSquad(Squad squad) {
        Squad[] active = getActiveSquads();
        for(int i=0; i<MAX_SQUAD_COUNT; i++) {
            if (active[i] == null) {
                this.squadList.add(squad);
                return true;
            }
        }
        return false;
    }

    public boolean complete(String operation, String location,
                                     String observer, String unit) {
        if(!isSquadActive()){
            this.operation = operation;
            this.location = location;
            this.observer = observer;
            this.unit = unit;
            this.endTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public void export(){
        this.isExported = true;
    }

    public String getFilename() {
        return toString().replace(':', '-');
    }

    @Override
    public String toString() {
        return new SimpleDateFormat("dd.MM.yyyy hh:mm").format(new Timestamp(startTime)).toString()
                + " - " + operation + " - " + location;
    }
}

package de.thral.atemschutzueberwachung.Monitoring;

import java.util.ArrayList;
import java.util.List;

import de.thral.atemschutzueberwachung.Draegerman.Draegerman;

/**
 * Created by Markus Thral on 26.10.2017.
 */

public class Operation {

    private String operation;
    private String location;
    private String observer;
    private String unit;

    private final long startTime;
    private long endTime;

    private List<Squad> squadList;

    public Operation(){
        this.startTime = System.currentTimeMillis();
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

    public List<Squad> getActiveSquads() {
        List<Squad> activeSquads = new ArrayList<>();
        for(Squad squad:squadList){
            if(!squad.getLastEventType().equals(Event.Type.End)){
                activeSquads.add(squad);
            }
        }
        return activeSquads;
    }

    public boolean addSquad(Draegerman leader, int initialPressureLeader,
                            Draegerman member, int initialPressureMember,
                            Squad.OperationTime operationTime, Squad.Order order) {

        if(getActiveSquads().size() <4){
            this.squadList.add(new Squad(leader, initialPressureLeader, member,
                    initialPressureMember, operationTime, order));
        }
        return false;
    }

    public boolean completeOperation(String operation, String location,
                                     String observer, String unit) {
        if(getActiveSquads().size() == 0){
            this.operation = operation;
            this.location = location;
            this.observer = observer;
            this.unit = unit;
            this.endTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

}

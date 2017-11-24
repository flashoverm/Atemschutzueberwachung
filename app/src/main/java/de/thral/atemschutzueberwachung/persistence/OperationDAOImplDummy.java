package de.thral.atemschutzueberwachung.persistence;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import de.thral.atemschutzueberwachung.business.Draegerman;
import de.thral.atemschutzueberwachung.business.OperatingTime;
import de.thral.atemschutzueberwachung.business.Operation;
import de.thral.atemschutzueberwachung.business.Order;
import de.thral.atemschutzueberwachung.business.Squad;

public class OperationDAOImplDummy implements OperationDAO {

    private Operation activeOperation;

    @Override
    public Operation getActive() {
        return activeOperation;
    }

    @Override
    public void createOperation() {
        activeOperation = new Operation();
        activeOperation.registerSquad(new Squad("Angriff 2/40/1", new Draegerman("Bernd", "Beispiel"), 300, new Draegerman("Andreas", "Atemschutzträger"), 310, OperatingTime.Debug, Order.Firefighting));
        activeOperation.registerSquad(new Squad("Sicherung 1/21/1", new Draegerman("Max", "Mustermann"), 310, new Draegerman("Theo", "Test"), 300, OperatingTime.Debug, Order.Firefighting));
    }

    @Override
    public boolean updateActive() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println("UPDATE: ");
        System.out.println(gson.toJson(getActive()));
        return true;
    }

    @Override
    public boolean endOperation() {
        return true;
    }

    @Override
    public List<CompleteOperation> getCompletedOperations() {
        return null;
    }

    @Override
    public boolean setupStorage(Activity activity) {
        return false;
    }

    @Override
    public boolean exportOperation(CompleteOperation export) {
        return false;
    }

    @Override
    public boolean removeCompletedOperation(CompleteOperation operation) {
        return true;
    }
}

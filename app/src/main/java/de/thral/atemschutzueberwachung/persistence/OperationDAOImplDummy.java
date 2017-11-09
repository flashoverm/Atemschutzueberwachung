package de.thral.atemschutzueberwachung.persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.thral.atemschutzueberwachung.domain.Draegerman;
import de.thral.atemschutzueberwachung.domain.OperatingTime;
import de.thral.atemschutzueberwachung.domain.Operation;
import de.thral.atemschutzueberwachung.domain.Order;
import de.thral.atemschutzueberwachung.domain.Squad;

/**
 * Created by Markus Thral on 28.10.2017.
 */

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
    public boolean update() {
        return true;
    }

    @Override
    public boolean endOperation(Operation operation) {
        return true;
    }

    @Override
    public List<Operation> getCompletedOperations() {
        return null;
    }

    @Override
    public List<File> getCompletedOperationsAsFiles() {
        return null;
    }
}

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

    private Operation operation;

    public OperationDAOImplDummy(){
        operation = new Operation();
        operation.registerSquad(new Squad("Trupp 1", new Draegerman("Markus", "Thral"), 300, new Draegerman("Bernd", "Beisp"), 310, OperatingTime.Debug, Order.Firefighting));
        //operation.registerSquad(new Squad("Trupp 2", new Draegerman("Franz", "Eber"), 310, new Draegerman("Hans", "Wurst"), 300, OperatingTime.Debug, Order.Firefighting));
        //operation.registerSquad(new Squad("Trupp 3", new Draegerman("Test", "Theo"), 310, new Draegerman("Franz", "Furz"), 300, OperatingTime.Normal, Order.Firefighting));
        operation.registerSquad(new Squad("Trupp 4", new Draegerman("Test", "Dürsc"), 310, new Draegerman("Hans", "Wurst"), 300, OperatingTime.Normal, Order.Firefighting));
    }

    @Override
    public Operation getActive() {
        return operation;
    }

    @Override
    public boolean update(Operation operation) {
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

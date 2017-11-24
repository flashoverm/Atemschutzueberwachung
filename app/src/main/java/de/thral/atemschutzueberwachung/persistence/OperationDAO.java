package de.thral.atemschutzueberwachung.persistence;

import android.app.Activity;

import java.util.List;

import de.thral.atemschutzueberwachung.business.Operation;

public interface OperationDAO {

    Operation getActive();
    void createOperation();
    boolean updateActive();
    boolean endOperation();

    List<CompleteOperation> getCompletedOperations();
    boolean setupStorage(Activity activity);
    boolean exportOperation(CompleteOperation export);
    boolean removeCompletedOperation(CompleteOperation operation);
}

package de.thral.atemschutzueberwachung.persistence;

import android.app.Activity;
import android.content.Context;

import java.io.File;
import java.util.List;
import java.util.ListIterator;

import de.thral.atemschutzueberwachung.domain.Operation;

/**
 * Created by Markus Thral on 28.10.2017.
 */

public interface OperationDAO {

    Operation getActive();
    void createOperation();
    boolean update();
    boolean endOperation();

    List<Operation> getCompletedOperations();
    boolean setupStorage(Activity activity);
    boolean exportOperation(Operation export);
    boolean removeCompletedOperation(Operation operation);
}

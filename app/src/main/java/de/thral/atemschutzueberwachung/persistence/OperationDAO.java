package de.thral.atemschutzueberwachung.persistence;

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
    boolean update(Operation operation);
    boolean endOperation(Operation operation);

    List<Operation> getCompletedOperations();
    List<File> getCompletedOperationsAsFiles();
}
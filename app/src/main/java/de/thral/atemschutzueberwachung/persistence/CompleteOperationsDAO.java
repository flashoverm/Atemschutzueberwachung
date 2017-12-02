package de.thral.atemschutzueberwachung.persistence;

import android.app.Activity;

import java.util.List;

import de.thral.atemschutzueberwachung.business.Operation;

public interface CompleteOperationsDAO {

    List<CompleteOperation> getAll();
    boolean add(Operation operation);
    boolean remove(CompleteOperation operation);

    boolean setupStorage(Activity activity);
    boolean export(CompleteOperation export);

}

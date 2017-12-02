package de.thral.atemschutzueberwachung.persistence;

import de.thral.atemschutzueberwachung.business.Operation;

public interface ActiveOperationDAO {

    Operation get();
    void load();
    boolean create();
    boolean update();
    boolean end();
}

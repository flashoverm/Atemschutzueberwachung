package de.thral.atemschutzueberwachung;

import android.app.Application;

import de.thral.atemschutzueberwachung.domain.Draegerman;
import de.thral.atemschutzueberwachung.persistence.DraegermanDAO;
import de.thral.atemschutzueberwachung.persistence.DraegermanDAOImplDummy;
import de.thral.atemschutzueberwachung.persistence.OperationDAO;
import de.thral.atemschutzueberwachung.persistence.OperationDAOImplDummy;

public class DraegermanObservationApplication extends Application {

    private OperationDAO operationDAO;
    private DraegermanDAO draegermanDAO;

    @Override
    public void onCreate() {
        super.onCreate();
        operationDAO = new OperationDAOImplDummy();
        draegermanDAO = new DraegermanDAOImplDummy();
    }

    public OperationDAO getOperationDAO(){
        return operationDAO;
    }

    public DraegermanDAO getDraegermanDAO(){
        return draegermanDAO;
    }
}

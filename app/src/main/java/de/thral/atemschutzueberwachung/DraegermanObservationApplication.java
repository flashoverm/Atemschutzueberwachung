package de.thral.atemschutzueberwachung;

import android.app.Application;

import de.thral.atemschutzueberwachung.persistence.DraegermanDAO;
import de.thral.atemschutzueberwachung.persistence.DraegermanDAOImpl;
import de.thral.atemschutzueberwachung.persistence.OperationDAO;
import de.thral.atemschutzueberwachung.persistence.OperationDAOImpl;

public class DraegermanObservationApplication extends Application {

    private OperationDAO operationDAO;
    private DraegermanDAO draegermanDAO;

    @Override
    public void onCreate() {
        super.onCreate();
        operationDAO = new OperationDAOImpl(getApplicationContext());
        draegermanDAO = new DraegermanDAOImpl(getApplicationContext());
    }

    public OperationDAO getOperationDAO(){
        return operationDAO;
    }

    public DraegermanDAO getDraegermanDAO(){
        return draegermanDAO;
    }
}

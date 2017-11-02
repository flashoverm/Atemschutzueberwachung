package de.thral.atemschutzueberwachung;

import android.app.Application;

import de.thral.atemschutzueberwachung.persistence.OperationDAO;
import de.thral.atemschutzueberwachung.persistence.OperationDAOImplDummy;

/**
 * Created by Markus Thral on 01.11.2017.
 */

public class AtemschutzueberwachungApplication extends Application {

    OperationDAO operationDAO;

    @Override
    public void onCreate() {
        super.onCreate();
        operationDAO = new OperationDAOImplDummy();

        //TODO startTimer on active squads, if necessary
    }

    public OperationDAO getOperationDAO(){
        return operationDAO;
    }
}

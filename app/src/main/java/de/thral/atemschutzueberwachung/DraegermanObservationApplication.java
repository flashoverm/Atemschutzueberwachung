package de.thral.atemschutzueberwachung;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import de.thral.atemschutzueberwachung.hardware.HardwareInterface;
import de.thral.atemschutzueberwachung.persistence.DraegermanDAO;
import de.thral.atemschutzueberwachung.persistence.DraegermanDAOImpl;
import de.thral.atemschutzueberwachung.persistence.OperationDAO;
import de.thral.atemschutzueberwachung.persistence.OperationDAOImpl;

public class DraegermanObservationApplication extends Application {

    private OperationDAO operationDAO;
    private DraegermanDAO draegermanDAO;

    private HardwareInterface hardwareInterface;

    @Override
    public void onCreate() {
        super.onCreate();
        Context context = getApplicationContext();
        operationDAO = new OperationDAOImpl(context);
        draegermanDAO = new DraegermanDAOImpl(context);
        hardwareInterface = new HardwareInterface(context);
    }

    public HardwareInterface getHardwareInterface(){
        return hardwareInterface;
    }

    public static HardwareInterface getHardwareInterface(Context context){
        DraegermanObservationApplication activity
                = (DraegermanObservationApplication)((Activity)context).getApplication();
        return activity.getHardwareInterface();
    }

    public OperationDAO getOperationDAO(){
        return operationDAO;
    }

    public DraegermanDAO getDraegermanDAO(){
        return draegermanDAO;
    }
}

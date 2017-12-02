package de.thral.atemschutzueberwachung;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import de.thral.atemschutzueberwachung.hardware.HardwareInterface;
import de.thral.atemschutzueberwachung.persistence.ActiveOperationDAO;
import de.thral.atemschutzueberwachung.persistence.ActiveOperationDAOImpl;
import de.thral.atemschutzueberwachung.persistence.CompleteOperationsDAO;
import de.thral.atemschutzueberwachung.persistence.CompleteOperationsDAOImpl;
import de.thral.atemschutzueberwachung.persistence.DraegermanDAO;
import de.thral.atemschutzueberwachung.persistence.DraegermanDAOImpl;

public class DraegermanObservationApplication extends Application {

    private ActiveOperationDAO activeOperationDAO;
    private DraegermanDAO draegermanDAO;
    private CompleteOperationsDAO completeOperationsDAO;

    private HardwareInterface hardwareInterface;

    @Override
    public void onCreate() {
        super.onCreate();
        Context context = getApplicationContext();
        activeOperationDAO = new ActiveOperationDAOImpl(context);
        draegermanDAO = new DraegermanDAOImpl(context);
        completeOperationsDAO = new CompleteOperationsDAOImpl(context);
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

    public ActiveOperationDAO getActiveOperationDAO(){
        return activeOperationDAO;
    }

    public DraegermanDAO getDraegermanDAO(){
        return draegermanDAO;
    }

    public CompleteOperationsDAO getCompleteOperationsDAO() {
        return completeOperationsDAO;
    }
}

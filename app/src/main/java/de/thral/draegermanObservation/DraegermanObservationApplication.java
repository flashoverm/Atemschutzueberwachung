package de.thral.draegermanObservation;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import de.thral.draegermanObservation.hardware.HardwareInterface;
import de.thral.draegermanObservation.persistence.ActiveOperationDAO;
import de.thral.draegermanObservation.persistence.ActiveOperationDAOImpl;
import de.thral.draegermanObservation.persistence.CompleteOperationsDAO;
import de.thral.draegermanObservation.persistence.CompleteOperationsDAOImpl;
import de.thral.draegermanObservation.persistence.DraegermanDAO;
import de.thral.draegermanObservation.persistence.DraegermanDAOImpl;

public class DraegermanObservationApplication extends Application {

    //TODO remove delay
    public static final int DEBUG_IO_DELAY = 1000;

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

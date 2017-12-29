package de.thral.draegermanObservation;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import de.thral.draegermanObservation.deviceNotification.DeviceNotificationInterface;
import de.thral.draegermanObservation.persistence.ActiveOperationDAO;
import de.thral.draegermanObservation.persistence.ActiveOperationDAOImpl;
import de.thral.draegermanObservation.persistence.CompleteOperationsDAO;
import de.thral.draegermanObservation.persistence.CompleteOperationsDAOImpl;
import de.thral.draegermanObservation.persistence.DraegermanDAO;
import de.thral.draegermanObservation.persistence.DraegermanDAOImpl;

public class DraegermanObservationApplication extends Application {

    //TODO remove delay
    public static final int DEBUG_IO_DELAY = 800;

    private ActiveOperationDAO activeOperationDAO;
    private DraegermanDAO draegermanDAO;
    private CompleteOperationsDAO completeOperationsDAO;

    private DeviceNotificationInterface deviceNotificationInterface;

    @Override
    public void onCreate() {
        super.onCreate();
        Context context = getApplicationContext();
        activeOperationDAO = new ActiveOperationDAOImpl(context);
        draegermanDAO = new DraegermanDAOImpl(context);
        completeOperationsDAO = new CompleteOperationsDAOImpl(context);
        deviceNotificationInterface = new DeviceNotificationInterface(context);
    }

    public DeviceNotificationInterface getDeviceNotificationInterface(){
        return deviceNotificationInterface;
    }

    public static DeviceNotificationInterface getDeviceNotificationInterface(Context context){
        DraegermanObservationApplication activity
                = (DraegermanObservationApplication)((Activity)context).getApplication();
        return activity.getDeviceNotificationInterface();
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

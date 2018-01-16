package de.thral.draegermanObservation;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import de.thral.draegermanObservation.notification.NotificationManager;
import de.thral.draegermanObservation.persistence.ActiveOperationDAO;
import de.thral.draegermanObservation.persistence.ActiveOperationDAOImpl;
import de.thral.draegermanObservation.persistence.CompleteOperationsDAO;
import de.thral.draegermanObservation.persistence.CompleteOperationsDAOImpl;
import de.thral.draegermanObservation.persistence.DraegermanDAO;
import de.thral.draegermanObservation.persistence.DraegermanDAOImpl;

public class DraegermanObservationApplication extends Application {

    //TODO remove delay
    public static final int DEBUG_IO_DELAY = 600;

    private ActiveOperationDAO activeOperationDAO;
    private DraegermanDAO draegermanDAO;
    private CompleteOperationsDAO completeOperationsDAO;

    private NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Context context = getApplicationContext();
        activeOperationDAO = new ActiveOperationDAOImpl(context);
        draegermanDAO = new DraegermanDAOImpl(context);
        completeOperationsDAO = new CompleteOperationsDAOImpl(context);
        notificationManager = new NotificationManager(context);
    }

    public static NotificationManager getNotificationManager(Activity activity){
        return getApplication(activity).notificationManager;
    }

    public static ActiveOperationDAO getActiveOperationDAO(Activity activity){
        return getApplication(activity).activeOperationDAO;
    }

    public static DraegermanDAO getDraegermanDAO(Activity activity){
        return getApplication(activity).draegermanDAO;
    }

    public static CompleteOperationsDAO getCompleteOperationsDAO(Activity activity){
        return getApplication(activity).completeOperationsDAO;
    }

    private static DraegermanObservationApplication getApplication(Activity activity){
        return (DraegermanObservationApplication)activity.getApplication();
    }
}

package de.thral.draegermanObservation.persistence;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import de.thral.draegermanObservation.DraegermanObservationApplication;
import de.thral.draegermanObservation.business.Operation;

public class ActiveOperationDAOImpl implements ActiveOperationDAO {

    private static final String ACTIVE_FILE = "active.json";

    private Context context;
    private Operation activeOperation;

    public ActiveOperationDAOImpl(Context context){
        this.context = context;
        this.activeOperation = null;
    }

    @Override
    public Operation get() {
        return activeOperation;
    }

    @Override
    public void load(){

        try {
            Thread.sleep(DraegermanObservationApplication.DEBUG_IO_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        File active = new File(context.getFilesDir(), ACTIVE_FILE);
        if(active.exists()){
            Gson gson = new Gson();
            try(JsonReader reader = new JsonReader(new FileReader(active))) {
                activeOperation = gson.fromJson(reader, Operation.class);
            }catch (IOException e){
                Log.e(LOG_TAG, e.getMessage());
                //File not existing -> No active operation
            }
        }
    }

    @Override
    public boolean add(){
        this.activeOperation = new Operation();
        return true;
    }

    @Override
    public boolean update() {
        try {
            Thread.sleep(DraegermanObservationApplication.DEBUG_IO_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        File active = new File(context.getFilesDir(), ACTIVE_FILE);
        Gson gson = new Gson();
        try(FileWriter writer = new FileWriter(active)){
            writer.append(gson.toJson(get()));
            writer.flush();
            return true;
        }catch(IOException e) {
            Log.e(LOG_TAG, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete() {
        File active = new File(context.getFilesDir(), ACTIVE_FILE);
        if(active.delete()){
            this.activeOperation = null;
            return true;
        }
        return false;
    }
}

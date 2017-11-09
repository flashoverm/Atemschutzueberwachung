package de.thral.atemschutzueberwachung.persistence;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.thral.atemschutzueberwachung.domain.EventType;
import de.thral.atemschutzueberwachung.domain.Operation;
import de.thral.atemschutzueberwachung.domain.Squad;

public class OperationDAOImpl implements OperationDAO {

    private static final String ACTIVE_FILE = "active.json";

    private static final Type OPERATION_TYPE = new TypeToken<List<Operation>>(){}.getType();

    private Context context;
    private Operation activeOperation;

    public OperationDAOImpl(Context context){
        this.context = context;
        this.activeOperation = loadActive();
        if(activeOperation != null){
            for(Squad squad : activeOperation.getActiveSquads()){
                if(!squad.getState().equals(EventType.PauseTimer)
                        && !squad.getState().equals(EventType.Register)){
                    //TODO squad.resumeAfterError();
                }
            }
        }
    }

    @Override
    public Operation getActive() {
        return activeOperation;
    }

    protected Operation loadActive(){
        try{
            File active = new File(context.getFilesDir(), ACTIVE_FILE);
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(active));
            return gson.fromJson(reader, Operation.class);
        } catch(IOException e){
            return null;
        }
    }

    public void createOperation(){
        this.activeOperation = new Operation();
        update();
    }

    @Override
    public boolean update() {
        try{
            File active = new File(context.getFilesDir(), ACTIVE_FILE);
            Gson gson = new Gson();
            FileWriter writer = new FileWriter(active);
            writer.append(gson.toJson(getActive()));
            writer.flush();
            writer.close();
            return true;
        }catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean endOperation(Operation operation) {
        try{
            File completed = new File(context.getFilesDir(), operation.getFilename()+".json");
            Gson gson = new Gson();
            gson.toJson(operation, new FileWriter(completed));
            File active = new File(context.getFilesDir(), ACTIVE_FILE);
            active.delete();
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Operation> getCompletedOperations() {
        List<Operation> completed = new ArrayList<>();
        /*try{
            Gson gson = new Gson();
            JsonReader reader;
            File directory = new File(COMPLETED_PATH);
            while(directory.){

                reader = new JsonReader(new FileReader(COMPLETED_PATH));
                completed.add((Operation)gson.fromJson(reader, Operation.class));
            }
        }catch(FileNotFoundException e){
        }*/
        return completed;
    }

    @Override
    public List<File> getCompletedOperationsAsFiles() {
        return null;
    }
}

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

import de.thral.atemschutzueberwachung.domain.Operation;

/**
 * Created by Markus Thral on 01.11.2017.
 */

public class OperationDAOImpl implements OperationDAO {

    private static final String ACTIVE_FILE = "active.json";

       private static final Type OPERATION_TYPE = new TypeToken<List<Operation>>() {
    }.getType();

    private Context context;

    public OperationDAOImpl(Context context){
        this.context = context;
    }

    @Override
    public Operation getActive() {
       try{
            File active = new File(context.getFilesDir(), ACTIVE_FILE);
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(active));
            return gson.fromJson(reader, Operation.class);
        } catch(IOException e){
            return new Operation();
        }
    }

    @Override
    public boolean update(Operation operation) {
        try{
            File active = new File(context.getFilesDir(), ACTIVE_FILE);
            Gson gson = new Gson();
            FileWriter writer = new FileWriter(active);
            writer.append(gson.toJson(operation));
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

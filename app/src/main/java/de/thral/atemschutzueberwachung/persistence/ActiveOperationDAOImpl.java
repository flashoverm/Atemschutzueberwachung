package de.thral.atemschutzueberwachung.persistence;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import de.thral.atemschutzueberwachung.business.Operation;

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
        File active = new File(context.getFilesDir(), ACTIVE_FILE);
        if(active.exists()){
            Gson gson = new Gson();
            try(JsonReader reader = new JsonReader(new FileReader(active))) {
                activeOperation = gson.fromJson(reader, Operation.class);
            }catch (IOException e){
                //File not existing -> No active operation
            }
        }
    }

    @Override
    public boolean create(){
        this.activeOperation = new Operation();
        return save();
    }

    @Override
    public boolean save() {
        File active = new File(context.getFilesDir(), ACTIVE_FILE);
        Gson gson = new Gson();
        try(FileWriter writer = new FileWriter(active)){
            writer.append(gson.toJson(get()));
            writer.flush();
            return true;
        }catch(IOException e) {
            Log.e("PERSISTENCE", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean end() {
        File active = new File(context.getFilesDir(), ACTIVE_FILE);
        if(active.delete()){
            this.activeOperation = null;
            return true;
        }
        return false;
    }
}

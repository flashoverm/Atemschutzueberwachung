package de.thral.atemschutzueberwachung.persistence;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.domain.Operation;

public class OperationDAOImpl implements OperationDAO {

    private static final String ACTIVE_FILE = "active.json";
    private static final String COMPLETED_FOLDER = "completed/";

    private Context context;
    private Operation activeOperation;
    private File completedFolder;
    private File exportFolder;

    public OperationDAOImpl(Context context){
        this.context = context;
        this.activeOperation = loadActive();

        completedFolder = new File(context.getFilesDir(), COMPLETED_FOLDER);
        completedFolder.mkdirs();
    }

    @Override
    public Operation getActive() {
        return activeOperation;
    }

    protected Operation loadActive(){
        try{
            File active = new File(context.getFilesDir(), ACTIVE_FILE);
            if(active.exists()){
                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new FileReader(active));
                return gson.fromJson(reader, Operation.class);
            }
        } catch(IOException e){
        }
        return null;
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
    public boolean endOperation() {
        try{
            File completed = new File(completedFolder, getActive().getFilename()+".json");
            Gson gson = new Gson();
            FileWriter writer = new FileWriter(completed);
            writer.append(gson.toJson(getActive()));
            writer.flush();
            writer.close();
            File active = new File(context.getFilesDir(), ACTIVE_FILE);
            active.delete();
            this.activeOperation = null;
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Operation> getCompletedOperations() {
        List<Operation> completedOperations = new ArrayList<>();
        try{
            Gson gson = new Gson();
            JsonReader reader;
            for(File completed : completedFolder.listFiles()){
                reader = new JsonReader(new FileReader(completed));
                completedOperations.add((Operation)gson.fromJson(reader, Operation.class));
            }
            Collections.sort(completedOperations);
        }catch(FileNotFoundException e){
        }
        return completedOperations;
    }

    @Override
    public boolean exportOperation(Operation export){
        Gson gson = new Gson();
        File exportFile = new File(exportFolder, export.getFilename()+".json");
        try{
            FileWriter writer = new FileWriter(exportFile);
            writer.append(gson.toJson(export));
            writer.flush();
            writer.close();
            MediaScannerConnection.scanFile(
                    context, new String[] {exportFile.getAbsolutePath()}, null, null);
            return true;
        } catch(IOException e){
        }
        return false;
    }

    @Override
    public boolean removeCompletedOperation(Operation operation) {
        File completed = new File(completedFolder, operation.getFilename()+".json");
        return completed.delete();
    }

    @Override
    public boolean setupStorage(Activity activity){
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 387);
            return false;
        }
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            exportFolder = new File(
                    Environment.getExternalStorageDirectory(),
                    context.getString(R.string.folderNameExternal) + "/");
            if(!exportFolder.exists()){
                return exportFolder.mkdir();
            }
            return true;
        }
        return false;
    }
}

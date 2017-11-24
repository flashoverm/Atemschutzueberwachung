package de.thral.atemschutzueberwachung.persistence;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.thral.atemschutzueberwachung.R;
import de.thral.atemschutzueberwachung.business.Operation;

public class OperationDAOImpl implements OperationDAO {

    private static final String ACTIVE_FILE = "active.json";
    private static final String COMPLETED_OPS = "completed.json";
    private static final String COMPLETED_FOLDER = "completed/";

    private static final Type operationEntryListType
            = new TypeToken<List<CompleteOperation>>(){}.getType();

    private Context context;
    private Operation activeOperation;
    private File completedFolder;
    private File exportFolder;

    private List<CompleteOperation> completeOperations;

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
        updateActive();
    }

    @Override
    public boolean updateActive() {
        try{
            File active = new File(context.getFilesDir(), ACTIVE_FILE);
            Gson gson = new Gson();
            FileWriter writer = new FileWriter(active);
            writer.append(gson.toJson(getActive()));
            writer.flush();
            return true;
        }catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean endOperation() {
        try{
            File completedFile = new File(completedFolder, getActive().getFilename()+".json");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter(completedFile);
            writer.append(gson.toJson(getActive()));
            writer.flush();

            completeOperations.add(new CompleteOperation(completedFile, activeOperation.toString()));
            Collections.sort(completeOperations);
            updateCompletedOperations();

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
    public List<CompleteOperation> getCompletedOperations() {
        if(completeOperations == null){
            try{
                File completedOperationsFile = new File(context.getFilesDir(), COMPLETED_OPS);
                if(completedOperationsFile.exists()){
                    Gson gson = new Gson();
                    JsonReader reader = new JsonReader(new FileReader(completedOperationsFile));
                    completeOperations = gson.fromJson(reader, operationEntryListType);
                    return completeOperations;
                }
            } catch(IOException e){
                e.printStackTrace();
            }
            return new ArrayList<>();
        }
        return completeOperations;
    }

    @Override
    public boolean exportOperation(CompleteOperation export){
        List<CompleteOperation> completedOperations = getCompletedOperations();
        CompleteOperation completed = completedOperations.get(completedOperations.indexOf(export));
        if(completed.export(exportFolder)){
            MediaScannerConnection.scanFile(
                    context, new String[] {export.getFile().getAbsolutePath()}, null, null);
            return updateCompletedOperations();
        }
        return false;
    }

    private boolean updateCompletedOperations(){
        try{
            File completedOperationsFile = new File(context.getFilesDir(), COMPLETED_OPS);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter(completedOperationsFile);
            writer.write(gson.toJson(completeOperations));
            writer.flush();
            return true;
        } catch(IOException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeCompletedOperation(CompleteOperation operation) {
        List<CompleteOperation> completedOperations = getCompletedOperations();
        if(completedOperations.remove(operation)){
            if(updateCompletedOperations()){
                File completed = new File(operation.getFile().getAbsolutePath());
                return completed.delete();
            }
        }
        return false;
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

package de.thral.draegermanObservation.persistence;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

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

import de.thral.draegermanObservation.DraegermanObservationApplication;
import de.thral.draegermanObservation.R;
import de.thral.draegermanObservation.business.Operation;

public class CompleteOperationsDAOImpl implements CompleteOperationsDAO {

    private static final String COMPLETED_OPS = "completeOperations.json";
    private static final String COMPLETED_FOLDER = "complete/";

    private static final Type operationEntryListType
            = new TypeToken<List<CompleteOperation>>(){}.getType();

    private Context context;

    private List<CompleteOperation> completeOperations;

    private File completedFolder;
    private File exportFolder;

    public CompleteOperationsDAOImpl(Context context){
        this.context = context;

        completedFolder = new File(context.getFilesDir(), COMPLETED_FOLDER);
        if(!completedFolder.exists()){
            completedFolder.mkdirs();
            //TODO Ordner konnte nicht angelegt werden
        }
    }

    @Override
    public List<CompleteOperation> getAll() {
        load();
        return completeOperations;
    }

    /** Loads completed operations from the storage file
     *
     */
    private void load() {
        if(completeOperations == null){

            try {
                Thread.sleep(DraegermanObservationApplication.DEBUG_IO_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            File completedOperationsFile = new File(context.getFilesDir(), COMPLETED_OPS);
            if(completedOperationsFile.exists()){
                Gson gson = new Gson();
                try(JsonReader reader = new JsonReader(new FileReader(completedOperationsFile))){
                    completeOperations = gson.fromJson(reader, operationEntryListType);
                    return;
                }catch(IOException e){
                    //File not existing -> Initialize list
                }
            }
            completeOperations = new ArrayList<>();
        }
    }

    @Override
    public boolean add(Operation operation){
        File completedFile = new File(completedFolder, operation.getFilename()+".json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try(FileWriter writer = new FileWriter(completedFile)){
            writer.append(gson.toJson(operation));
            writer.flush();
        } catch(IOException e){
            Log.e("PERSISTENCE", e.getMessage());
            return false;
        }

        load();
        CompleteOperation complete = new CompleteOperation(completedFile, operation.toString());
        if(completeOperations.add(complete)){
            Collections.sort(completeOperations);
            try{
                save();
                return true;
            }catch (IOException e){
                completeOperations.remove(complete);
            }
        }
        return false;
    }

    @Override
    public boolean delete(CompleteOperation operation) {
        load();
        if(completeOperations.remove(operation)){
            try{
                save();
            }catch(IOException e){
                return false;
            }
            File completed = new File(operation.getFile().getAbsolutePath());
            return completed.delete();
        }
        return false;
    }

    /** Saves completed operation to the storage file
     *
     * @throws IOException if writing fails
     */
    private void save() throws IOException{

        try {
            Thread.sleep(DraegermanObservationApplication.DEBUG_IO_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        File completedOperationsFile = new File(context.getFilesDir(), COMPLETED_OPS);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try(FileWriter writer = new FileWriter(completedOperationsFile)){
            writer.write(gson.toJson(completeOperations));
            writer.flush();
        } catch(IOException e){
            Log.e("PERSISTENCE", e.getMessage());
            throw e;
        }
    }

    /*
        Export operation(s)
     */

    @Override
    public boolean export(CompleteOperation export){
        CompleteOperation completed = completeOperations.get(completeOperations.indexOf(export));
        if(completed.export(exportFolder)){
            MediaScannerConnection.scanFile(
                    context, new String[] {export.getFile().getAbsolutePath()}, null, null);
            try{
                save();
                return true;
            } catch(IOException e){
                return false;
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

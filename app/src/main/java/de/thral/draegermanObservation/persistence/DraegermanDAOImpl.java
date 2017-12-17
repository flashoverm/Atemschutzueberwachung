package de.thral.draegermanObservation.persistence;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
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
import de.thral.draegermanObservation.business.Draegerman;

public class DraegermanDAOImpl implements DraegermanDAO {

    private static final String DRAEGERMEN = "draegermen.json";
    private static final Type DRAEGERMEN_TYPE = new TypeToken<List<Draegerman>>(){}.getType();

    private Context context;
    private List<Draegerman> draegermen;

    public DraegermanDAOImpl(Context context){
        this.context = context;
        new InitDraegermenTask().execute();
    }

    @Override
    public List<Draegerman> getAll() {
        load();
        return draegermen;
    }

    /** Loads list of draegerman from the storage file
     *
     */
    private void load(){
        if(draegermen == null){

            try {
                Thread.sleep(DraegermanObservationApplication.DEBUG_IO_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            File draegermenFile = new File(context.getFilesDir(), DRAEGERMEN);
            if(draegermenFile.exists()){
                Gson gson = new Gson();
                try(JsonReader reader = new JsonReader(new FileReader(draegermenFile))){
                    draegermen =  gson.fromJson(reader, DRAEGERMEN_TYPE);
                    return;
                } catch(IOException e){
                    //File not existing -> Initialize list
                }
            }
            draegermen = new ArrayList<>();
        }
    }

    @Override
    public Draegerman prepareAdd(String firstname, String lastname) {
        load();
        Draegerman draegerman = new Draegerman(firstname, lastname);
        for(Draegerman existing : draegermen){
            if(existing.equals(draegerman)){
                return null;
            }
            if(existing.getLastName().equals(draegerman.getLastName())){
                existing.setDisplayName(existing.toString());
                draegerman.setDisplayName(draegerman.toString());
            }
        }
        return draegerman;
    }

    @Override
    public boolean add(final Draegerman newDraegerman){
        if(draegermen.add(newDraegerman)){
            Collections.sort(draegermen);
            try{
                save();
            }catch(IOException e){
                draegermen.remove(newDraegerman);
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Draegerman draegerman) {
        load();
        if(draegermen.remove(draegerman)){
            try{
                save();
                return true;
            }catch(IOException e){
                draegermen.add(draegerman);
            }
        }
        //not found/could not update
        return false;
    }

    /** Saves list of draegerman to the storage file
     *
     * @throws IOException if writing fails
     */
    private void save() throws IOException {

        try {
            Thread.sleep(DraegermanObservationApplication.DEBUG_IO_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        File draegermenFile = new File(context.getFilesDir(), DRAEGERMEN);
        Gson gson = new Gson();
        try(FileWriter writer = new FileWriter(draegermenFile)){
            writer.append(gson.toJson(draegermen));
            writer.flush();
        }catch(IOException e) {
            Log.e("PERSISTENCE", e.getMessage());
            throw e;
        }
    }

    private class InitDraegermenTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            load();
            return null;
        }
    }
}

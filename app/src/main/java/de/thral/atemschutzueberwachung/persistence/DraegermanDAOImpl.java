package de.thral.atemschutzueberwachung.persistence;

import android.content.Context;

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

import de.thral.atemschutzueberwachung.domain.Draegerman;
import de.thral.atemschutzueberwachung.domain.Operation;

public class DraegermanDAOImpl implements DraegermanDAO {

    private static final String DRAEGERMEN = "draegermen.json";
    private static final Type DRAEGERMEN_TYPE = new TypeToken<List<Draegerman>>(){}.getType();

    private Context context;
    private List<Draegerman> draegermen;

    public DraegermanDAOImpl(Context context){
        this.context = context;
        draegermen = loadDraegermanList();
        if(draegermen == null){
            draegermen = new ArrayList<>();
            saveDraegermanList();
        }
    }

    @Override
    public List<Draegerman> getAll() {
        return draegermen;
    }

    private List<Draegerman> loadDraegermanList(){
        try{
            File draegermenFile = new File(context.getFilesDir(), DRAEGERMEN);
            if(draegermenFile.exists()){
                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new FileReader(draegermenFile));
                return gson.fromJson(reader, DRAEGERMEN_TYPE);
            }
        } catch(IOException e){
        }
        return null;
    }

    @Override
    public boolean add(Draegerman newDraegerman) {
        for(Draegerman existing : draegermen){
            if(existing.equals(newDraegerman)){
                return false;
            }
            if(existing.getLastName().equals(newDraegerman.getLastName())){
                existing.setDisplayName(existing.toString());
                newDraegerman.setDisplayName(newDraegerman.toString());
            }
        }
        draegermen.add(newDraegerman);
        Collections.sort(draegermen);
        saveDraegermanList();
        return true;
    }

    @Override
    public boolean remove(Draegerman draegerman) {
        if(draegermen.remove(draegerman)){
            saveDraegermanList();
            return true;
        }
        return false;
    }

    private boolean saveDraegermanList(){
        try{
            File draegermenFile = new File(context.getFilesDir(), DRAEGERMEN);
            Gson gson = new Gson();
            FileWriter writer = new FileWriter(draegermenFile);
            writer.append(gson.toJson(draegermen));
            writer.flush();
            writer.close();
            return true;
        }catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

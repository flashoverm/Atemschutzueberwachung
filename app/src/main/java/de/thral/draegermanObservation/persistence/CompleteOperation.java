package de.thral.draegermanObservation.persistence;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CompleteOperation implements Comparable<CompleteOperation>{

    private File file;
    private boolean exported;
    private String identifier;

    public CompleteOperation(File file, String identifier){
        this.file = file;
        this.exported = false;
        this.identifier = identifier;
    }

    public File getFile(){
        return file;
    }

    public boolean isExported(){
        return exported;
    }

    public String getIdentifier(){
        return identifier;
    }

    /** Exports completed operation as JSON file to the export folder
     *
     * @param exportFolder Folder where the file should be saved
     * @return true if export succeeds, false if not
     */
    public boolean export(File exportFolder){
        if(copy(file, new File(exportFolder, file.getName()))){
            exported = true;
            return true;
        }
        return false;
    }

    private static boolean copy(File src, File dst){
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                return true;
            }catch(FileNotFoundException e){
                //TargetFile not found
                Log.e("PERSISTENCE", e.getMessage());
            }
        }catch(FileNotFoundException e){
            //SourceFile not found
            Log.e("PERSISTENCE", e.getMessage());

        }catch(IOException e){
            //Other error
            Log.e("PERSISTENCE", e.getMessage());
        }
        return false;
    }

    @Override
    public int compareTo(CompleteOperation completeOperation) {
        return this.getIdentifier().compareTo(completeOperation.getIdentifier());
    }
}

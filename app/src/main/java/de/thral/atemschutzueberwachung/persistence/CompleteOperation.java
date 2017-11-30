package de.thral.atemschutzueberwachung.persistence;

import java.io.File;
import java.io.FileInputStream;
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

    public boolean export(File exportFolder){
        try{
            copy(file, new File(exportFolder, file.getName()));
            exported = true;
            return true;
        } catch(IOException e){
            //TODO handling, inform user
            e.printStackTrace();
        }
        return false;
    }

    public static void copy(File src, File dst) throws IOException {
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }

    @Override
    public int compareTo(CompleteOperation completeOperation) {
        return this.getIdentifier().compareTo(completeOperation.getIdentifier());
    }
}

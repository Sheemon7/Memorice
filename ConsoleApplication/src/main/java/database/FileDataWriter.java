package database;

import model.entities.Entity;

import java.io.*;

/**
 * Created by sheemon on 18.3.16.
 */
public class FileDataWriter implements DataWriter {

    public void writeEntry(Entity entity) {
        String path = directory + File.separator + entity.getName();
        File file = getFile(path);
        try {
            FileOutputStream out = new FileOutputStream(file);
            ObjectOutputStream objectOut = new ObjectOutputStream(out);
            objectOut.writeObject(entity);
            objectOut.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}

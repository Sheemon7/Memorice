package cz.cvut.fel.memorice.model.database;

import java.io.*;
import java.util.logging.Logger;

import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.model.entities.EntityEnum;

/**
 * Created by sheemon on 18.3.16.
 */
public class FileDataWriter implements DataWriter {

    private final static Logger LOGGER = Logger.getLogger(FileDataWriter.class.getName());

    public void writeEntity(Entity entity, EntityEnum type) {
        String path = directory + File.separator + type.getName() + File.separator + entity.getName();
        File file = getFile(path);
        try {
            FileOutputStream out = new FileOutputStream(file);
            ObjectOutputStream objectOut = new ObjectOutputStream(out);
            objectOut.writeObject(entity);
            LOGGER.info(type + " " + entity.getName() + " succesfully saved to database.");
            objectOut.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteEntity(Entity entity, EntityEnum type) {
        String path = directory + File.separator + type.getName() + File.separator + entity.getName();
        File file = getFile(path);
        file.delete();
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

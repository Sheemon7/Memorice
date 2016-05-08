package cz.cvut.fel.memorice.model.database;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.model.entities.EntityEnum;

/**
 * {@inheritDoc}
 * @deprecated
 */
@Deprecated
public class FileDataWriter implements DataWriter {

    private final static Logger LOGGER = Logger.getLogger(FileDataWriter.class.getName());

    /**
     * {@inheritDoc}
     */
    public void writeEntity(Entity entity, EntityEnum type) {
        String path = directory + File.separator + type.getName() + File.separator + entity.getLabel();
        File file = getFile(path);
        try {
            FileOutputStream out = new FileOutputStream(file);
            ObjectOutputStream objectOut = new ObjectOutputStream(out);
            objectOut.writeObject(entity);
            LOGGER.info(type + " " + entity.getLabel() + " succesfully saved to database.");
            objectOut.close();
            out.close();
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "File not found!", e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IO Exception!", e);
        }
    }

    /**
     * Deletes entity from the database
     * @param entity entity to be deleted
     * @param type type of the entity
     */
    public void deleteEntity(Entity entity, EntityEnum type) {
        String path = directory + File.separator + type.getName() + File.separator + entity.getLabel();
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
            LOGGER.log(Level.SEVERE, "IO Exception", e);
        }
        return file;
    }
}

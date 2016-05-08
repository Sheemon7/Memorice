package cz.cvut.fel.memorice.model.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.model.entities.EntityEnum;

/**
 * {@inheritDoc}
 * @deprecated
 */
@Deprecated
public class FileDataReader implements DataReader {
    private static final Logger LOGGER = Logger.getLogger(FileDataReader.class.getName());

    /**
     * {@inheritDoc}
     */
    public Entity readEntity(String name, EntityEnum type) {
        String path = DIRECTORY + File.separator + type.getName() + File.separator + name;
        Object object = null;
        try {
            FileInputStream in = new FileInputStream(path);
            ObjectInputStream objectIn = new ObjectInputStream(in);
            object = objectIn.readObject();
            LOGGER.info(type + " " + name + " succesfully loaded from the database");
            objectIn.close();
            in.close();
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "File not found!", e);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Class not found!", e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IO Exception", e);
        }
        return (Entity) object;
    }
}


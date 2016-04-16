package cz.cvut.fel.memorice.model.database;

import cz.cvut.fel.memorice.model.database.DataReader;
import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.model.entities.EntityEnum;

import java.io.*;
import java.util.logging.Logger;

/**
 * Created by sheemon on 18.3.16.
 */
public class FileDataReader implements DataReader {

    private final static Logger LOGGER = Logger.getLogger(FileDataReader.class.getName());

    public Entity readEntity(String name, EntityEnum type) {
        String path = directory + File.separator + type.getName() + File.separator + name;
        Object object = null;
        try {
            FileInputStream in = new FileInputStream(path);
            ObjectInputStream objectIn = new ObjectInputStream(in);
            object = objectIn.readObject();
            LOGGER.info(type + " " + name + " succesfully loaded from the database");
            objectIn.close();
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (Entity) object;
    }
}


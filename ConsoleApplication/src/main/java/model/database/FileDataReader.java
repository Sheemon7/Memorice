package model.database;

import model.entities.Entity;
import model.entities.EntityEnum;

import java.io.*;

/**
 * Created by sheemon on 18.3.16.
 */
public class FileDataReader implements DataReader {

    public Entity readEntity(String name, EntityEnum type) {
        String path = directory + File.separator + type.getName() + File.separator + name;
        Object object = null;
        try {
            FileInputStream in = new FileInputStream(path);
            ObjectInputStream objectIn = new ObjectInputStream(in);
            object = objectIn.readObject();
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


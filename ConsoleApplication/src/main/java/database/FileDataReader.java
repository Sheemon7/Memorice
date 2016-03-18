package database;

import model.entities.Entity;

import java.io.*;

/**
 * Created by sheemon on 18.3.16.
 */
public class FileDataReader implements DataReader {

    public Entity readEntity(String name) {
        String path = directory + File.separator + name;
        FileInputStream in = null;
        Object object = null;
        try {
            in = new FileInputStream(path);
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


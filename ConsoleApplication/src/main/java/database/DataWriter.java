package database;

import model.entities.Entity;

import java.io.FileNotFoundException;

/**
 * Created by sheemon on 18.3.16.
 */
public interface DataWriter extends DataHandler{


    void writeEntry(Entity entity) throws FileNotFoundException;
}

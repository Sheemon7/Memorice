package model.database;

import model.entities.Entity;
import model.entities.EntityEnum;

import java.io.FileNotFoundException;

/**
 * Created by sheemon on 18.3.16.
 */
public interface DataWriter extends DataHandler {

    void writeEntry(Entity entity, EntityEnum type) throws FileNotFoundException;
}

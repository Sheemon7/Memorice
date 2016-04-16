package cz.cvut.fel.memorice.model.database;


import java.io.FileNotFoundException;

import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.model.entities.EntityEnum;

/**
 * Created by sheemon on 18.3.16.
 */
public interface DataWriter extends DataHandler {

    void writeEntity(Entity entity, EntityEnum type) throws FileNotFoundException;
}

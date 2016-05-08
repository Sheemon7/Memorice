package cz.cvut.fel.memorice.model.database;


import java.io.FileNotFoundException;

import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.model.entities.EntityEnum;

/**
 * Specifies objects that are able to write objects to files
 * @deprecated
 */
@Deprecated
public interface DataWriter extends DataHandler {

    /**
     * Writes entity to file-based database
     * @param entity entity object to be written
     * @param type type of the entity
     * @throws FileNotFoundException
     */
    void writeEntity(Entity entity, EntityEnum type) throws FileNotFoundException;
}

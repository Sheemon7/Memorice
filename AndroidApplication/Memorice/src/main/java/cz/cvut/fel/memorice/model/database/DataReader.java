package cz.cvut.fel.memorice.model.database;


import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.model.entities.EntityEnum;

/**
 * Specifies objects that are able to read objects from files
 * @deprecated
 */
@Deprecated
public interface DataReader extends DataHandler {

    /**
     * Reads entity form file-based database
     * @param name name of the entity/file in the database
     * @param type type of the entity
     * @return read entity
     */
    Entity readEntity(String name, EntityEnum type);

}

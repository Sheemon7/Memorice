package cz.cvut.fel.memorice.model.database;


import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.model.entities.EntityEnum;

/**
 * Created by sheemon on 18.3.16.
 */
public interface DataReader extends DataHandler {

    Entity readEntity(String name, EntityEnum type);

}

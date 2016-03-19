package model.database;

import model.entities.Entity;
import model.entities.EntityEnum;

/**
 * Created by sheemon on 18.3.16.
 */
public interface DataReader extends DataHandler {

    Entity readEntity(String name, EntityEnum type);

}

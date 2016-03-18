package database;

import model.entities.Entity;

/**
 * Created by sheemon on 18.3.16.
 */
public interface DataReader extends DataHandler{

    Entity readEntity(String name);

}

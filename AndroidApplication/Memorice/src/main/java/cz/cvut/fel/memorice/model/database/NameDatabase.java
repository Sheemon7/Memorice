package cz.cvut.fel.memorice.model.database;

import java.io.*;
import java.util.*;
import java.util.Set;
import java.util.logging.Logger;

import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.model.entities.EntityEnum;
import cz.cvut.fel.memorice.model.util.InvalidNameException;
import cz.cvut.fel.memorice.model.util.NameAlreadyUsedException;


/**
 * Class that stores all names that have been used and provides methods for checking
 * name validity.
 * @deprecated
 */
@Deprecated
public class NameDatabase implements Serializable {

    private final static Logger LOGGER = Logger.getLogger(NameDatabase.class.getName());
    private static NameDatabase singleton = new NameDatabase();

    private Map<String, EntityEnum> types = new HashMap<>();
    private Set<String> names = new HashSet<>();

    private NameDatabase() { }

    /**
     * Returns singleton of the class
     * @return singleton
     */
    public static NameDatabase getInstance() {
        return singleton;
    }

    /**
     * Adds name of the passed entity to name database
     * @param entity entity, which name is going to be written
     * @throws NameAlreadyUsedException
     * @throws InvalidNameException
     */
    public void addName(Entity entity) throws NameAlreadyUsedException, InvalidNameException {
        String name = entity.getLabel();
        if (names.contains(name)) {
            throw new NameAlreadyUsedException();
        }
        if (name.equals("")) {
            throw new InvalidNameException();
        }
        names.add(name);
        types.put(name, entity.getType());
    }

    /**
     * Deletes name from the database
     * @param name name
     */
    public void deleteName(String name) {
        new File(DataHandler.directory + File.separator + types.get(name).getName() + File.separator + name).delete();
        names.remove(name);
        types.remove(name);
    }

    /**
     * Returns corresponding type of entity
     * @param entity entity name
     * @return
     */
    public EntityEnum getType(String entity) {
        return types.get(entity);
    }

    /**
     * Returns map of names and types of added entities
     * @return map of names and types
     */
    public Map<String, EntityEnum> getTypes() {
        return types;
    }

    /**
     * Returns set of all used names so far
     * @return set of names
     */
    public Set<String> getNames() {
        return names;
    }

    /**
     * Updates data from files
     */
    public void updateNames() {
        for (EntityEnum type : EntityEnum.values()) {
            updateFolder(type);
        }
    }

    private void updateFolder(EntityEnum type) {
        File folder = new File(DataHandler.directory + File.separator + type.getName());
        if (folder.exists()) {
            for (File file : folder.listFiles()) {
                String name = file.getName();
                if (!this.names.contains(name)) {
                    this.names.add(name);
                    this.types.put(name, type);
                }
            }
        }
    }

}

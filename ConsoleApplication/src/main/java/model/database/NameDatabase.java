package model.database;

import model.entities.Entity;
import model.entities.EntityEnum;
import model.util.InvalidNameException;
import model.util.NameAlreadyUsedException;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;


/**
 * Created by sheemon on 18.3.16.
 */
public class NameDatabase implements Serializable {

    private final static Logger LOGGER = Logger.getLogger(NameDatabase.class.getName());
    private static NameDatabase singleton = new NameDatabase();

    private Map<String, EntityEnum> types = new HashMap<>();
    private Set<String> names = new HashSet<>();

    private NameDatabase() {

    }

    public static NameDatabase getInstance() {
        return singleton;
    }

    public void addName(Entity entity) throws NameAlreadyUsedException, InvalidNameException {
        String name = entity.getName();
        if (names.contains(name)) {
            throw new NameAlreadyUsedException();
        }
        if (name.equals("")) {
            throw new InvalidNameException();
        }
        names.add(name);
        types.put(name, entity.getType());
    }

    public void deleteName(String name) {
        new File(DataHandler.directory + File.separator + types.get(name).getName() + File.separator + name).delete();
        names.remove(name);
        types.remove(name);
    }

    public EntityEnum getType(String entity) {
        return types.get(entity);
    }

    public Map<String, EntityEnum> getTypes() {
        return types;
    }

    public Set<String> getNames() {
        return names;
    }

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

package model.database;

import model.entities.Entity;
import model.entities.EntityEnum;
import model.util.InvalidNameException;
import model.util.NameAlreadyUsedException;

import java.util.*;
import java.util.List;
import java.util.Set;


/**
 * Created by sheemon on 18.3.16.
 */
public class NameDatabase {

    static Map<String, EntityEnum> types = new HashMap<String, EntityEnum>();
    static Set<String> names = new HashSet<String>();
//    TODO File.exists()

    public static void addName(Entity entity) throws NameAlreadyUsedException, InvalidNameException {
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

    public static List<Entity> browseAll(String[] args) {
        List<Entity> ret = new ArrayList<Entity>();
        for (EntityEnum type :
                EntityEnum.values()) {

        }
        return ret;
    }


}

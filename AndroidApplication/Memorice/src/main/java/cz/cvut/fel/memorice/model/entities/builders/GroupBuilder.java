package cz.cvut.fel.memorice.model.entities.builders;

import cz.cvut.fel.memorice.model.database.NameDatabase;
import cz.cvut.fel.memorice.model.entities.Dictionary;
import cz.cvut.fel.memorice.model.entities.Group;
import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.util.ConcurrentBuildingException;
import cz.cvut.fel.memorice.model.util.InvalidNameException;
import cz.cvut.fel.memorice.model.util.NameAlreadyUsedException;
import cz.cvut.fel.memorice.model.util.TermAlreadyUsedException;

/**
 * Created by sheemon on 21.3.16.
 */
public class GroupBuilder extends Builder {

    private static final GroupBuilder instance = new GroupBuilder();

    public static GroupBuilder getInstance() {
        return instance;
    }

    private GroupBuilder() {
    }

    private Group beingBuilt;

    @Override
    public void init(String label) {
        if (beingBuilt != null) {
            throw new ConcurrentBuildingException();
        } else {
            beingBuilt = new Group(label);
        }
    }

    @Override
    public void add(Entry e) {
        //TODO
    }

    @Override
    public Group wrap() {
        Group r = beingBuilt;
        beingBuilt = null;
        return r;
    }
}
package cz.cvut.fel.memorice.model.entities.builders;

import cz.cvut.fel.memorice.model.entities.Group;
import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.util.ConcurrentBuildingException;
import cz.cvut.fel.memorice.model.util.TermAlreadyUsedException;

/**
 * Created by sheemon on 21.3.16.
 */
public class SetBuilder extends Builder {

    private static final SetBuilder instance = new SetBuilder();

    public static SetBuilder getInstance() {
        return instance;
    }

    private SetBuilder() {
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
        try {
            beingBuilt.addEntry(e);
        } catch (TermAlreadyUsedException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public Group wrap() {
        Group r = beingBuilt;
        beingBuilt = null;
        return r;
    }
}
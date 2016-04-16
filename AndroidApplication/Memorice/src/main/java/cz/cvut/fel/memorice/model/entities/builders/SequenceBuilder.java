package cz.cvut.fel.memorice.model.entities.builders;


import cz.cvut.fel.memorice.model.database.NameDatabase;
import cz.cvut.fel.memorice.model.entities.Dictionary;
import cz.cvut.fel.memorice.model.entities.Sequence;
import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.entities.entries.SequenceEntry;
import cz.cvut.fel.memorice.model.util.ConcurrentBuildingException;
import cz.cvut.fel.memorice.model.util.InvalidNameException;
import cz.cvut.fel.memorice.model.util.NameAlreadyUsedException;

/**
 * Created by sheemon on 21.3.16.
 */
public class SequenceBuilder extends Builder {

    private static final SequenceBuilder instance = new SequenceBuilder();

    public static SequenceBuilder getInstance() {
        return instance;
    }

    private SequenceBuilder() {
    }

    private Sequence beingBuilt;

    @Override
    public void init(String label) {
        if (beingBuilt != null) {
            throw new ConcurrentBuildingException();
        } else {
            beingBuilt = new Sequence(label);
        }
    }

    @Override
    public void add(Entry e) {
        //TODO
    }

    @Override
    public Sequence wrap() {
        Sequence r = beingBuilt;
        beingBuilt = null;
        return r;
    }
}


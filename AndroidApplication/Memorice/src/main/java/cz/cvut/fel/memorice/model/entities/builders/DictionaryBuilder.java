package cz.cvut.fel.memorice.model.entities.builders;

import cz.cvut.fel.memorice.model.database.NameDatabase;
import cz.cvut.fel.memorice.model.entities.Dictionary;
import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.model.entities.entries.DictionaryEntry;
import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.util.ConcurrentBuildingException;
import cz.cvut.fel.memorice.model.util.InvalidNameException;
import cz.cvut.fel.memorice.model.util.NameAlreadyUsedException;
import cz.cvut.fel.memorice.model.util.TermAlreadyUsedException;

/**
 * Created by sheemon on 21.3.16.
 */
public class DictionaryBuilder extends Builder {

    private static final DictionaryBuilder instance = new DictionaryBuilder();

    public static DictionaryBuilder getInstance() {
        return instance;
    }

    private DictionaryBuilder() {
    }

    private Dictionary beingBuilt;

    @Override
    public void init(String label) {
        if (beingBuilt != null) {
            throw new ConcurrentBuildingException();
        } else {
            beingBuilt = new Dictionary(label);
        }
    }

    @Override
    public void add(Entry e) {
        //TODO
    }

    @Override
    public Dictionary wrap() {
        Dictionary r = beingBuilt;
        beingBuilt = null;
        return r;











    }
}

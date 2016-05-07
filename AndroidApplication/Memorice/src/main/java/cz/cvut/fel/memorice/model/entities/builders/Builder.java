package cz.cvut.fel.memorice.model.entities.builders;

import java.util.logging.Logger;

import cz.cvut.fel.memorice.model.entities.Dictionary;
import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.model.entities.EntityEnum;
import cz.cvut.fel.memorice.model.entities.Sequence;
import cz.cvut.fel.memorice.model.entities.Set;
import cz.cvut.fel.memorice.model.entities.builders.factories.DictionaryFactory;
import cz.cvut.fel.memorice.model.entities.builders.factories.EntityFactory;
import cz.cvut.fel.memorice.model.entities.builders.factories.SequenceFactory;
import cz.cvut.fel.memorice.model.entities.builders.factories.SetFactory;
import cz.cvut.fel.memorice.model.entities.entries.DictionaryEntry;
import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.entities.entries.SequenceEntry;
import cz.cvut.fel.memorice.model.util.ConcurrentBuildingException;
import cz.cvut.fel.memorice.model.util.TermAlreadyUsedException;

/**
 * Instances of this class serve for easy building of entities. The building process itself is encapsulated
 */
public class Builder {
    private static final Logger LOG = Logger.getLogger(Builder.class.getName());

    private Entity beingBuilt;
    private EntityFactory factory;

    /**
     * Initiates new builder object, setting a factory for further initialization
     * @param factory factory
     */
    public Builder(EntityFactory factory) {
        this.factory = factory;
    }

    /**
     * Returns appropriate builder according to type pass in as parameter
     * @param type type of entity to be built
     * @return correct builder
     */
    public static Builder getCorrectBuilder(EntityEnum type) {
        if (type == EntityEnum.DICTIONARY) {
            return new Builder(DictionaryFactory.getInstance());
        } else if (type == EntityEnum.GROUP) {
            return new Builder(SetFactory.getInstance());
        } else {
            return new Builder(SequenceFactory.getInstance());
        }
    }

    /**
     * Initializes a new building of entity. After this method has been called, another building
     * can not be initialized unless wrap method is called, otherwise, {@link ConcurrentBuildingException
     * } is thrown
     * @param label name
     * @throws ConcurrentBuildingException
     */
    public void init(String label) throws ConcurrentBuildingException {
        if (beingBuilt != null) {
            throw new ConcurrentBuildingException();
        } else {
            beingBuilt = factory.newEntry(label);
        }
    }

    /**
     * Adds new entry to the entity
     * @param entry
     */
    public void add(Entry entry) {
        try {
            beingBuilt.addEntry(entry);
        } catch (TermAlreadyUsedException e) {
            LOG.severe(e.getMessage());
        }
    }

    /**
     * Returns built entity
     * @return entity
     */
    public Entity wrap() {
        Entity ret = beingBuilt;
        beingBuilt = null;
        return ret;
    }

}

package cz.cvut.fel.memorice.model.entities.builders;

import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.model.entities.EntityEnum;
import cz.cvut.fel.memorice.model.entities.builders.factories.DictionaryFactory;
import cz.cvut.fel.memorice.model.entities.builders.factories.EntityFactory;
import cz.cvut.fel.memorice.model.entities.builders.factories.SequenceFactory;
import cz.cvut.fel.memorice.model.entities.builders.factories.SetFactory;
import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.util.ConcurrentBuildingException;
import cz.cvut.fel.memorice.model.util.DefinitionAlreadyUsedException;

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
     * Returns appropriate builder according to type passed in as parameter
     * @param type type of entity to be built
     * @return correct builder
     */
    public static Builder getCorrectBuilder(EntityEnum type) {
        switch (type) {
            case DICTIONARY:
                return new Builder(DictionaryFactory.getInstance());
            case SET:
                return new Builder(SetFactory.getInstance());
            default:
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
        } catch (DefinitionAlreadyUsedException e) {
            LOG.log(Level.SEVERE, "Concurrent Building!", e);
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

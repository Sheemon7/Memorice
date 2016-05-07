package cz.cvut.fel.memorice.model.entities.builders.factories;

import cz.cvut.fel.memorice.model.entities.Dictionary;

/**
 * {@inheritDoc}
 */
public class DictionaryFactory implements EntityFactory {

    private static final DictionaryFactory singleton = new DictionaryFactory();

    private DictionaryFactory() {

    }

    /**
     * Returns instance of this class, which represents singleton design pattern
     *
     * @return singleton
     */
    public static EntityFactory getInstance() {
        return singleton;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dictionary newEntry(String label) {
        return new Dictionary(label);
    }
}

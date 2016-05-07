package cz.cvut.fel.memorice.model.entities.builders.factories;

import cz.cvut.fel.memorice.model.entities.Set;

/**
 * {@inheritDoc}
 */

public class SetFactory implements EntityFactory {

    private static final SetFactory singleton = new SetFactory();

    private SetFactory() {

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
    public Set newEntry(String label) {
        return new Set(label);
    }
}

package cz.cvut.fel.memorice.model.entities.builders.factories;

import cz.cvut.fel.memorice.model.entities.Sequence;

/**
 * {@inheritDoc}
 */
public class SequenceFactory implements EntityFactory {

    private static final SequenceFactory singleton = new SequenceFactory();

    private SequenceFactory() {

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
    public Sequence newEntry(String label) {
        return new Sequence(label);
    }
}

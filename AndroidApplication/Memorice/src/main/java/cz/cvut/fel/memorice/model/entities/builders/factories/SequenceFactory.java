package cz.cvut.fel.memorice.model.entities.builders.factories;

import cz.cvut.fel.memorice.model.entities.Sequence;

public class SequenceFactory implements EntityFactory {

    private static final SequenceFactory singleton = new SequenceFactory();

    private SequenceFactory() {

    }

    public static EntityFactory getInstance() {
        return singleton;
    }

    @Override
    public Sequence newEntry(String label) {
        return new Sequence(label);
    }
}

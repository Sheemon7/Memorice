package cz.cvut.fel.memorice.model.entities.builders.factories;

import cz.cvut.fel.memorice.model.entities.Set;

public class SetFactory implements EntityFactory {

    private static final SetFactory singleton = new SetFactory();

    private SetFactory() {

    }

    public static EntityFactory getInstance() {
        return singleton;
    }

    @Override
    public Set newEntry(String label) {
        return new Set(label);
    }
}

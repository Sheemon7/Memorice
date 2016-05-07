package cz.cvut.fel.memorice.model.entities.builders.factories;

import cz.cvut.fel.memorice.model.entities.Dictionary;

public class DictionaryFactory implements EntityFactory {

    private static final DictionaryFactory singleton = new DictionaryFactory();

    private DictionaryFactory() {

    }

    public static EntityFactory getInstance() {
        return singleton;
    }

    @Override
    public Dictionary newEntry(String label) {
        return new Dictionary(label);
    }
}

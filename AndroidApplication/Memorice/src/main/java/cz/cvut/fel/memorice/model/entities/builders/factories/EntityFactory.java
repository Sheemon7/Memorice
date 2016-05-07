package cz.cvut.fel.memorice.model.entities.builders.factories;

import cz.cvut.fel.memorice.model.entities.Entity;

public interface EntityFactory {

    Entity newEntry(String label);
}

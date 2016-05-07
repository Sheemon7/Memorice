package cz.cvut.fel.memorice.model.entities.builders.factories;

import cz.cvut.fel.memorice.model.entities.Entity;

/**
 * Instances of entity factory serves for providing builders with {@link Entity} objects
 */
public interface EntityFactory {

    /**
     * Returns new Entity object
     *
     * @param label label of the new entity
     * @return new entity
     */
    Entity newEntry(String label);
}

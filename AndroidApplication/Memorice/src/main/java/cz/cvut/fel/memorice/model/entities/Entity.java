package cz.cvut.fel.memorice.model.entities;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.util.TermAlreadyUsedException;

/**
 * Entity abstract class defines attributes and characteristics of each entity in the program
 */
public abstract class Entity implements Serializable, Iterable {

    protected boolean favourite;
    protected String label;

    /**
     * Initiates new Entity instance with label passed as parameter
     *
     * @param label new entity label
     */
    public Entity(String label) {
        this.label = label;
    }

    /**
     * Returns size of the entity, in other words how many entries this entity holds
     *
     * @return size of the entity
     */
    public abstract int size();

    /**
     * Adds new entry to the entity, throws {@link TermAlreadyUsedException} if the entry with
     * the same value is already present
     *
     * @param entry entry to be added
     * @throws TermAlreadyUsedException
     */
    public abstract void addEntry(Entry entry) throws TermAlreadyUsedException;

    /**
     * Deletes entry from the entity
     * @param entry entry to be deleted
     */
    public abstract void deleteEntry(Entry entry);

    /**
     * Returns appropriate iterator capable of iterating through entries in the entity
     *
     * @return iterator
     */
    public abstract Iterator<? extends Entry> iterator();

    /**
     * Returns {@link List} of entries present in the entity
     *
     * @return list of entries
     */
    public abstract List<? extends Entry> getListOfEntries();

    /**
     * Return whether this entity is user's favourite
     *
     * @return true if entity is user's favourite else false
     */
    public boolean isFavourite() {
        return favourite;
    }

    /**
     * Set favourite property of entity to the value passed in parameters
     *
     * @param favourite new property favourite attribute
     */
    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    /**
     * Returns the label of the entity
     *
     * @return label of the entity
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets new label to the entity
     * @param label new label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Returns type of the entity
     *
     * @return type of the entity
     */
    public abstract EntityEnum getType();


    /* equals and hashcode methods are required for proper functionality of the application */

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract boolean equals(Object o);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract int hashCode();
}

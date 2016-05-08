package cz.cvut.fel.memorice.model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.util.TermAlreadyUsedException;

/**
 * Instances of this class represent Set datasets in the application
 */
public class Set extends Entity implements Serializable {

    private java.util.Set<Entry> entries = new LinkedHashSet<>();

    /**
     * Initiates new Set entity with label passed as parameter
     *
     * @param label label of the new entity
     */
    public Set(String label) {
        super(label);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityEnum getType() {
        return EntityEnum.SET;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEntry(Entry entry) throws TermAlreadyUsedException {
        if (!entries.contains(entry)) {
            entries.add(entry);
        } else {
            throw new TermAlreadyUsedException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEntry(Entry entry) {
        entries.remove(entry);
    }

    /**
     * {@inheritDoc}
     */
    public boolean removeEntry(Entry entry) {
        return entries.remove(entry);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return entries.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Entry> iterator() {
        return entries.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Entry> getListOfEntries() {
        return new ArrayList<>(entries);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Set that = (Set) o;
        return this.getLabel().equals(that.getLabel()) && !(entries != null ? !entries.equals(that.entries) : that.entries != null);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return entries != null ? entries.hashCode() : 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Set: " + getLabel() +
                "\nentries= ");
        for (Entry entry :
                entries) {
            builder.append(entry.toString()).append("\n");
        }
        return builder.toString();
    }
}

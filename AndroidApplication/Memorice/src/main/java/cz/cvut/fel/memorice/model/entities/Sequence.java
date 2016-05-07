package cz.cvut.fel.memorice.model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.entities.entries.SequenceEntry;

/**
 * Instances of this class represent Sequence datasets in the application
 */
public class Sequence extends Entity implements Serializable {

    private List<SequenceEntry> entries = new ArrayList<>();

    /**
     * Initiates new Sequence entity with label passed as parameter
     *
     * @param label label of the new entity
     */
    public Sequence(String label) {
        super(label);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityEnum getType() {
        return EntityEnum.SEQUENCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEntry(Entry entry) {
        entries.add((SequenceEntry) entry);
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
    @Override
    public int size() {
        return entries.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<SequenceEntry> iterator() {
        return entries.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SequenceEntry> getListOfEntries() {
        return entries;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sequence that = (Sequence) o;
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
        StringBuilder builder = new StringBuilder("Sequence: " + getLabel() +
                "\nentries= ");
        for (SequenceEntry entry :
                entries) {
            builder.append(entry.toString()).append("\n");
        }
        return builder.toString();
    }
}

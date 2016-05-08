package cz.cvut.fel.memorice.model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cz.cvut.fel.memorice.model.entities.entries.DictionaryEntry;
import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.util.DefinitionAlreadyUsedException;

/**
 * Instances of this class represent Dictionary datasets in the application
 */
public class Dictionary extends Entity implements Serializable {

    private Set<DictionaryEntry> entries = new LinkedHashSet<>();

    /**
     * Initiates new Dictionary entity with label passed as parameter
     *
     * @param label label of the new entity
     */
    public Dictionary(String label) {
        super(label);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityEnum getType() {
        return EntityEnum.DICTIONARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEntry(Entry entry) throws DefinitionAlreadyUsedException {
        if (!entries.contains(entry)) {
            entries.add((DictionaryEntry) entry);
        } else {
            throw new DefinitionAlreadyUsedException();
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
    @Override
    public int size() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<DictionaryEntry> iterator() {
        return entries.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DictionaryEntry> getListOfEntries() {
        return new ArrayList<>(entries);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dictionary that = (Dictionary) o;
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
        StringBuilder builder = new StringBuilder("Dictionary: " + getLabel() +
                "\nentries= ");
        for (DictionaryEntry entry :
                entries) {
            builder.append(entry.toString()).append("\n");
        }
        return builder.toString();
    }
}

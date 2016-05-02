package cz.cvut.fel.memorice.model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cz.cvut.fel.memorice.model.entities.entries.DictionaryEntry;
import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.util.TermAlreadyUsedException;

/**
 * Created by sheemon on 18.3.16.
 */
public class Dictionary extends Entity implements Serializable {

    private Set<DictionaryEntry> entries = new HashSet<>();

    public Dictionary(String name) {
        super(name);
    }

    @Override
    public EntityEnum getType() {
        return EntityEnum.DICTIONARY;
    }

    public boolean addEntry(DictionaryEntry entry) throws TermAlreadyUsedException {
        if (!entries.contains(entry)) {
            return entries.add(entry);
        } else {
            throw new TermAlreadyUsedException();
        }
    }

    public boolean removeEntry(DictionaryEntry entry) {
        return entries.remove(entry);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Iterator iterator() {
        return entries.iterator();
    }

    @Override
    public List<DictionaryEntry> getListOfEntries() {
        return new ArrayList<DictionaryEntry>(entries);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dictionary that = (Dictionary) o;

        return !(entries != null ? !entries.equals(that.entries) : that.entries != null);

    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Dictionary: " + getName() +
                "\nentries= ");
        for (DictionaryEntry entry:
                entries) {
            builder.append(entry.toString() + "\n");
        }
        return builder.toString();
    }
}

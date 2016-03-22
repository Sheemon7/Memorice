package model.entities;

import model.entities.entries.DictionaryEntry;
import model.entities.entries.SequenceEntry;
import model.util.TermAlreadyUsedException;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
        return null;
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

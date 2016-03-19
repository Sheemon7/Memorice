package model.entities;

import model.entities.entries.DictionaryEntry;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by sheemon on 18.3.16.
 */
public class Dictionary extends Entity {

    private Set<DictionaryEntry> entries = new HashSet<>();

    public Dictionary(String name) {
        super(name);
    }

    public boolean addEntry(DictionaryEntry entry) {
        return entries.add(entry);
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
}

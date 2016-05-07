package cz.cvut.fel.memorice.model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.entities.entries.SequenceEntry;

/**
 * Created by sheemon on 18.3.16.
 */
public class Sequence extends Entity implements Serializable {

    private List<SequenceEntry> entries = new ArrayList<>();

    public Sequence(String name) {
        super(name);
    }

    @Override
    public EntityEnum getType() {
        return EntityEnum.SEQUENCE;
    }

    @Override
    public void addEntry(Entry entry) {
        entries.add((SequenceEntry)entry);
    }

    @Override
    public int size() {
        return entries.size();
    }

    @Override
    public Iterator iterator() {
        return entries.iterator();
    }

    @Override
    public List<SequenceEntry> getListOfEntries() {
        return entries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sequence that = (Sequence) o;
        return this.getName().equals(that.getName()) && !(entries != null ? !entries.equals(that.entries) : that.entries != null);

    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Sequence: " + getName() +
                "\nentries= ");
        for (SequenceEntry entry:
             entries) {
            builder.append(entry.toString() + "\n");
        }
        return builder.toString();
    }
}

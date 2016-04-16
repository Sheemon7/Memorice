package cz.cvut.fel.memorice.model.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.util.TermAlreadyUsedException;

/**
 * Created by sheemon on 18.3.16.
 */
public class Group extends Entity implements Serializable {

    private Set<Entry> entries = new HashSet<>();

    public Group(String name) {
        super(name);
    }

    @Override
    public EntityEnum getType() {
        return EntityEnum.GROUP;
    }

    public boolean addEntry(Entry entry) throws TermAlreadyUsedException {
        if (!entries.contains(entry)) {
            return entries.add(entry);
        } else {
            throw new TermAlreadyUsedException();
        }
    }

    public boolean removeEntry(Entry entry) {
        return entries.remove(entry);
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
    public String toString() {
        StringBuilder builder = new StringBuilder("Group: " + getName() +
                "\nentries= ");
        for (Entry entry:
                entries) {
            builder.append(entry.toString()+ "\n");
        }
        return builder.toString();
    }
}

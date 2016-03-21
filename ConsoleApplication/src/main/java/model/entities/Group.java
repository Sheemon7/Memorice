package model.entities;

import model.entities.entries.GroupEntry;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by sheemon on 18.3.16.
 */
public class Group extends Entity implements Serializable {

    private Set<GroupEntry> entries = new HashSet<>();

    public Group(String name) {
        super(name);
    }

    public boolean addEntry(GroupEntry entry) {
        return entries.add(entry);
    }

    public boolean removeEntry(GroupEntry entry) {
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
}

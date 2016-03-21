package model.entities;

import model.entities.entries.SequenceEntry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sheemon on 18.3.16.
 */
public class Sequence extends Entity implements Serializable {

    private List<SequenceEntry> entries= new ArrayList<SequenceEntry>();

    public Sequence(String name) {
        super(name);
    }

    public boolean addEntry(SequenceEntry entry) {
        return entries.add(entry);
    }

    public boolean removeEntry(SequenceEntry entry) {
        return entries.remove(entry);
        //soupnout cisla? v builderu nebo tady? dulezite
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
        StringBuilder builder = new StringBuilder("Sequence: " + getName() +
                " entries= ");
        for (SequenceEntry entry:
             entries) {
            builder.append(entry.toString());
        }
        return builder.toString();
    }
}

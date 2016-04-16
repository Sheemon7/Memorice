package cz.cvut.fel.memorice.model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.entities.entries.SequenceEntry;
import cz.cvut.fel.memorice.model.util.TermAlreadyUsedException;

/**
 * Created by sheemon on 18.3.16.
 */
public class Sequence extends Entity implements Serializable {

    private List<SequenceEntry> entries= new ArrayList<SequenceEntry>();

    public Sequence(String name) {
        super(name);
    }

    @Override
    public EntityEnum getType() {
        return EntityEnum.SEQUENCE;
    }

//    @Override
    public boolean addEntry(SequenceEntry entry) {
        return entries.add(entry);
    }

    public boolean removeEntry(SequenceEntry entry) {
        return entries.remove(entry);
        //soupnout cisla? v builderu nebo tady? dulezite, asi podle toho, jak budou fungovat databaze v Androidu
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
                "\nentries= ");
        for (SequenceEntry entry:
             entries) {
            builder.append(entry.toString() + "\n");
        }
        return builder.toString();
    }
}

package cz.cvut.fel.memorice.model.entities;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.util.TermAlreadyUsedException;

/**
 * Created by sheemon on 18.3.16.
 */
public abstract class Entity implements Serializable, Iterable {

    private boolean favourite;

    public boolean isFavourite() {
        return favourite;
    }


    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    private String name;

    public Entity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

//    public abstract <T extends Entry> boolean addEntry(Class<T> entry);

    public abstract EntityEnum getType();

    public abstract int size();

    public abstract Iterator iterator();

    public abstract List<? extends Entry> getListOfEntries();

}

package model.entities;

import model.entities.entries.ListEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sheemon on 18.3.16.
 */
public class List extends Entity {

    private java.util.List<ListEntry> data = new ArrayList<ListEntry>();

    public List(String name) {
        super(name);
    }

    @Override
    public int size() {
        return data.size();
    }
}

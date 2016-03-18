package model.entities;

import model.entities.Entity;

/**
 * Created by sheemon on 18.3.16.
 */
public class Set extends Entity {
    public Set(String name) {
        super(name);
    }

    @Override
    public int size() {
        return 0;
    }
}

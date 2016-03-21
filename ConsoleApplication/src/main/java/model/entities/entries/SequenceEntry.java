package model.entities.entries;

import java.io.Serializable;

/**
 * Created by sheemon on 18.3.16.
 */
public class SequenceEntry extends GroupEntry implements Serializable {

    private final int number;

    public SequenceEntry(String key, int number) {
        super(key);
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}

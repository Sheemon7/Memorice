package model.entities.entries;

import java.io.Serializable;

/**
 * Created by sheemon on 18.3.16.
 */
public class DictionaryEntry extends GroupEntry implements Serializable{

    private final String key;

    public DictionaryEntry(String key, String value) {
        super(value);
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}

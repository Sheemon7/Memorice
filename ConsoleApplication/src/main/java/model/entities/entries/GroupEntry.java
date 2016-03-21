package model.entities.entries;

import java.io.Serializable;

/**
 * Created by sheemon on 18.3.16.
 */
public class GroupEntry implements Serializable {

    private final String value;

    public GroupEntry(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}

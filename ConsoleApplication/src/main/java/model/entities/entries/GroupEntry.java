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

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupEntry that = (GroupEntry) o;
        return value != null ? value.equals(that.value) : that.value == null;

    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}

package cz.cvut.fel.memorice.model.entities.entries;

import java.io.Serializable;

/**
 * Entry objects represent each piece of data in entities
 */
public class Entry implements Serializable {

    protected String value;

    /**
     * Returns entry with value passed as parameter
     *
     * @param value value
     */
    public Entry(String value) {
        this.value = value;
    }

    /**
     * Returns value of the entity
     *
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the string value of this entry
     *
     * @param value value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Entry that = (Entry) o;
        return value != null ? value.equals(that.value) : that.value == null;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}

package cz.cvut.fel.memorice.model.entities.entries;

import java.io.Serializable;

/**
 * Sequence Entry objects represent each piece of data in sequence entities
 */
public class SequenceEntry extends Entry implements Serializable {

    private final int number;

    /**
     * Returns new entry with value and its number specified
     * @param value value of the new entry
     * @param number order of the new entry
     */
    public SequenceEntry(String value, int number) {
        super(value);
        this.number = number;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return number + ": " + super.toString();
    }

    /**
     * Returns number of this entry
     * @return number of entry
     */
    public int getNumber() {
        return number;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SequenceEntry that = (SequenceEntry) o;

        return number == that.number;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + number;
        return result;
    }
}

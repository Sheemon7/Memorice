package cz.cvut.fel.memorice.model.entities.entries;

import java.io.Serializable;

/**
 * DictionaryEntry objects represent each piece of data in Dictionary entities
 */
public class DictionaryEntry extends Entry implements Serializable{

    private final String definition;

    /**
     * Returns new instance of Dictionary Entry, with value and definition specified
     * @param definition definition of the new instance
     * @param value value of the new instance
     */
    public DictionaryEntry(String definition, String value) {
        super(value);
        this.definition = definition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return super.toString() + ": " + definition;
    }

    /**
     * Returns the definition of the entry
     * @return definition of the entry
     */
    public String getDefinition() {
        return definition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        DictionaryEntry that = (DictionaryEntry) o;

        return !(definition != null ? !definition.equals(that.definition) : that.definition != null);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (definition != null ? definition.hashCode() : 0);
        return result;
    }
}

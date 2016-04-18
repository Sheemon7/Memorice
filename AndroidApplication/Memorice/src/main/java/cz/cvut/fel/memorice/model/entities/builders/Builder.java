package cz.cvut.fel.memorice.model.entities.builders;

import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.model.entities.EntityEnum;
import cz.cvut.fel.memorice.model.entities.entries.Entry;

/**
 * Created by sheemon on 21.3.16.
 */
public abstract class Builder {

    public static Builder getCorrectBuilder(EntityEnum type) {
        if (type == EntityEnum.DICTIONARY) {
            return DictionaryBuilder.getInstance();
        } else if (type == EntityEnum.GROUP) {
            return SetBuilder.getInstance();
        } else {
            return SequenceBuilder.getInstance();
        }
    }

    public abstract void init(String label);
    public abstract void add(Entry e);
    public abstract Entity wrap();

}

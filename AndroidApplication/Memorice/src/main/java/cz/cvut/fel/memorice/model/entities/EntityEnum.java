package cz.cvut.fel.memorice.model.entities;

/**
 * Created by sheemon on 19.3.16.
 */
public enum EntityEnum {
    DICTIONARY("dictionary"),
    SEQUENCE("sequence"),
    GROUP("group");

    EntityEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private final String name;


}

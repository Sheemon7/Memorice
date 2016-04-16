package cz.cvut.fel.memorice.model.entities;

/**
 * Created by sheemon on 19.3.16.
 */
public enum EntityEnum {
    DICTIONARY("dictionary"),
    SEQUENCE("sequence"),
    GROUP("group");

    public static EntityEnum getType(String typeString) {
        switch (typeString) {
            case "dictionary":
                return DICTIONARY;
            case "sequence":
                return SEQUENCE;
            case "group":
                return GROUP;
        }
        return null;
    }

    EntityEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private final String name;


}

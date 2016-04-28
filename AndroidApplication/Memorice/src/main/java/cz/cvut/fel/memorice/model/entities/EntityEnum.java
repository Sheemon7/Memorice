package cz.cvut.fel.memorice.model.entities;

/**
 * Created by sheemon on 19.3.16.
 */
public enum EntityEnum {
    DICTIONARY("Dictionary"),
    SEQUENCE("Sequence"),
    GROUP("Set");

    public static EntityEnum getType(String typeString) {
        switch (typeString.toLowerCase()) {
            case "dictionary":
                return DICTIONARY;
            case "sequence":
                return SEQUENCE;
            default:
                return GROUP;
        }
    }

    EntityEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private final String name;


}

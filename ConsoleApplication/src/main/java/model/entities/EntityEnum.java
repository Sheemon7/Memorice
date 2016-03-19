package model.entities;

/**
 * Created by sheemon on 19.3.16.
 */
public enum EntityEnum {
    DICTIONARY("dictionary"),
    DOUBLEDICTIONARY("double dictionary"),
    LIST("list"),
    SET("set");

    EntityEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private final String name;


}

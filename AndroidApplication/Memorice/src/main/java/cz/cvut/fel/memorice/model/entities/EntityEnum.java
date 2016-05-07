package cz.cvut.fel.memorice.model.entities;

/**
 * Entity enumeration type enumerates all possible entities that can be present in an application
 */
public enum EntityEnum {
    DICTIONARY("Dictionary"),
    SEQUENCE("Sequence"),
    SET("Set");

    private final String name;

    /**
     * Returns correct type according to its string representation
     * @param typeString string representation of type
     * @return correct type
     */
    public static EntityEnum getType(String typeString) {
        switch (typeString.toLowerCase()) {
            case "dictionary":
                return DICTIONARY;
            case "sequence":
                return SEQUENCE;
            default:
                return SET;
        }
    }

    /**
     * COnstructs new enum
     * @param name name of the enum
     */
    EntityEnum(String name) {
        this.name = name;
    }

    /**
     * Returns name of the enum
     * @return name
     */
    public String getName() {
        return name;
    }



}

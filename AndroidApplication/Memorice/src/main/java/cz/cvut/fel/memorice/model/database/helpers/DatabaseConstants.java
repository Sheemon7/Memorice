package cz.cvut.fel.memorice.model.database.helpers;

public final class DatabaseConstants {

    private DatabaseConstants() {
        
    }

    public static final String TABLE_ENTITIES = "entities";
    public static final String KEY_LABEL = "label";
    public static final String KEY_TYPE = "type";
    public static final String KEY_FAVORITE = "favorite";
    public static final String[] ENTITIES_COLUMNS = {KEY_LABEL, KEY_TYPE, KEY_FAVORITE};

    public static final String TABLE_ENTRIES_SEQUENCES = "seqs";
    public static final String KEY_NUMBER = "num";
    public static final String KEY_VALUE = "entry";
    public static final String KEY_ENTITY = "entity";

    public static final String TABLE_ENTRIES_DICTS = "dicts";
    public static final String KEY_PASS = "pass";

    public static final String TABLE_ENTRIES_SETS = "grps";
}

package cz.cvut.fel.memorice.model.database.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.model.entities.EntityEnum;
import cz.cvut.fel.memorice.model.entities.builders.Builder;
import cz.cvut.fel.memorice.model.entities.entries.DictionaryEntry;
import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.entities.entries.SequenceEntry;
import cz.cvut.fel.memorice.model.util.WrongNameException;

/**
 * This class provides database access, and conducts database access operations
 * According to this article, I use only one instance of database helper to avoid collisions
 * <a href="//http://blog.foxxtrot.net/2009/01/a-sqliteopenhelper-is-not-a-sqlitetablehelper.html">
 * SQLiteHelper is not a SQLiteTableHelper</a>
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    private static final Logger LOG = Logger.getLogger(SQLiteHelper.class.getName());

    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "MemoriceDB_5";

    private static final String TABLE_ENTITIES = DatabaseConstants.TABLE_ENTITIES;
    private static final String KEY_LABEL = DatabaseConstants.KEY_LABEL;
    private static final String KEY_TYPE = DatabaseConstants.KEY_TYPE;
    private static final String KEY_FAVORITE = DatabaseConstants.KEY_FAVORITE;
    private static final String[] ENTITIES_COLUMNS = DatabaseConstants.ENTITIES_COLUMNS;

    private static final String TABLE_ENTRIES_SEQUENCES = DatabaseConstants.TABLE_ENTRIES_SEQUENCES;
    private static final String KEY_NUMBER = DatabaseConstants.KEY_NUMBER;
    private static final String KEY_VALUE = DatabaseConstants.KEY_VALUE;
    private static final String KEY_ENTITY = DatabaseConstants.KEY_ENTITY;

    private static final String TABLE_ENTRIES_DICTS = DatabaseConstants.TABLE_ENTRIES_DICTS;
    private static final String KEY_PASS = DatabaseConstants.KEY_PASS;

    private static final String TABLE_ENTRIES_SETS = DatabaseConstants.TABLE_ENTRIES_SETS;

    private SQLiteEntryTableHelper sqLiteEntryTableHelper = new SQLiteEntryTableHelper(this);

    /**
     * {@inheritDoc}
     *
     * @param context
     */
    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Creates a new entity tables and three entries tables
     *
     * @param db writable database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        LOG.info("Creating database " + DATABASE_NAME + " , version: " + DATABASE_VERSION);
        createEntitiesTable(db);
        createTableDictionaries(db);
        createTableSequences(db);
        createTableSets(db);
    }

    /**
     * Updates existing database
     *
     * @param db         database
     * @param oldVersion oldVersion number
     * @param newVersion newVersion number
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LOG.info("Updating database");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTITIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES_SEQUENCES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES_SETS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES_DICTS);
        this.onCreate(db);
    }

    /**
     * Adds entity and its entries to the database
     *
     * @param entity entity to be added
     */
    public void addEntity(Entity entity) {
        LOG.info("Adding entity: " + entity);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LABEL, entity.getLabel());
        values.put(KEY_TYPE, entity.getType().toString());
        values.put(KEY_FAVORITE, entity.isFavourite() ? 1 : 0);
        db.insert(TABLE_ENTITIES, null, values);
        db.close();

        if (entity.getType() == EntityEnum.SEQUENCE) {
            for (Entry entry : entity.getListOfEntries()) {
                sqLiteEntryTableHelper.addSequenceEntry((SequenceEntry)entry, entity);
            }
        } else if (entity.getType() == EntityEnum.DICTIONARY) {
            for (Entry entry : entity.getListOfEntries()) {
                sqLiteEntryTableHelper.addDictEntry((DictionaryEntry) entry, entity);
            }
        } else {
            for (Entry entry : entity.getListOfEntries()) {
                sqLiteEntryTableHelper.addSetEntry(entry, entity);
            }

        }
    }

    /**
     * Renames the entity in the database
     *
     * @param oldLabel old label of the entity
     * @param entity entity object
     */
    public void renameEntity(String oldLabel, Entity entity) {
        LOG.info("Renaming entity " + oldLabel);
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE " + TABLE_ENTITIES + " SET " + KEY_LABEL + "=\"" + entity.getLabel() +
                "\" WHERE " + KEY_LABEL + " = " + "\""
                + oldLabel + "\"";
        db.execSQL(query);
        String table;
        switch (entity.getType()) {
            case DICTIONARY:
                table = TABLE_ENTRIES_DICTS;
                break;
            case SEQUENCE:
                table = TABLE_ENTRIES_SEQUENCES;
                break;
            default:
                table = TABLE_ENTRIES_SETS;
                break;

        }
        query = "UPDATE " + table + " SET " + KEY_ENTITY + "=\"" + entity.getLabel() +
                    "\" WHERE " + KEY_ENTITY + " = " + "\""
                    + oldLabel + "\"";
            db.execSQL(query);
        db.close();
    }

    /**
     * Returns all entity's entries in the database
     *
     * @param label name of the entity
     * @return list of all entity's entries
     */
    public Entity getEntity(String label) {
        LOG.info("Getting entity: " + label);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ENTITIES, ENTITIES_COLUMNS,
                " label = ? ", new String[]{label}, null, null, null, null);

        List<? extends Entry> entries;
        if (cursor != null && cursor.moveToFirst()) {
            EntityEnum type = EntityEnum.getType(cursor.getString(1));
            Builder b = Builder.getCorrectBuilder(type);
            b.init(label);
            entries = getEntityEntries(label, type);
            for (Entry e : entries) {
                b.add(e);
            }
            Entity entity = b.wrap();
            try {
                entity.setFavourite(isEntityFavourite(entity.getLabel()));
            } catch (WrongNameException e) {
                LOG.log(Level.INFO, "Wrong name", e);
            }
            cursor.close();
            return entity;
        } else {
            return null;
        }
    }

    /**
     * Sets entity favourite if it is currently not and vice versa
     *
     * @param entity entity to be toggled
     */
    public void toggleFavourite(Entity entity) {
        LOG.info("Toggling favourite at " + entity.getLabel());
        entity.setFavourite(!entity.isFavourite());
        String query = "UPDATE " + TABLE_ENTITIES + " SET " + KEY_FAVORITE + "=" +
                (entity.isFavourite() ? 1 : 0) +
                " WHERE " + KEY_LABEL + "=" + "\'" + entity.getLabel() + "\'";
        executeQuery(query);
    }

    /**
     * Deletes the whole entity from the database
     *
     * @param entity entity to be deleted
     */
    public void deleteEntity(Entity entity) {
        LOG.info("Deleting entity: " + entity.getLabel());
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ENTITIES, KEY_LABEL + " =?", new String[]{entity.getLabel()});
        sqLiteEntryTableHelper.deleteAllEntityEntries(entity);
        db.close();
    }

    /**
     * Returns the favourite state of the entity taken from database
     *
     * @param label label of referred entity
     * @return true if entity is favourite else false
     * @throws WrongNameException
     */
    public boolean isEntityFavourite(String label) throws WrongNameException {
        LOG.info("Verifying favourite at " + label);
        String query = "SELECT " + KEY_FAVORITE + " FROM " + TABLE_ENTITIES +
                " WHERE " + KEY_LABEL + "=" + "\'" + label + "\'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            boolean ret = 1 == Integer.parseInt(cursor.getString(0));
            cursor.close();
            return ret;
        } else {
            throw new WrongNameException("Name does not exist in the database");
        }
    }

    /**
     * Returns list of all entities in the database
     *
     * @return list of all entities in the database
     */
    public ArrayList<Entity> getAllEntities() {
        LOG.info("Getting all entities:");
        ArrayList<Entity> ret = new ArrayList<>();

        String query = "SELECT " + KEY_LABEL + " FROM " + TABLE_ENTITIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String label = cursor.getString(0);
                ret.add(getEntity(label));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return ret;
    }

    /**
     * Returns list of all favourite entities in the database
     *
     * @return list of all favourite entities in the database
     */
    public ArrayList<Entity> getAllFavouriteEntities() {
        LOG.info("Getting all favourite entities:");
        ArrayList<Entity> ret = new ArrayList<>();

        String query = "SELECT " + KEY_LABEL + " FROM " + TABLE_ENTITIES +
                " WHERE " + KEY_FAVORITE + "=1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String label = cursor.getString(0);
                ret.add(getEntity(label));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return ret;
    }

    /**
     * Returns list of all entities in the database that comprise with
     * the showFiltered string
     *
     * @return list of all entities in the database that comprises
     */
    public ArrayList<Entity> getAllEntitiesFiltered(String filter) {
        LOG.info("Getting all entities with filter: " + filter);
        ArrayList<Entity> ret = new ArrayList<>();
        String query = "SELECT " + KEY_LABEL + " FROM " + TABLE_ENTITIES +
                " WHERE " + KEY_LABEL + " LIKE " + "\"%" + filter + "%\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ret.add(getEntity(cursor.getString(0)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return ret;
    }

    /**
     * Returns list of all favourite entities in the database that comprise with
     * the showFiltered string
     *
     * @return list of all favourite entities in the database that comprises
     */
    public ArrayList<Entity> getAllFavouriteEntitiesFiltered(String filter) {
        LOG.info("Getting all favourite and filtered entities:");
        ArrayList<Entity> ret = new ArrayList<>();

        String query = "SELECT " + KEY_LABEL + " FROM " + TABLE_ENTITIES +
                " WHERE " + KEY_FAVORITE + "=1 AND "
                + KEY_LABEL + " LIKE " + "\"%" + filter + "%\"";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String label = cursor.getString(0);
                ret.add(getEntity(label));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return ret;
    }

    /**
     * Returns list of all entities' names in the database
     *
     * @return list of all entities' names in the database
     */
    public ArrayList<String> getAllLabels() {
        LOG.info("Getting all possible labels");
        SQLiteDatabase db = this.getReadableDatabase();
        String querry = "SELECT " + KEY_LABEL + " FROM " + TABLE_ENTITIES;
        Cursor cursor = db.rawQuery(querry, null);

        ArrayList<String> ret = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                ret.add(cursor.getString(0));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return ret;
    }

    /**
     * Returns number of all entities in the database
     *
     * @return number of all entities in the database
     */
    public int countEntities() {
        LOG.info("Getting entities count");
        String query = "SELECT COUNT(*) FROM " + TABLE_ENTITIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            int ret = Integer.parseInt(cursor.getString(0));
            cursor.close();
            return ret;

        } else {
            return 0;
        }
    }

    /**
     * Returns number of all entries in the database
     *
     * @return number of all entries in the database
     */
    public int countEntries() {
        LOG.info("Getting entries count");
        return sqLiteEntryTableHelper.getEntriesCount();
    }

    private void executeQuery(String query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    private List<? extends Entry> getEntityEntries(String label, EntityEnum type) {
        List<? extends Entry> entries;
        if (type == EntityEnum.SEQUENCE) {
            entries = sqLiteEntryTableHelper.getAllSequenceEntries(label);
        } else if (type == EntityEnum.DICTIONARY) {
            entries = sqLiteEntryTableHelper.getAllDictEntries(label);
        } else {
            entries = sqLiteEntryTableHelper.getAllSetEntries(label);
        }
        return entries;
    }

    private void createTableDictionaries(SQLiteDatabase db) {
        String createTableEntriesDicts =
                "CREATE TABLE " + TABLE_ENTRIES_DICTS + " (" +
                        KEY_PASS + " TEXT, " +
                        KEY_VALUE + " TEXT, " +
                        KEY_ENTITY + " TEXT " +
                        ");";
        db.execSQL(createTableEntriesDicts);
    }

    private void createTableSets(SQLiteDatabase db) {
        String createTableEntriesGroups =
                "CREATE TABLE " + TABLE_ENTRIES_SETS + " (" +
                        KEY_VALUE + " TEXT, " +
                        KEY_ENTITY + " TEXT " +
                        ");";
        db.execSQL(createTableEntriesGroups);
    }

    private void createTableSequences(SQLiteDatabase db) {
        String createTableEntriesSequences =
                "CREATE TABLE " + TABLE_ENTRIES_SEQUENCES + " (" +
                        KEY_NUMBER + " INTEGER, " +
                        KEY_VALUE + " TEXT, " +
                        KEY_ENTITY + " TEXT " +
                        ");";
        db.execSQL(createTableEntriesSequences);
    }

    private void createEntitiesTable(SQLiteDatabase db) {
        String createTableEntities =
                "CREATE TABLE " + TABLE_ENTITIES + " (" +
                        KEY_LABEL + " TEXT PRIMARY KEY, " +
                        KEY_TYPE + " TEXT, " +
                        KEY_FAVORITE + " INTEGER " +
                        ");";
        db.execSQL(createTableEntities);
    }
}

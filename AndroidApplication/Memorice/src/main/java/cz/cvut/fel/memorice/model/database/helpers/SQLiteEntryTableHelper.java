package cz.cvut.fel.memorice.model.database.helpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.model.entities.entries.DictionaryEntry;
import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.entities.entries.SequenceEntry;

/**
 * This class helps {@link SQLiteHelper} with entries tables. There are three tables so far -
 * one for sets, one for lists and one for dictionaries
 */
public class SQLiteEntryTableHelper {
    private static final Logger LOG = Logger.getLogger(SQLiteEntryTableHelper.class.getName());

    private static final String TABLE_ENTRIES_SEQUENCES = "seqs";
    private static final String KEY_NUMBER = "num";
    private static final String KEY_VALUE = "entry";
    private static final String KEY_ENTITY = "entity";

    private static final String TABLE_ENTRIES_DICTS = "dicts";
    private static final String KEY_PASS = "pass";

    private static final String TABLE_ENTRIES_SETS = "grps";

    /**
     * This method is called upon the creation of new database - creates three new tables.
     *
     * @param db database to be changed
     */
    public void onCreate(SQLiteDatabase db) {
        createTableSets(db);
        createTableSequences(db);
        createTableDictionaries(db);
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

    /**
     * This method is called if the update of the database is going to be executed.
     * Drops all entries tables
     *
     * @param db database reference
     */
    public void onUpgrade(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES_SEQUENCES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES_SETS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES_DICTS);
    }

    /**
     * Deletes all entries from the passed entity
     *
     * @param entity entity, which entries is going to be deleted
     * @param db     writable database
     */
    public void deleteAllEntityEntries(Entity entity, SQLiteDatabase db) {
        switch (entity.getType()) {
            case GROUP:
                db.delete(TABLE_ENTRIES_SETS, KEY_ENTITY + " =?", new String[]{entity.getName()});
                break;
            case SEQUENCE:
                db.delete(TABLE_ENTRIES_SEQUENCES, KEY_ENTITY + " =?", new String[]{entity.getName()});
                break;
            case DICTIONARY:
                db.delete(TABLE_ENTRIES_DICTS, KEY_ENTITY + " =?", new String[]{entity.getName()});
                break;
        }
    }

    /**
     * Returns all set entries in the database from passed entity
     *
     * @param label label of the entity
     * @param db    readable database
     * @return list all entries
     */
    public ArrayList<Entry> getAllSetEntries(String label, SQLiteDatabase db) {
        LOG.info("Getting all set entries from " + label);
        ArrayList<Entry> ret = new ArrayList<>();

        String query = "SELECT " + KEY_VALUE + " FROM " + TABLE_ENTRIES_SETS +
                " WHERE " + KEY_ENTITY + "=" + "\"" + label + "\"";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ret.add(new Entry(cursor.getString(0)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return ret;
    }

    /**
     * Returns all sequence entries in the database from passed entity
     *
     * @param label label of the entity
     * @param db    readable database
     * @return list all entries
     */
    public List<SequenceEntry> getAllSequenceEntries(String label, SQLiteDatabase db) {
        LOG.info("Getting all sequence entries from " + label);
        List<SequenceEntry> ret = new ArrayList<>();
        String query = "SELECT " + KEY_NUMBER + ", " + KEY_VALUE + " FROM " +
                TABLE_ENTRIES_SEQUENCES + " WHERE " + KEY_ENTITY + "=" + "\"" + label + "\"";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ret.add(new SequenceEntry(cursor.getString(1), Integer.parseInt(cursor.getString(0))));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return ret;
    }

    /**
     * Returns all dictionary entries in the database from passed entity
     *
     * @param label label of the entity
     * @param db    readable database
     * @return list all entries
     */
    public List<DictionaryEntry> getAllDictEntries(String label, SQLiteDatabase db) {
        LOG.info("Getting all dictionary entries from " + label);
        List<DictionaryEntry> ret = new ArrayList<>();

        String query = "SELECT " + KEY_PASS + ", " + KEY_VALUE + " FROM " + TABLE_ENTRIES_DICTS +
                " WHERE " + KEY_ENTITY + "=" + "\"" + label + "\"";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ret.add(new DictionaryEntry(cursor.getString(0), cursor.getString(1)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return ret;
    }

    /**
     * Adds one set entry to the database
     *
     * @param entry  entry to be added
     * @param entity owner of the entry
     * @param db     writable database
     */
    public void addSetEntry(Entry entry, Entity entity, SQLiteDatabase db) {
        LOG.info("Adding entry: " + entry);

        ContentValues values = new ContentValues();
        values.put(KEY_VALUE, entry.getValue());
        values.put(KEY_ENTITY, entity.getName());

        db.insert(TABLE_ENTRIES_SETS, null, values);
        db.close();
    }

    /**
     * Adds one sequence entry to the database
     *
     * @param entry  entry to be added
     * @param entity owner of the entry
     * @param db     writable database
     */
    public void addSequenceEntry(SequenceEntry entry, Entity entity, SQLiteDatabase db) {
        LOG.info("Adding entry: " + entry);

        ContentValues values = new ContentValues();
        values.put(KEY_NUMBER, entry.getNumber());
        values.put(KEY_VALUE, entry.getValue());
        values.put(KEY_ENTITY, entity.getName());

        db.insert(TABLE_ENTRIES_SEQUENCES, null, values);
        db.close();
    }

    /**
     * Adds one dictionary entry to the database
     *
     * @param entry  entry to be added
     * @param entity owner of the entry
     * @param db     writable database
     */
    public void addDictEntry(DictionaryEntry entry, Entity entity, SQLiteDatabase db) {
        LOG.info("Adding entry: " + entry);

        ContentValues values = new ContentValues();
        values.put(KEY_PASS, entry.getDefinition());
        values.put(KEY_VALUE, entry.getValue());
        values.put(KEY_ENTITY, entity.getName());

        db.insert(TABLE_ENTRIES_DICTS, null, values);
        db.close();
    }

    /**
     * Deletes passed set entry from the database
     *
     * @param entry entry to be deleted
     * @param db    writable database
     */
    public void deleteSetEntry(Entry entry, SQLiteDatabase db) {
        LOG.info("Deleting set entry " + entry);
        String query = "DELETE FROM " + TABLE_ENTRIES_SETS + "WHERE " + KEY_VALUE + " = " + "\""
                + entry.getValue() + "\"";
        db.execSQL(query);
        db.close();
    }

    /**
     * Deletes passed set entry from the database
     *
     * @param entry entry to be deleted
     * @param db    writable database
     */
    public void deleteSequenceEntry(SequenceEntry entry, SQLiteDatabase db) {
        LOG.info("Deleting sequence entry " + entry);
        String query = "DELETE FROM " + TABLE_ENTRIES_SEQUENCES + "WHERE " + KEY_VALUE + " = " + "\""
                + entry.getValue() + "\"";
        db.execSQL(query);
        db.close();
    }

    /**
     * Deletes passed set entry from the database
     *
     * @param entry entry to be deleted
     * @param db    writable database
     */
    public void deleteDictionaryEntry(DictionaryEntry entry, SQLiteDatabase db) {
        LOG.info("Deleting dictionary entry " + entry);
        String query = "DELETE FROM " + TABLE_ENTRIES_DICTS + "WHERE " + KEY_VALUE + " = " + "\""
                + entry.getValue() + "\"";
        db.execSQL(query);
        db.close();
    }

    /**
     * Returns the total number of entries in all tables.
     *
     * @param db readable database
     * @return the sum of all entries
     */
    public int getEntriesCount(SQLiteDatabase db) {
        int ret = 0;
        for (String table :
                new String[]{TABLE_ENTRIES_DICTS, TABLE_ENTRIES_SETS, TABLE_ENTRIES_SEQUENCES}) {
            String query = "SELECT COUNT(*) FROM " + table;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                ret += Integer.parseInt(cursor.getString(0));
                cursor.close();
            }
        }
        return ret;
    }

}

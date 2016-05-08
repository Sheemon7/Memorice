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

    private static final String TABLE_ENTRIES_SEQUENCES = DatabaseConstants.TABLE_ENTRIES_SEQUENCES;
    private static final String KEY_NUMBER = DatabaseConstants.KEY_NUMBER;
    private static final String KEY_VALUE = DatabaseConstants.KEY_VALUE;
    private static final String KEY_ENTITY = DatabaseConstants.KEY_ENTITY;

    private static final String TABLE_ENTRIES_DICTS = DatabaseConstants.TABLE_ENTRIES_DICTS;
    private static final String KEY_PASS = DatabaseConstants.KEY_PASS;

    private static final String TABLE_ENTRIES_SETS = DatabaseConstants.TABLE_ENTRIES_SETS;

    private SQLiteHelper helper;

    public SQLiteEntryTableHelper(SQLiteHelper helper) {
        this.helper = helper;
    }

    /**
     * Deletes all entries from the passed entity
     *
     * @param entity entity, which entries is going to be deleted
     */
    public void deleteAllEntityEntries(Entity entity) {
        SQLiteDatabase db = helper.getWritableDatabase();
        switch (entity.getType()) {
            case SEQUENCE:
                db.delete(TABLE_ENTRIES_SEQUENCES, KEY_ENTITY + " =?", new String[]{entity.getLabel()});
                break;
            case DICTIONARY:
                db.delete(TABLE_ENTRIES_DICTS, KEY_ENTITY + " =?", new String[]{entity.getLabel()});
                break;
            default:
                db.delete(TABLE_ENTRIES_SETS, KEY_ENTITY + " =?", new String[]{entity.getLabel()});
                break;
        }
        db.close();
    }

    /**
     * Returns all set entries in the database from passed entity
     *
     * @param label label of the entity
     * @return list all entries
     */
    public List<Entry> getAllSetEntries(String label) {
        LOG.info("Getting all set entries from " + label);
        ArrayList<Entry> ret = new ArrayList<>();

        String query = "SELECT " + KEY_VALUE + " FROM " + TABLE_ENTRIES_SETS +
                " WHERE " + KEY_ENTITY + "=" + "\"" + label + "\"";
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ret.add(new Entry(cursor.getString(0)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return ret;
    }

    /**
     * Returns all sequence entries in the database from passed entity
     *
     * @param label label of the entity
     * @return list all entries
     */
    public List<SequenceEntry> getAllSequenceEntries(String label) {
        LOG.info("Getting all sequence entries from " + label);
        List<SequenceEntry> ret = new ArrayList<>();
        String query = "SELECT " + KEY_NUMBER + ", " + KEY_VALUE + " FROM " +
                TABLE_ENTRIES_SEQUENCES + " WHERE " + KEY_ENTITY + "=" + "\"" + label + "\"";
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ret.add(new SequenceEntry(cursor.getString(1), Integer.parseInt(cursor.getString(0))));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return ret;
    }

    /**
     * Returns all dictionary entries in the database from passed entity
     *
     * @param label label of the entity
     * @return list all entries
     */
    public List<DictionaryEntry> getAllDictEntries(String label) {
        LOG.info("Getting all dictionary entries from " + label);
        List<DictionaryEntry> ret = new ArrayList<>();

        String query = "SELECT " + KEY_PASS + ", " + KEY_VALUE + " FROM " + TABLE_ENTRIES_DICTS +
                " WHERE " + KEY_ENTITY + "=" + "\"" + label + "\"";

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ret.add(new DictionaryEntry(cursor.getString(0), cursor.getString(1)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return ret;
    }

    /**
     * Adds one set entry to the database
     *
     * @param entry  entry to be added
     * @param entity owner of the entry
     */
    public void addSetEntry(Entry entry, Entity entity) {
        LOG.info("Adding entry: " + entry);

        ContentValues values = new ContentValues();
        values.put(KEY_VALUE, entry.getValue());
        values.put(KEY_ENTITY, entity.getLabel());
        SQLiteDatabase db = helper.getWritableDatabase();
        db.insert(TABLE_ENTRIES_SETS, null, values);
        db.close();
    }

    /**
     * Adds one sequence entry to the database
     *
     * @param entry  entry to be added
     * @param entity owner of the entry
     */
    public void addSequenceEntry(SequenceEntry entry, Entity entity) {
        LOG.info("Adding sequence entry: " + entry);

        ContentValues values = new ContentValues();
        values.put(KEY_NUMBER, entry.getNumber());
        values.put(KEY_VALUE, entry.getValue());
        values.put(KEY_ENTITY, entity.getLabel());
        SQLiteDatabase db = helper.getWritableDatabase();
        db.insert(TABLE_ENTRIES_SEQUENCES, null, values);
        db.close();
    }

    /**
     * Adds one dictionary entry to the database
     *
     * @param entry  entry to be added
     * @param entity owner of the entry
     */
    public void addDictEntry(DictionaryEntry entry, Entity entity) {
        LOG.info("Adding dictionary entry: " + entry);

        ContentValues values = new ContentValues();
        values.put(KEY_PASS, entry.getDefinition());
        values.put(KEY_VALUE, entry.getValue());
        values.put(KEY_ENTITY, entity.getLabel());
        SQLiteDatabase db = helper.getWritableDatabase();
        db.insert(TABLE_ENTRIES_DICTS, null, values);
        db.close();
    }

    /**
     * Deletes passed set entry from the database
     *
     * @param entry entry to be deleted
     */
    public void deleteEntry(Entry entry, Entity entity) {
        LOG.info("Deleting set entry " + entry);
        String query = "DELETE FROM " + TABLE_ENTRIES_SETS + " WHERE " + KEY_VALUE + " = " + "\""
                + entry.getValue() + "\"" + " AND " + KEY_ENTITY
                + " = " + "\"" + entity.getLabel() + "\"";
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    /**
     * Deletes passed set entry from the database
     *
     * @param entry entry to be deleted
     */
    public void deleteEntry(SequenceEntry entry, Entity entity) {
        LOG.info("Deleting sequence entry " + entry);
        String query = "DELETE FROM " + TABLE_ENTRIES_SEQUENCES + " WHERE " + KEY_VALUE + " = " + "\""
                + entry.getValue() + "\"" + " AND " + KEY_ENTITY
                + " = " + "\"" + entity.getLabel() + "\"";
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    /**
     * Deletes passed set entry from the database
     *
     * @param entry entry to be deleted
     */
    public void deleteEntry(DictionaryEntry entry, Entity entity) {
        LOG.info("Deleting dictionary entry " + entry);
        String query = "DELETE FROM " + TABLE_ENTRIES_DICTS + " WHERE " + KEY_VALUE + " = " + "\""
                + entry.getValue() + "\"" + " AND " + KEY_ENTITY
                + " = " + "\"" + entity.getLabel() + "\"";
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    /**
     * Edits passed set entry from the database
     *
     * @param entry entry to be deleted
     */
    public void editEntry(Entry entry, String oldValue, Entity entity) {
        LOG.info("Editing set entry " + entry);
        String query = "UPDATE " + TABLE_ENTRIES_SETS + " SET " + KEY_VALUE + "=\"" + entry.getValue() +
                "\" WHERE " + KEY_VALUE + " = " + "\""
                + oldValue + "\"" + " AND " + KEY_ENTITY
                + " = " + "\"" + entity.getLabel() + "\"";
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    /**
     * Edits passed set entry from the database
     *
     * @param entry entry to be deleted
     */
    public void editEntry(SequenceEntry entry, String oldValue, Entity entity) {
        LOG.info("Editing set entry " + entry);
        String query = "UPDATE " + TABLE_ENTRIES_SEQUENCES + " SET " + KEY_VALUE + "=\"" + entry.getValue() +
                "\" WHERE " + KEY_VALUE + " = " + "\""
                + oldValue + "\"" + " AND " + KEY_ENTITY
                + " = " + "\"" + entity.getLabel() + "\"";
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    /**
     * Edits passed set entry from the database
     *
     * @param entry entry to be deleted
     */
    public void editEntry(DictionaryEntry entry, String oldValue, Entity entity) {
        LOG.info("Editing set entry " + entry);
        String query = "UPDATE " + TABLE_ENTRIES_DICTS + " SET " + KEY_VALUE + "=\"" + entry.getValue() +
                "\" WHERE " + KEY_VALUE + " = " + "\""
                + oldValue + "\"" + " AND " + KEY_ENTITY
                + " = " + "\"" + entity.getLabel() + "\"";
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    /**
     * Returns the total number of entries in all tables.
     *
     * @return the sum of all entries
     */
    public int getEntriesCount() {
        int ret = 0;
        for (String table :
                new String[]{TABLE_ENTRIES_DICTS, TABLE_ENTRIES_SETS, TABLE_ENTRIES_SEQUENCES}) {
            String query = "SELECT COUNT(*) FROM " + table;
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                ret += Integer.parseInt(cursor.getString(0));
                cursor.close();
            }
            db.close();
        }
        return ret;
    }
}

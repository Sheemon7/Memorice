package cz.cvut.fel.memorice.model.database.helpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.logging.Logger;

import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.model.entities.entries.DictionaryEntry;
import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.entities.entries.SequenceEntry;

public class SQLiteEntryTableHelper {
    private static final Logger LOG = Logger.getLogger(SQLiteEntryTableHelper.class.getName());

    private static final String TABLE_ENTRIES_SEQUENCES = "seqs";
    private static final String KEY_NUMBER = "num";
    private static final String KEY_VALUE = "entry";
    private static final String KEY_ENTITY = "entity";

    private static final String TABLE_ENTRIES_DICTS = "dicts";
    private static final String KEY_PASS = "pass";

    private static final String TABLE_ENTRIES_GROUPS = "grps";

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
                "CREATE TABLE " + TABLE_ENTRIES_GROUPS + " (" +
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

    public void onUpgrade(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES_SEQUENCES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES_GROUPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES_DICTS);
    }

    public void deleteAllEntityEntries(Entity e, SQLiteDatabase db) {
        db.delete(TABLE_ENTRIES_SEQUENCES, KEY_ENTITY + " =?", new String[]{e.getName()});
        db.delete(TABLE_ENTRIES_GROUPS, KEY_ENTITY + " =?", new String[]{e.getName()});
        db.delete(TABLE_ENTRIES_DICTS, KEY_ENTITY + " =?", new String[]{e.getName()});
    }

    public ArrayList<SequenceEntry> getAllSequenceEntries(String label, SQLiteDatabase db) {
        LOG.info("Getting all sequence entries from " + label);
        ArrayList<SequenceEntry> ret = new ArrayList<>();
        String query = "SELECT " + KEY_NUMBER + ", " + KEY_VALUE + " FROM " + TABLE_ENTRIES_SEQUENCES +
                " WHERE " + KEY_ENTITY + "=" + "\"" + label + "\"";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ret.add(new SequenceEntry(cursor.getString(1), Integer.parseInt(cursor.getString(0))));
            } while (cursor.moveToNext());
        }
        return ret;
    }

    public ArrayList<DictionaryEntry> getAllDictEntries(String label, SQLiteDatabase db) {
        LOG.info("Getting all dictionary entries from " + label);
        ArrayList<DictionaryEntry> ret = new ArrayList<>();

        String query = "SELECT " + KEY_PASS +", " + KEY_VALUE + " FROM " + TABLE_ENTRIES_DICTS +
                " WHERE " + KEY_ENTITY + "=" + "\"" + label + "\"";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ret.add(new DictionaryEntry(cursor.getString(0), cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        return ret;
    }

    public ArrayList<Entry> getAllSetEntries(String label, SQLiteDatabase db) {
        LOG.info("Getting all set entries from " + label);
        ArrayList<Entry> ret = new ArrayList<>();

        String query = "SELECT " + KEY_VALUE + " FROM " + TABLE_ENTRIES_GROUPS +
                " WHERE " + KEY_ENTITY + "=" + "\"" + label + "\"";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ret.add(new Entry(cursor.getString(0)));
            } while (cursor.moveToNext());
        }
        return ret;
    }

    public void addSetEntry(Entry entry, Entity entity, SQLiteDatabase db) {
        LOG.info("Adding entry: " + entry);

        ContentValues values = new ContentValues();
        values.put(KEY_VALUE, entry.getValue());
        values.put(KEY_ENTITY, entity.getName());

        db.insert(TABLE_ENTRIES_GROUPS, null, values);
        db.close();
    }

    public void addSequenceEntry(SequenceEntry entry, Entity entity, SQLiteDatabase db) {
        LOG.info("Adding entry: " + entry);

        ContentValues values = new ContentValues();
        values.put(KEY_NUMBER, entry.getNumber());
        values.put(KEY_VALUE, entry.getValue());
        values.put(KEY_ENTITY, entity.getName());

        db.insert(TABLE_ENTRIES_SEQUENCES, null, values);
        db.close();
    }

    public void addDictEntry(DictionaryEntry entry, Entity entity, SQLiteDatabase db) {
        LOG.info("Adding entry: " + entry);

        ContentValues values = new ContentValues();
        values.put(KEY_PASS, entry.getDefinition());
        values.put(KEY_VALUE, entry.getValue());
        values.put(KEY_ENTITY, entity.getName());

        db.insert(TABLE_ENTRIES_DICTS, null, values);
        db.close();
    }

    public void deleteSetEntry(Entry entry, SQLiteDatabase db) {
        LOG.info("Deleting set entry " + entry);
        String query = "DELETE FROM " + TABLE_ENTRIES_GROUPS + "WHERE " + KEY_VALUE + " = " + "\"" + entry.getValue() +"\"";
        db.execSQL(query);
        db.close();
    }

    public void deleteSequenceEntry(SequenceEntry entry, SQLiteDatabase db) {
        LOG.info("Deleting sequence entry " + entry);
        String query = "DELETE FROM " + TABLE_ENTRIES_SEQUENCES + "WHERE " + KEY_VALUE + " = " + "\"" + entry.getValue() +"\"";
        db.execSQL(query);
        db.close();
    }

    public void deleteDictionaryEntry(DictionaryEntry entry, SQLiteDatabase db) {
        LOG.info("Deleting dictionary entry " + entry);
        String query = "DELETE FROM " + TABLE_ENTRIES_DICTS + "WHERE " + KEY_VALUE + " = " + "\"" + entry.getValue() +"\"";
        db.execSQL(query);
        db.close();
    }

    public int getEntriesCount(SQLiteDatabase db) {
        int ret = 0;
        for (String table :
                new String[]{TABLE_ENTRIES_DICTS, TABLE_ENTRIES_GROUPS, TABLE_ENTRIES_SEQUENCES}) {
            String query = "SELECT COUNT(*) FROM " + table;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                ret += Integer.parseInt(cursor.getString(0));
            }

        }
        return  ret;
    }

}

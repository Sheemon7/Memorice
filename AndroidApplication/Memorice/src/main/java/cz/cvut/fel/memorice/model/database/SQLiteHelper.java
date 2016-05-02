package cz.cvut.fel.memorice.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.logging.Logger;

import cz.cvut.fel.memorice.model.entities.Dictionary;
import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.model.entities.EntityEnum;
import cz.cvut.fel.memorice.model.entities.builders.Builder;
import cz.cvut.fel.memorice.model.entities.entries.DictionaryEntry;
import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.entities.entries.SequenceEntry;
import cz.cvut.fel.memorice.model.util.WrongNameException;

/**
 * Created by sheemon on 16.4.16.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    private static final Logger LOG = Logger.getLogger(SQLiteHelper.class.getName());

    // Database Version
    private static final int DATABASE_VERSION = 4;
    // Database Name
    private static final String DATABASE_NAME = "MemoriceDB_4";

    // Entities table name
    private static final String TABLE_ENTITIES = "entities";
    // Entities table Columns names
    private static final String KEY_LABEL = "label";
    private static final String KEY_TYPE = "type";
    private static final String KEY_FAVORITE = "favorite";
    private static final String[] ENTITIES_COLUMNS = {KEY_LABEL, KEY_TYPE, KEY_FAVORITE};

    // Entries table name
    private static final String TABLE_ENTRIES_SEQUENCES = "seqs";
    // Entries table Columns names
    private static final String KEY_NUMBER = "num";
    private static final String KEY_VALUE = "entry";
    private static final String KEY_ENTITY = "entity";

    // Entries table name
    private static final String TABLE_ENTRIES_DICTS = "dicts";
    // Entries table Columns names
    private static final String KEY_PASS = "pass";

    // Entries table name
    private static final String TABLE_ENTRIES_GROUPS = "grps";
    // Entries table Columns names

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        LOG.info("Creating database " + DATABASE_NAME + " , version: " + DATABASE_VERSION);
        String createTableEntities =
                "CREATE TABLE " + TABLE_ENTITIES + " (" +
                        KEY_LABEL + " TEXT PRIMARY KEY, " +
                        KEY_TYPE + " TEXT, " +
                        KEY_FAVORITE + " INTEGER " +
                        ");";
        String createTableEntriesSequences =
                "CREATE TABLE " + TABLE_ENTRIES_SEQUENCES + " (" +
                        KEY_NUMBER + " INTEGER, " +
                        KEY_VALUE + " TEXT, " +
                        KEY_ENTITY + " TEXT " +
                        ");";
        String createTableEntriesGroups =
                "CREATE TABLE " + TABLE_ENTRIES_GROUPS + " (" +
                        KEY_VALUE + " TEXT, " +
                        KEY_ENTITY + " TEXT " +
                        ");";
        String createTableEntriesDicts =
                "CREATE TABLE " + TABLE_ENTRIES_DICTS + " (" +
                        KEY_PASS + " TEXT, " +
                        KEY_VALUE + " TEXT, " +
                        KEY_ENTITY + " TEXT " +
                        ");";
        db.execSQL(createTableEntities);
        db.execSQL(createTableEntriesSequences);
        db.execSQL(createTableEntriesGroups);
        db.execSQL(createTableEntriesDicts);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LOG.info("Updating database");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTITIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES_SEQUENCES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES_GROUPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES_DICTS);
        this.onCreate(db);
    }

    public void addEntity(Entity entity) {
        LOG.info("Adding entity: " + entity);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LABEL, entity.getName());
        values.put(KEY_TYPE, entity.getType().toString());
        values.put(KEY_FAVORITE, entity.isFavourite() ? 1 : 0);
        LOG.info(values.toString());
        db.insert(TABLE_ENTITIES, null, values);
        db.close();

        if (entity.getType() == EntityEnum.SEQUENCE) {
            for (SequenceEntry entry : (Iterable<SequenceEntry>) entity) {
                addSequenceEntry(entry, entity);
            }
        } else if (entity.getType() == EntityEnum.DICTIONARY) {
            for (DictionaryEntry entry : (Iterable<DictionaryEntry>) entity) {
                addDictEntry(entry, entity);
            }
        } else {
            for (Entry entry : (Iterable<Entry>) entity) {
                addSetEntry(entry, entity);
            }
        }

    }

    public void addSequenceEntry(SequenceEntry entry, Entity entity) {
        LOG.info("Adding entry: " + entry);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NUMBER, entry.getNumber());
        values.put(KEY_VALUE, entry.getValue());
        values.put(KEY_ENTITY, entity.getName());

        db.insert(TABLE_ENTRIES_SEQUENCES, null, values);
        db.close();
    }

    public void addDictEntry(DictionaryEntry entry, Entity entity) {
        LOG.info("Adding entry: " + entry);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PASS, entry.getDefinition());
        values.put(KEY_VALUE, entry.getValue());
        values.put(KEY_ENTITY, entity.getName());

        db.insert(TABLE_ENTRIES_DICTS, null, values);
        db.close();
    }

    public void addSetEntry(Entry entry, Entity entity) {
        LOG.info("Adding entry: " + entry);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_VALUE, entry.getValue());
        values.put(KEY_ENTITY, entity.getName());

        db.insert(TABLE_ENTRIES_GROUPS, null, values);
        db.close();
    }

    public Entity getEntity(String label) {
        LOG.info("Getting entity: " + label);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ENTITIES,
                ENTITIES_COLUMNS,
                " label = ? ",
                new String[]{label},
                null, null, null, null);

        ArrayList<? extends Entry> entries;
        if (cursor != null && cursor.moveToFirst()) {
            EntityEnum type = EntityEnum.getType(cursor.getString(1));
            Builder b = Builder.getCorrectBuilder(type);
            b.init(label);
            if (type == EntityEnum.SEQUENCE) {
                entries = getAllSequenceEntries(label);
            } else if (type == EntityEnum.DICTIONARY) {
                entries = getAllDictEntries(label);
            } else {
                entries = getAllSetEntries(label);
            }
            for (Entry e : entries) {
                b.add(e);
            }
            Entity entity = b.wrap();
            try {
                entity.setFavourite(isEntityFavourite(entity));
            } catch (WrongNameException e) {
                e.printStackTrace();
            }
            return entity;
        } else {
            return null;
        }
    }

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
        }
        return ret;
    }

    public ArrayList<SequenceEntry> getAllSequenceEntries(String label) {
        LOG.info("Getting all sequence entries from " + label);
        ArrayList<SequenceEntry> ret = new ArrayList<>();
        String query = "SELECT " + KEY_NUMBER + ", " + KEY_VALUE + " FROM " + TABLE_ENTRIES_SEQUENCES +
                " WHERE " + KEY_ENTITY + "=" + "\"" + label + "\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ret.add(new SequenceEntry(cursor.getString(1), Integer.parseInt(cursor.getString(0))));
            } while (cursor.moveToNext());
        }
        return ret;
    }

    public ArrayList<DictionaryEntry> getAllDictEntries(String label) {
        LOG.info("Getting all dictionary entries from " + label);
        ArrayList<DictionaryEntry> ret = new ArrayList<>();

        String query = "SELECT " + KEY_PASS +", " + KEY_VALUE + " FROM " + TABLE_ENTRIES_DICTS +
                " WHERE " + KEY_ENTITY + "=" + "\"" + label + "\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ret.add(new DictionaryEntry(cursor.getString(0), cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        return ret;
    }

    public ArrayList<Entry> getAllSetEntries(String label) {
        LOG.info("Getting all set entries from " + label);
        ArrayList<Entry> ret = new ArrayList<>();

        String query = "SELECT " + KEY_VALUE + " FROM " + TABLE_ENTRIES_GROUPS +
                " WHERE " + KEY_ENTITY + "=" + "\"" + label + "\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ret.add(new Entry(cursor.getString(0)));
            } while (cursor.moveToNext());
        }
        return ret;
    }

    public ArrayList<Entity> getAllEntities() {
        LOG.info("Getting all entities:");
        ArrayList<Entity> ret = new ArrayList<>();

        String query = "SELECT " + KEY_LABEL + " FROM " + TABLE_ENTITIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst() && cursor != null) {
            do {
                String label = cursor.getString(0);
                ret.add(getEntity(label));
            } while (cursor.moveToNext());
        }
        return ret;
    }

    public ArrayList<Entity> getAllFavouriteEntities() {
        LOG.info("Getting all favourite entities:");
        ArrayList<Entity> ret = new ArrayList<>();

        String query = "SELECT " + KEY_LABEL + " FROM " + TABLE_ENTITIES + " WHERE " + KEY_FAVORITE + "=1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst() && cursor != null) {
            do {
                String label = cursor.getString(0);
                ret.add(getEntity(label));
            } while (cursor.moveToNext());
        }
        return ret;
    }

    public ArrayList<Entity> getAllFavouriteFilteredEntities(String filter) {
        LOG.info("Getting all favourite and filtered entities:");
        ArrayList<Entity> ret = new ArrayList<>();

        String query = "SELECT " + KEY_LABEL + " FROM " + TABLE_ENTITIES + " WHERE " + KEY_FAVORITE + "=1 AND "
                + KEY_LABEL + " LIKE " + "\"%" + filter + "%\"";;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst() && cursor != null) {
            do {
                String label = cursor.getString(0);
                ret.add(getEntity(label));
            } while (cursor.moveToNext());
        }
        return ret;
    }

    public void deleteEntity(Entity e) {
        LOG.info("Deleting entity: " + e.getName());
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ENTITIES, KEY_LABEL + " =?", new String[]{e.getName()});
        db.delete(TABLE_ENTRIES_SEQUENCES, KEY_ENTITY + " =?", new String[]{e.getName()});
        db.delete(TABLE_ENTRIES_GROUPS, KEY_ENTITY + " =?", new String[]{e.getName()});
        db.delete(TABLE_ENTRIES_DICTS, KEY_ENTITY + " =?", new String[]{e.getName()});
        db.close();
    }

    public void deleteSequenceEntry(SequenceEntry entry) {
        LOG.info("Deleting sequence entry " + entry);
        String query = "DELETE FROM " + TABLE_ENTRIES_SEQUENCES + "WHERE " + KEY_VALUE + " = " + "\"" + entry.getValue() +"\"";
        executeQuery(query);
    }

    public void deleteSetEntry(Entry entry) {
        LOG.info("Deleting set entry " + entry);
        String query = "DELETE FROM " + TABLE_ENTRIES_GROUPS + "WHERE " + KEY_VALUE + " = " + "\"" + entry.getValue() +"\"";
        executeQuery(query);
    }

    public void deleteDictionaryEntry(DictionaryEntry entry) {
        LOG.info("Deleting dictionary entry " + entry);
        String query = "DELETE FROM " + TABLE_ENTRIES_DICTS + "WHERE " + KEY_VALUE + " = " + "\"" + entry.getValue() +"\"";
        executeQuery(query);
    }

    private void executeQuery(String query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    public void toggleFavorite(Entity e) throws WrongNameException {
        LOG.info("Toggling favourite at " + e.getName());
        e.setFavourite(!e.isFavourite());
        String query = "UPDATE " + TABLE_ENTITIES + " SET " + KEY_FAVORITE + "=" + (e.isFavourite() ? 1 : 0) +
                " WHERE " + KEY_LABEL + "=" + "\'" + e.getName() + "\'";
        executeQuery(query);
    }

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
        }
        return ret;
    }


    public boolean isEntityFavourite(Entity e) throws WrongNameException {
        LOG.info("Verifying favourite at " + e.getName());
        String query = "SELECT " + KEY_FAVORITE + " FROM " + TABLE_ENTITIES + " WHERE " + KEY_LABEL + "=" + "\'" + e.getName() + "\'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            return 1 == Integer.parseInt(cursor.getString(0));
        } else {
            throw new WrongNameException();
        }
    }
}

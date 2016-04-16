package cz.cvut.fel.memorice.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.logging.Logger;

import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.model.entities.EntityEnum;
import cz.cvut.fel.memorice.model.entities.Sequence;
import cz.cvut.fel.memorice.model.entities.builders.Builder;
import cz.cvut.fel.memorice.model.entities.builders.DictionaryBuilder;
import cz.cvut.fel.memorice.model.entities.builders.GroupBuilder;
import cz.cvut.fel.memorice.model.entities.builders.SequenceBuilder;

/**
 * Created by sheemon on 16.4.16.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    private static final Logger LOG = Logger.getLogger(SQLiteHelper.class.getName());

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "MemoriceDB";

    // Entities table name
    private static final String TABLE_ENTITIES = "entities";
    // Entities Table Columns names
    private static final String KEY_LABEL = "label";
    private static final String KEY_TYPE = "author";
    private static final String KEY_FAVORITE = "favorite";
    private static final String[] ENTITIES_COLUMNS = {KEY_LABEL, KEY_TYPE, KEY_FAVORITE};

    // Entries table name
    private static final String TABLE_BOOKS = "books";
    //TODO - tabulky


    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_ENTITIES =
                "CREATE TABLE entities (" +
                        KEY_LABEL + " TEXT PRIMARY KEY, " +
                        KEY_TYPE + " TEXT ENUM ('sequence', 'group', 'dictionary') " +
                        KEY_FAVORITE + " BOOLEAN)";
        db.execSQL(CREATE_TABLE_ENTITIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTITIES);
        //TODO
        this.onCreate(db);
    }

    public void addEntity(Entity entity) {
        LOG.info("Adding entity: " + entity.getName());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LABEL, entity.getName());
        values.put(KEY_TYPE, entity.getType().toString());
        values.put(KEY_FAVORITE, false);

        db.insert(TABLE_ENTITIES, null, values);
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

        if (cursor != null) {
            cursor.moveToFirst();
        }

        EntityEnum type = EntityEnum.getType(cursor.getString(1));
        Builder b = Builder.getCorrectBuilder(type);


        b.init(label);
        //TODO - add entries, SET FAVORITE
        return b.wrap();
    }

    public void deleteEntity(String label) {
        LOG.info("Deleting entity: " + label);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ENTITIES, KEY_LABEL + " =?", new String[]{label});
        db.close();
    }

    public ArrayList<String> getAllLabels() {
        SQLiteDatabase db = this.getReadableDatabase();
        String querry = "SELECT " + KEY_LABEL + " FROM " + TABLE_ENTITIES;
        Cursor cursor = db.rawQuery(querry, null);

        ArrayList<String> ret = new ArrayList<>();

        if (cursor != null) {
            cursor.moveToFirst();
        }
        while (!cursor.isLast()) {
            ret.add(cursor.getString(0));
            cursor.moveToNext();
        }
        return ret;
    }
}

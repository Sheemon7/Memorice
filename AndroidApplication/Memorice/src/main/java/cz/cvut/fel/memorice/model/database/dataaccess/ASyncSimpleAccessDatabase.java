package cz.cvut.fel.memorice.model.database.dataaccess;

import android.content.Context;
import android.os.AsyncTask;

import cz.cvut.fel.memorice.model.database.helpers.SQLiteEntryTableHelper;
import cz.cvut.fel.memorice.model.database.helpers.SQLiteHelper;
import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.model.entities.entries.Entry;

/**
 * {@link ASyncSimpleAccessDatabase} is responsible for simple database operations that
 * do not require layout or other advanced changes. Everything is executed on the next thread
 */
public class ASyncSimpleAccessDatabase extends AsyncTask<String, Void, Void> {

    /**
     * Constant indicating add entity to the database action
     */
    public static final String ADD_ENTITY = "add_entity";

    /**
     * Constant indicating delete entity from database action
     */
    public static final String DELETE_ENTITY = "delete_entity";

    /**
     * Constant indicating renaming entity in the database action
     */
    public static final String RENAME_ENTITY = "rename_entity";

    /**
     * Constant indicating delete entry from database action
     */
    public static final String DELETE_ENTRY = "delete_entry";

    /**
     * Constant indicating adding entry to database action
     */
    public static final String EDIT_ENTRY = "edit_entry";

    /**
     * Constant indicating toggle favourite entity action
     */
    public static final String TOGGLE_FAVOURITE = "toggle_favourite";

    private Context context;
    private Entity entity;
    private Entry entry;
    private String oldValue;

    /**
     * Constructor demanding context of the application where the database query comes from
     *
     * @param context context of the application
     */
    public ASyncSimpleAccessDatabase(Context context) {
        this.context = context;
    }

    /**
     * Does the specific action base on the constant passed as first parameter
     *
     * @param params array of parameters, first one always specifies intended action
     * @return void (required by super)
     */
    @Override
    protected Void doInBackground(String... params) {
        String param = params[0];
        SQLiteHelper helper = new SQLiteHelper(context);
        switch (param) {
            case DELETE_ENTITY:
                helper.deleteEntity(entity);
                break;
            case RENAME_ENTITY:
                helper.renameEntity(oldValue, entity);
                break;
            case DELETE_ENTRY:
                new SQLiteEntryTableHelper(helper).deleteEntry(entry, entity);
                break;
            case EDIT_ENTRY:
                new SQLiteEntryTableHelper(helper).editEntry(entry, oldValue, entity);
                break;
            case ADD_ENTITY:
                helper.addEntity(entity);
                break;
            case TOGGLE_FAVOURITE:
                helper.toggleFavourite(entity);
                break;
        }
        return null;
    }

    /**
     * Passes entity to the action
     *
     * @param entity entity to be worked with
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    /**
     * Passes entry to the action
     *
     * @param entry entry to be worked with
     */
    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    /**
     * Sets old value of the entry to be edited
     * @param oldValue old value of the entry
     */
    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }
}

package cz.cvut.fel.memorice.model.database.dataaccess;

import android.content.Context;
import android.os.AsyncTask;

import cz.cvut.fel.memorice.model.database.helpers.SQLiteHelper;
import cz.cvut.fel.memorice.model.entities.Entity;

public class ASyncSimpleReadDatabase extends AsyncTask<String, Void, Void> {

    public static final String ADD_ENTITY = "add_entity";
    public static final String DELETE_ENTITY = "delete_entity";
    public static final String TOGGLE_FAVOURITE = "toggle_favourite";

    private Context context;
    private Entity entity;

    public ASyncSimpleReadDatabase(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        String param = params[0];
        SQLiteHelper helper = new SQLiteHelper(context);
        if (param.equals(DELETE_ENTITY)) {
            helper.deleteEntity(entity);
        } else if (param.equals(ADD_ENTITY)) {
            helper.addEntity(entity);
        } else if (param.equals(TOGGLE_FAVOURITE)) {
            helper.toggleFavourite(entity);
        }
        return null;
    }

    public void setEntity(Entity e) {
        this.entity = e;
    }
}

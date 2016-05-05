package cz.cvut.fel.memorice.model.database.dataaccess;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import cz.cvut.fel.memorice.model.database.helpers.SQLiteHelper;
import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.view.fragments.EntityListAdapter;

public class ASyncListReadDatabase extends AsyncTask<String, Void, List<Entity>> {

    public static final String ALL_ENTITIES = "all_entities";
    public static final String FAVOURITE_ENTITIES = "favourite_entities";
    public static final String FILTERED_ENTITIES = "filtered_entities";
    public static final String FAVOURITE_FILTER_ENTITIES = "favourite_filter_entities";

    private Context context;
    private String filter = "";
    private EntityListAdapter adapter;

    public ASyncListReadDatabase(Context context) {
        this.context = context;
    }

    @Override
    protected List<Entity> doInBackground(String... params) {
        String param = params[0];
        SQLiteHelper helper = new SQLiteHelper(context);
        if (param.equals(ALL_ENTITIES)) {
            return helper.getAllEntities();
        } else if (param.equals(FAVOURITE_ENTITIES)) {
            return helper.getAllFavouriteEntities();
        } else if (param.equals(FILTERED_ENTITIES)) {
            return helper.getAllEntitiesFiltered(filter);
        } else if (param.equals(FAVOURITE_FILTER_ENTITIES)) {
            return helper.getAllFavouriteEntitiesFiltered(filter);
        } else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Entity> entities) {
        this.adapter.setData(entities);
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void setAdapter(EntityListAdapter adapter) {
        this.adapter = adapter;
    }
}

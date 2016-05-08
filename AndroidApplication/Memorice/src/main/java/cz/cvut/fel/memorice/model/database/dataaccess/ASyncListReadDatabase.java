package cz.cvut.fel.memorice.model.database.dataaccess;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.memorice.model.database.helpers.SQLiteHelper;
import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.view.fragments.EntityListAdapter;

/**
 * {@link ASyncListReadDatabase} serves for accessing the database in another thread and querying
 * multiple objects (typically lists of objects according to given criteria)
 */
public class ASyncListReadDatabase extends AsyncTask<String, Void, List<Entity>> {

    /**
     * Constant indicating get all entities action
     */
    public static final String ALL_ENTITIES = "all_entities";

    /**
     * Constant indicating get all favourite entities action
     */
    public static final String FAVOURITE_ENTITIES = "favourite_entities";

    /**
     * Constant indicating get all filtered entities action
     */
    public static final String FILTERED_ENTITIES = "filtered_entities";

    /**
     * Constant indicating get all favourite filtered entities action
     */
    public static final String FAVOURITE_FILTER_ENTITIES = "favourite_filter_entities";

    private Context context;
    private String filter = "";
    private EntityListAdapter adapter;

    public ASyncListReadDatabase(Context context) {
        this.context = context;
    }

    /**
     * Does the specific action base on the constant passed as first parameter
     * @param params array of parameters, first one always specifies intended action
     * @return list of objects specified by query
     */
    @Override
    protected List<Entity> doInBackground(String... params) {
        String param = params[0];
        SQLiteHelper helper = new SQLiteHelper(context);
        switch (param) {
            case ALL_ENTITIES:
                return helper.getAllEntities();
            case FAVOURITE_ENTITIES:
                return helper.getAllFavouriteEntities();
            case FILTERED_ENTITIES:
                return helper.getAllEntitiesFiltered(filter);
            case FAVOURITE_FILTER_ENTITIES:
                return helper.getAllFavouriteEntitiesFiltered(filter);
            default:
                return new ArrayList<>();
        }
    }

    /**
     * Specifies what happens after the task has been executed. In this case, it updates adapter
     * with new data
     * @param entities list of entities
     */
    @Override
    protected void onPostExecute(List<Entity> entities) {
        this.adapter.setData(entities);
    }

    /**
     * Helper method for setting up showFiltered for further filtering carried out in a task
     * @param filter showFiltered
     */
    public void setFilter(String filter) {
        this.filter = filter;
    }

    /**
     * Helper method for setting up adapter for further use in task
     * @param adapter adapter
     */
    public void setAdapter(EntityListAdapter adapter) {
        this.adapter = adapter;
    }
}

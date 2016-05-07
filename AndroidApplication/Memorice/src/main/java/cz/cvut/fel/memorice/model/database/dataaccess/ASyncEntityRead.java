package cz.cvut.fel.memorice.model.database.dataaccess;

import android.content.Context;
import android.os.AsyncTask;

import cz.cvut.fel.memorice.model.database.helpers.SQLiteHelper;
import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.view.activities.detail.DetailActivity;

/**
 * This class is used to provide access to entity in another thread, serves for viewing detail of entities
 */
public class ASyncEntityRead extends AsyncTask<String, Void, Entity> {

    private Context context;
    private DetailActivity detailActivity;

    public ASyncEntityRead(Context context) {
        this.context = context;
    }

    /**
     * Does the specific action base on the constant passed as first parameter
     *
     * @param params array of parameters, first one always specifies intended action
     * @return list of objects specified by query
     */
    @Override
    protected Entity doInBackground(String... params) {
        String label = params[0];
        SQLiteHelper helper = new SQLiteHelper(context);
        return helper.getEntity(label);
    }

    /**
     * Specifies what happens after the task has been executed. In this case, it updates detail activity
     * with read entity
     *
     * @param entity read entity
     */
    @Override
    protected void onPostExecute(Entity entity) {
        this.detailActivity.setEntity(entity);
    }

    /**
     * Helper method for setting up activity for further use in task
     *
     * @param activity activity
     */
    public void setDetailActivity(DetailActivity activity) {
        this.detailActivity = activity;
    }
}

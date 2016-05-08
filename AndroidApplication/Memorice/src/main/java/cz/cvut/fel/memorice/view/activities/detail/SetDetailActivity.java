package cz.cvut.fel.memorice.view.activities.detail;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.database.dataaccess.ASyncEntityRead;
import cz.cvut.fel.memorice.model.entities.EntityEnum;
import cz.cvut.fel.memorice.model.entities.Set;
import cz.cvut.fel.memorice.view.fragments.DividerItemDecoration;
import cz.cvut.fel.memorice.view.fragments.detail.EntityDetailListAdapter;
import cz.cvut.fel.memorice.view.fragments.detail.SetDetailListAdapter;

/**
 * Created by sheemon on 24.4.16.
 */
public class SetDetailActivity extends DetailActivity {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prepareTypeIcon(ImageView icon) {
        icon.setImageResource(R.drawable.ic_set_white_24dp);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prepareTypeText(EditText textType) {
        textType.setText(EntityEnum.SET.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected EntityDetailListAdapter provideListAdapter(RecyclerView view) {
        return new SetDetailListAdapter(view);
    }

}

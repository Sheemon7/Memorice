package cz.cvut.fel.memorice.view.activities.detail;

import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.entities.EntityEnum;
import cz.cvut.fel.memorice.view.fragments.detail.EntityDetailListAdapter;
import cz.cvut.fel.memorice.view.fragments.detail.SequenceDetailListAdapter;

/**
 * Created by sheemon on 24.4.16.
 */
public class SequenceDetailActivity extends DetailActivity {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prepareTypeIcon(ImageView iconType) {
        iconType.setImageResource(R.drawable.ic_list_white_24dp);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prepareTypeText(EditText textType) {
        textType.setText(EntityEnum.SEQUENCE.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected EntityDetailListAdapter provideListAdapter(RecyclerView view) {
        return new SequenceDetailListAdapter(view);
    }
}

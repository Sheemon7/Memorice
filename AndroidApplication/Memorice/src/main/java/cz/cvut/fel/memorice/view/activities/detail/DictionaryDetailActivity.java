package cz.cvut.fel.memorice.view.activities.detail;

import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.entities.EntityEnum;
import cz.cvut.fel.memorice.view.fragments.detail.DictionaryDetailListAdapter;
import cz.cvut.fel.memorice.view.fragments.detail.EntityDetailListAdapter;

/**
 * Activity displays detail of dictionary entity
 */
public class DictionaryDetailActivity extends DetailActivity {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prepareTypeText(EditText textType) {
        textType.setText(EntityEnum.DICTIONARY.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected EntityDetailListAdapter provideListAdapter(RecyclerView view) {
        return new DictionaryDetailListAdapter(view);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prepareTypeIcon(ImageView iconType) {
        iconType.setImageResource(R.drawable.ic_dictionary_white_24dp);
    }
}

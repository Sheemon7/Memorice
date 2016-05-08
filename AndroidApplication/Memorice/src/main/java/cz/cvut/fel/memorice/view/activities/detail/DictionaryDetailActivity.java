package cz.cvut.fel.memorice.view.activities.detail;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.database.dataaccess.ASyncEntityRead;
import cz.cvut.fel.memorice.model.entities.EntityEnum;
import cz.cvut.fel.memorice.view.fragments.detail.DictionaryDetailListAdapter;
import cz.cvut.fel.memorice.view.fragments.DividerItemDecoration;
import cz.cvut.fel.memorice.view.fragments.detail.EntityDetailListAdapter;

/**
 * Created by sheemon on 18.4.16.
 */
public class DictionaryDetailActivity extends DetailActivity {

    @Override
    protected void prepareTypeText(EditText textType) {
        textType.setText(EntityEnum.DICTIONARY.getName());
    }

    @Override
    protected EntityDetailListAdapter provideListAdapter(RecyclerView view) {
        return new DictionaryDetailListAdapter(view);
    }

    @Override
    protected void prepareTypeIcon(ImageView iconType) {
        iconType.setImageResource(R.drawable.ic_dictionary_white_24dp);
    }
}

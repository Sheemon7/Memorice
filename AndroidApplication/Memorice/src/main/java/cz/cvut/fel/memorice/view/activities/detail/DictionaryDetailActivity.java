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

/**
 * Created by sheemon on 18.4.16.
 */
public class DictionaryDetailActivity extends DetailActivity {

    @Override
    protected void prepareTypeText(EditText textType) {
        textType.setText(EntityEnum.DICTIONARY.getName());
    }

    @Override
    protected void prepareTypeIcon(ImageView iconType) {
        iconType.setImageResource(R.drawable.ic_dictionary_white_24dp);
    }

    protected void prepareRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getApplicationContext(), R.drawable.separator));
        mAdapter = new DictionaryDetailListAdapter(mRecyclerView);
    }
}

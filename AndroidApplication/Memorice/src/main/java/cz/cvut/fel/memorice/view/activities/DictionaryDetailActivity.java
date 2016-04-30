package cz.cvut.fel.memorice.view.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.entities.Dictionary;
import cz.cvut.fel.memorice.model.entities.EntityEnum;
import cz.cvut.fel.memorice.model.entities.entries.DictionaryEntry;
import cz.cvut.fel.memorice.view.fragments.DictionaryDetailListAdapter;
import cz.cvut.fel.memorice.view.fragments.DividerItemDecoration;

/**
 * Created by sheemon on 18.4.16.
 */
public class DictionaryDetailActivity extends DetailActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEntity((Dictionary) getIntent().getSerializableExtra("entity"));
        setContentView(R.layout.activity_detail);
        prepareToolbar();

        ImageView icon = (ImageView) findViewById(R.id.entity_icon);
        icon.setImageResource(R.drawable.ic_dictionary_white_24dp);
        TextView text = (TextView) findViewById(R.id.entity_type);
        text.setText(EntityEnum.DICTIONARY.getName());


        prepareRecyclerView();

    }

    protected void prepareRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getApplicationContext(), R.drawable.separator));
        mAdapter = new DictionaryDetailListAdapter(mRecyclerView);
        mAdapter.setData(getEntity().getListOfEntries());
    }
}

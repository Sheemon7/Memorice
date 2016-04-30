package cz.cvut.fel.memorice.view.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.entities.EntityEnum;
import cz.cvut.fel.memorice.model.entities.Sequence;
import cz.cvut.fel.memorice.model.entities.entries.SequenceEntry;
import cz.cvut.fel.memorice.view.fragments.DictionaryDetailListAdapter;
import cz.cvut.fel.memorice.view.fragments.DividerItemDecoration;
import cz.cvut.fel.memorice.view.fragments.SequenceDetailListAdapter;

/**
 * Created by sheemon on 24.4.16.
 */
public class SequenceDetailActivity extends DetailActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEntity((Sequence) getIntent().getSerializableExtra("entity"));
        setContentView(R.layout.activity_detail);
        prepareToolbar();

        ImageView icon = (ImageView) findViewById(R.id.entity_icon);
        icon.setImageResource(R.drawable.ic_list_white_24dp);
        TextView text = (TextView) findViewById(R.id.entity_type);
        text.setText(EntityEnum.SEQUENCE.getName());

        prepareRecyclerView();

    }

    protected void prepareRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getApplicationContext(), R.drawable.separator));
        mAdapter = new SequenceDetailListAdapter(mRecyclerView);
        mAdapter.setData(getEntity().getListOfEntries());
    }

}

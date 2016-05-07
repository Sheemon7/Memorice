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
import cz.cvut.fel.memorice.model.entities.Sequence;
import cz.cvut.fel.memorice.view.fragments.DividerItemDecoration;
import cz.cvut.fel.memorice.view.fragments.detail.SequenceDetailListAdapter;

/**
 * Created by sheemon on 24.4.16.
 */
public class SequenceDetailActivity extends DetailActivity {

    @Override
    protected void prepareTypeIcon(ImageView iconType) {
        iconType.setImageResource(R.drawable.ic_list_white_24dp);
    }

    @Override
    protected void prepareTypeText(EditText textType) {
        textType.setText(EntityEnum.SEQUENCE.getName());
    }

    protected void prepareRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getApplicationContext(), R.drawable.separator));
        mAdapter = new SequenceDetailListAdapter(mRecyclerView);
    }

}

package cz.cvut.fel.memorice.view.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.database.helpers.SQLiteHelper;
import cz.cvut.fel.memorice.model.entities.builders.SequenceBuilder;
import cz.cvut.fel.memorice.model.entities.entries.SequenceEntry;
import cz.cvut.fel.memorice.model.util.EmptyNameException;
import cz.cvut.fel.memorice.model.util.EmptyTermException;
import cz.cvut.fel.memorice.model.util.NameAlreadyUsedException;
import cz.cvut.fel.memorice.model.util.TermAlreadyUsedException;
import cz.cvut.fel.memorice.view.fragments.SequenceInputListAdapter;

/**
 * Created by sheemon on 18.4.16.
 */
public class SequenceInputActivity extends InputActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        Toolbar toolbar =
                (Toolbar) findViewById(R.id.input_toolbar);
        setSupportActionBar(toolbar);
        setColourToStatusBar();

        ImageView icon = (ImageView) findViewById(R.id.icon_type);
        icon.setImageResource(R.drawable.ic_list_white_24dp);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_cross_white_24dp);
        prepareInputLabels();
        prepareRecyclerView();
    }

    private void prepareRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SequenceInputListAdapter(mRecyclerView);
        ImageView iconAdd = (ImageView) findViewById(R.id.icon_add);
        iconAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.addRow();
            }
        });
        mAdapter.setPlusIcon(iconAdd);
        mAdapter.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: // Intercept the click on the home button
                finish();
                return true;
            case R.id.action_save:
                try {
                    buildNewSequence();
                    finish();
                } catch (NameAlreadyUsedException e) {
                    showLabelUsedDialog(new AlertDialog.Builder(SequenceInputActivity.this));
                } catch (EmptyNameException e) {
                    showLabelEmptyDialog(new AlertDialog.Builder(SequenceInputActivity.this));
                } catch (TermAlreadyUsedException e) {
                    showValueDuplicateDialog(new AlertDialog.Builder(SequenceInputActivity.this));
                } catch (EmptyTermException e) {
                    showValueEmptyDialog(new AlertDialog.Builder(SequenceInputActivity.this));
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void buildNewSequence() throws NameAlreadyUsedException, EmptyNameException, TermAlreadyUsedException, EmptyTermException {
        EditText labelInput = (EditText) findViewById(R.id.entity_type);
        String label = labelInput.getText().toString();
        SQLiteHelper helper = new SQLiteHelper(getApplicationContext());
        if (label.length() == 0) {
            throw new EmptyNameException();
        } else if (helper.getEntity(label) != null) {
            throw new NameAlreadyUsedException();
        }
        SequenceBuilder builder = SequenceBuilder.getInstance();
        builder.init(label);
        try {
            for (SequenceEntry e: (ArrayList< SequenceEntry>) mAdapter.getInput()) {
                builder.add(e);
            }
        } catch (EmptyTermException e) {
            mAdapter.emphasizeErrors();
            builder.wrap();
            throw e;
        }
        addEntityToDatabase(builder.wrap());
    }


}

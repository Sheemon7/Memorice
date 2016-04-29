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

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.database.SQLiteHelper;
import cz.cvut.fel.memorice.model.entities.builders.DictionaryBuilder;
import cz.cvut.fel.memorice.model.entities.entries.DictionaryEntry;
import cz.cvut.fel.memorice.model.util.EmptyNameException;
import cz.cvut.fel.memorice.model.util.NameAlreadyUsedException;
import cz.cvut.fel.memorice.view.fragments.DictionaryInputListAdapter;
import cz.cvut.fel.memorice.view.fragments.EntryInputListAdapter;

/**
 * Created by sheemon on 18.4.16.
 */
public class DictionaryInputActivity extends InputActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        Toolbar toolbar =
                (Toolbar) findViewById(R.id.input_toolbar);
        setSupportActionBar(toolbar);
        setColourToStatusBar();

        ImageView icon = (ImageView) findViewById(R.id.icon_type);
        icon.setImageResource(R.drawable.ic_dictionary_white_24dp);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_cross_white_24dp);
        prepareInputLabels();
        prepareRecyclerView();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // Intercept the click on the home button
                finish();
                return true;
            case R.id.action_save:
                try {
                    buildNewDictionary();
                    finish();
                } catch (NameAlreadyUsedException e) {
                    showLabelUsedDialog(new AlertDialog.Builder(DictionaryInputActivity.this));
                } catch (EmptyNameException e) {
                    showLabelEmptyDialog(new AlertDialog.Builder(DictionaryInputActivity.this));
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void buildNewDictionary() throws NameAlreadyUsedException, EmptyNameException {
        labelInput = (EditText) findViewById(R.id.entity_type);
        String label = labelInput.getText().toString().trim();
        SQLiteHelper helper = new SQLiteHelper(getApplicationContext());
        if (helper.getEntity(label) != null) {
            throw new NameAlreadyUsedException();
        }
        if (label.length() == 0) {
            throw new EmptyNameException();
        }
        DictionaryBuilder builder = DictionaryBuilder.getInstance();
        builder.init(label);

        builder.add(new DictionaryEntry("A", "B"));
        builder.add(new DictionaryEntry("FF", "BS"));

        helper.addEntity(builder.wrap());
    }


    protected void prepareRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new DictionaryInputListAdapter(mRecyclerView);
        mAdapter.show();
        ImageView iconAdd = (ImageView) findViewById(R.id.icon_add);
        iconAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.addRow();
            }
        });
    }
}

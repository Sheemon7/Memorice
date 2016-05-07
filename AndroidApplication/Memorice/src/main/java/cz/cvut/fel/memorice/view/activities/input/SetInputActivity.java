package cz.cvut.fel.memorice.view.activities.input;

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
import cz.cvut.fel.memorice.model.entities.EntityEnum;
import cz.cvut.fel.memorice.model.entities.builders.Builder;
import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.util.EmptyLabelException;
import cz.cvut.fel.memorice.model.util.EmptyTermException;
import cz.cvut.fel.memorice.model.util.NameAlreadyUsedException;
import cz.cvut.fel.memorice.model.util.TermAlreadyUsedException;
import cz.cvut.fel.memorice.view.fragments.input.SetInputListAdapter;

/**
 * Created by sheemon on 18.4.16.
 */
public class SetInputActivity extends InputActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        Toolbar toolbar =
                (Toolbar) findViewById(R.id.input_toolbar);
        setSupportActionBar(toolbar);
        setColourToStatusBar();

        ImageView icon = (ImageView) findViewById(R.id.icon_type);
        icon.setImageResource(R.drawable.ic_set_white_24dp);

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
                    buildNewSet();
                    finish();
                } catch (NameAlreadyUsedException e) {
                    showLabelUsedDialog(new AlertDialog.Builder(SetInputActivity.this, R.style.AlertDialogTheme));
                } catch (EmptyLabelException e) {
                    showLabelEmptyDialog(new AlertDialog.Builder(SetInputActivity.this, R.style.AlertDialogTheme));
                } catch (TermAlreadyUsedException e) {
                    showValueDuplicateDialog(new AlertDialog.Builder(SetInputActivity.this, R.style.AlertDialogTheme));
                } catch (EmptyTermException e) {
                    showValueEmptyDialog(new AlertDialog.Builder(SetInputActivity.this, R.style.AlertDialogTheme));
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void buildNewSet() throws NameAlreadyUsedException, EmptyLabelException, TermAlreadyUsedException, EmptyTermException {
        EditText labelInput = (EditText) findViewById(R.id.entity_type);
        String label = labelInput.getText().toString();
        SQLiteHelper helper = new SQLiteHelper(getApplicationContext());
        if (label.length() == 0) {
            throw new EmptyLabelException();
        } else if (helper.getEntity(label) != null) {
            throw new NameAlreadyUsedException();
        }
        Builder builder = Builder.getCorrectBuilder(EntityEnum.SET);
        builder.init(label);

        try {
            for (Entry e: (ArrayList<Entry>) mAdapter.getInput()) {
                builder.add(e);
            }
        } catch (TermAlreadyUsedException | EmptyTermException e) {
            mAdapter.emphasizeErrors();
            builder.wrap();
            throw e;
        }
        addEntityToDatabase(builder.wrap());
    }

    protected void prepareRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SetInputListAdapter(mRecyclerView);
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

}

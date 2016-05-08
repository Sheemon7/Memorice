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
import cz.cvut.fel.memorice.model.entities.entries.SequenceEntry;
import cz.cvut.fel.memorice.model.util.EmptyLabelException;
import cz.cvut.fel.memorice.model.util.EmptyTermException;
import cz.cvut.fel.memorice.model.util.NameAlreadyUsedException;
import cz.cvut.fel.memorice.model.util.TermAlreadyUsedException;
import cz.cvut.fel.memorice.view.fragments.input.EntryInputListAdapter;
import cz.cvut.fel.memorice.view.fragments.input.SequenceInputListAdapter;

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



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_save:
                try {
                    buildNewSequence();
                    finish();
                } catch (NameAlreadyUsedException e) {
                    showLabelUsedDialog(new AlertDialog.Builder(SequenceInputActivity.this, R.style.AlertDialogTheme));
                } catch (EmptyLabelException e) {
                    showLabelEmptyDialog(new AlertDialog.Builder(SequenceInputActivity.this, R.style.AlertDialogTheme));
                } catch (TermAlreadyUsedException e) {
                    showValueDuplicateDialog(new AlertDialog.Builder(SequenceInputActivity.this, R.style.AlertDialogTheme));
                } catch (EmptyTermException e) {
                    showValueEmptyDialog(new AlertDialog.Builder(SequenceInputActivity.this, R.style.AlertDialogTheme));
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void buildNewSequence() throws NameAlreadyUsedException, EmptyLabelException, TermAlreadyUsedException, EmptyTermException {
        EditText labelInput = (EditText) findViewById(R.id.entity_type);
        String label = labelInput.getText().toString().trim();
        SQLiteHelper helper = new SQLiteHelper(getApplicationContext());
        if (label.length() == 0) {
            throw new EmptyLabelException();
        } else if (helper.getEntity(label) != null) {
            throw new NameAlreadyUsedException();
        }
        Builder builder = Builder.getCorrectBuilder(EntityEnum.SEQUENCE);
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


    @Override
    protected EntryInputListAdapter getCorrectAdapter(RecyclerView view) {
        return new SequenceInputListAdapter(view);
    }
}

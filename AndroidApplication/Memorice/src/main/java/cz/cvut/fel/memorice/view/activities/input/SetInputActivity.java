package cz.cvut.fel.memorice.view.activities.input;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.entities.EntityEnum;
import cz.cvut.fel.memorice.model.util.EmptyLabelException;
import cz.cvut.fel.memorice.model.util.EmptyDefinitionException;
import cz.cvut.fel.memorice.model.util.NameAlreadyUsedException;
import cz.cvut.fel.memorice.model.util.DefinitionAlreadyUsedException;
import cz.cvut.fel.memorice.view.fragments.input.EntryInputListAdapter;
import cz.cvut.fel.memorice.view.fragments.input.SetInputListAdapter;

/**
 * This activity extends InputActivity. It shows appropriate input method for taking user input for
 * set entity
 */
public class SetInputActivity extends InputActivity {

    private static final Logger LOG = Logger.getLogger(SequenceInputActivity.class.getName());

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_save:
                try {
                    EditText labelInput = (EditText) findViewById(R.id.entity_type);
                    String label = labelInput.getText().toString().trim();
                    mAdapter.buildNew(label, EntityEnum.SET);
                    finish();
                } catch (NameAlreadyUsedException e) {
                    LOG.log(Level.INFO, "NameAlreadyUsed!", e);
                    showLabelUsedDialog(new AlertDialog.Builder(SetInputActivity.this, R.style.AlertDialogTheme));
                } catch (EmptyLabelException e) {
                    LOG.log(Level.INFO, "EmptyLabel!", e);
                    showLabelEmptyDialog(new AlertDialog.Builder(SetInputActivity.this, R.style.AlertDialogTheme));
                } catch (DefinitionAlreadyUsedException e) {
                    LOG.log(Level.INFO, "DefinitionAlreadyUsed!", e);
                    showValueDuplicateDialog(new AlertDialog.Builder(SetInputActivity.this, R.style.AlertDialogTheme));
                } catch (EmptyDefinitionException e) {
                    LOG.log(Level.INFO, "EmptyDefinition!", e);
                    showValueEmptyDialog(new AlertDialog.Builder(SetInputActivity.this, R.style.AlertDialogTheme));
                }
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected EntryInputListAdapter getCorrectAdapter(RecyclerView view) {
        return new SetInputListAdapter(view);
    }
}

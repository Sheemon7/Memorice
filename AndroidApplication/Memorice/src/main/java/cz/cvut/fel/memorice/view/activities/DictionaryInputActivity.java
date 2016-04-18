package cz.cvut.fel.memorice.view.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.database.SQLiteHelper;
import cz.cvut.fel.memorice.model.entities.builders.DictionaryBuilder;
import cz.cvut.fel.memorice.model.util.NameAlreadyUsedException;

/**
 * Created by sheemon on 18.4.16.
 */
public class DictionaryInputActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_input);

        Toolbar toolbar =
                (Toolbar) findViewById(R.id.input_toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_cross_white_24dp);
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
                    showLabelUsedDialog();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void buildNewDictionary() throws NameAlreadyUsedException {
        EditText labelInput = (EditText) findViewById(R.id.entry_title);
        String label = labelInput.getText().toString();
        SQLiteHelper helper = new SQLiteHelper(getApplicationContext());
        if (helper.getEntity(label) != null) {
            throw new NameAlreadyUsedException();
        }
        DictionaryBuilder builder = DictionaryBuilder.getInstance();
        builder.init(label);
        helper.addEntity(builder.wrap());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_input, menu);
        return true;
    }

    protected void showLabelUsedDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DictionaryInputActivity.this);
        alertDialogBuilder.setTitle("Name already used!");
        alertDialogBuilder.setMessage("Please insert another one");
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


}

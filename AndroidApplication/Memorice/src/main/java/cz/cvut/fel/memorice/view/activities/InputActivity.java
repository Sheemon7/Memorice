package cz.cvut.fel.memorice.view.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.database.SQLiteHelper;
import cz.cvut.fel.memorice.model.entities.EntityEnum;
import cz.cvut.fel.memorice.model.entities.builders.Builder;

/**
 * Created by sheemon on 18.4.16.
 */
public class InputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        Toolbar toolbar =
                (Toolbar) findViewById(R.id.input_toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_remove_24dp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: // Intercept the click on the home button
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_input, menu);
        return true;
    }

    protected void showLabelUsedDialog(final EntityEnum type) {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(InputActivity.this);
        View promptView = layoutInflater.inflate(R.layout.label_name_used_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(InputActivity.this);
        alertDialogBuilder.setTitle("Label");
        alertDialogBuilder.setMessage("Insert label");
        alertDialogBuilder.setView(promptView);


        final EditText editText = (EditText) promptView.findViewById(R.id.input_text);
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SQLiteHelper db = new SQLiteHelper(getApplicationContext());
                        //TODO move jinam
                        Builder b = Builder.getCorrectBuilder(type);
                        b.init(editText.getText().toString());
                        db.addEntity(b.wrap());
                        Toast.makeText(getApplicationContext(), "Hello, " + editText.getText(), Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


}

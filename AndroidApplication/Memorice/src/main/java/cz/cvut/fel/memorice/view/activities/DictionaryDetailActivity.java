package cz.cvut.fel.memorice.view.activities;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.database.SQLiteHelper;
import cz.cvut.fel.memorice.model.entities.Dictionary;
import cz.cvut.fel.memorice.model.entities.Group;
import cz.cvut.fel.memorice.model.entities.builders.DictionaryBuilder;
import cz.cvut.fel.memorice.model.entities.entries.DictionaryEntry;
import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.util.EmptyNameException;
import cz.cvut.fel.memorice.model.util.NameAlreadyUsedException;

/**
 * Created by sheemon on 18.4.16.
 */
public class DictionaryDetailActivity extends DetailActivity{

    private Dictionary d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dictionary_detail);

        Toolbar toolbar =
                (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorInvertDarker));
        }

        d = (Dictionary) getIntent().getSerializableExtra("entity");
        TextView label = (TextView) findViewById(R.id.entry_title);
        String testName = d.getName();
        for (DictionaryEntry entry : (Iterable<DictionaryEntry>) d) {
            testName += entry.getValue();
        }
        label.setText(testName);
//        label.setText(s.getName());

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }
}

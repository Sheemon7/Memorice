package cz.cvut.fel.memorice.view.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.entities.Group;
import cz.cvut.fel.memorice.model.entities.Sequence;
import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.entities.entries.SequenceEntry;

/**
 * Created by sheemon on 24.4.16.
 */
public class SetDetailActivity extends DetailActivity {

    private Group g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_set_detail);

        Toolbar toolbar =
                (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorInvertDarker));
        }

        g = (Group) getIntent().getSerializableExtra("entity");
        TextView label = (TextView) findViewById(R.id.entry_title);
        String testName = g.getName();
        for (Entry entry : (Iterable<Entry>) g) {
            testName += entry.getValue();
        }
        label.setText(testName);
//        label.setText(s.getName());

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

}

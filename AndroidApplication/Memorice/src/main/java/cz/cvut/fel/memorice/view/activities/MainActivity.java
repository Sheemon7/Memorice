package cz.cvut.fel.memorice.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.database.helpers.SQLiteHelper;

public class MainActivity extends AppCompatActivity {

    private final static String statsStub = "So far, there are <b>%s</b> entries in <b>%s</b> datasets.<br/>Let's add some more!";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.btn_my_sets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, EntityViewActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        ImageView rice = (ImageView) findViewById(R.id.rice_pic);
        rice.setImageBitmap(
                BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.rice_chopsticks_bowl, 300, 300));

        setStatsText();
    }

    private void setStatsText() {
        SQLiteHelper helper = new SQLiteHelper(getApplicationContext());
        TextView stats = (TextView) findViewById(R.id.welcome_stats);
        stats.setText(Html.fromHtml(String.format(statsStub, helper.countEntities(), helper.countEntries())));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setStatsText();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent;
        switch (item.getItemId()) {
            case R.id.action_settings:
                myIntent = new Intent(MainActivity.this, SettingsActivity.class);
                MainActivity.this.startActivity(myIntent);
                return true;
            case R.id.action_help:
                myIntent = new Intent(MainActivity.this, HelpActivity.class);
                MainActivity.this.startActivity(myIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

package cz.cvut.fel.memorice.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.database.helpers.SQLiteHelper;

/**
 * Main Activity is responsible for viewing user stats and navigation to other parts of the application.
 */
public class MainActivity extends AppCompatActivity {

    private static final String statsStub = "So far, there are <b>%s</b> entries in <b>%s</b> datasets.<br/>Let's add some more!";

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setStatsText();
        prepareMySetsButton();
        prepareRiceImg();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onResume() {
        super.onResume();
        setStatsText();
    }

    private void prepareRiceImg() {
        ImageView rice = (ImageView) findViewById(R.id.rice_pic);
        rice.setImageBitmap(
                BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.rice_chopsticks_bowl, 300, 300));
    }

    private void prepareMySetsButton() {
        Button btnMySets = (Button) findViewById(R.id.btn_my_sets);
        btnMySets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, EntityViewActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
    }

    private void setStatsText() {
        SQLiteHelper helper = new SQLiteHelper(getApplicationContext());
        TextView stats = (TextView) findViewById(R.id.welcome_stats);
        if (stats != null) {
            stats.setText(Html.fromHtml(String.format(statsStub, helper.countEntries(), helper.countEntities())));
        }
    }
}

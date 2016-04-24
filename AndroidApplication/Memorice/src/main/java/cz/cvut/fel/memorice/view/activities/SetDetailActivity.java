package cz.cvut.fel.memorice.view.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

/**
 * Created by sheemon on 24.4.16.
 */
public class SetDetailActivity extends DetailActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

}

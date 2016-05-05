package cz.cvut.fel.memorice.view.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.database.dataaccess.ASyncSimpleReadDatabase;
import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.view.fragments.EntityDetailListAdapter;

/**
 * Created by sheemon on 24.4.16.
 */
public class DetailActivity extends AppCompatActivity {

    private Entity entity;
    private Menu menu;

    protected RecyclerView mRecyclerView;
    protected EntityDetailListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }

    protected void prepareToolbar() {
        Toolbar toolbar =
                (Toolbar) findViewById(R.id.detail_toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        setSupportActionBar(toolbar);
        setTitle(entity.getName());
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorInvertDarker));
        }
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        if (getEntity().isFavourite()) {
            menu.getItem(0).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_fav_white_fill_24dp, null));
        } else {
            menu.getItem(0).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_fav_outline_24dp, null));
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ASyncSimpleReadDatabase access = new ASyncSimpleReadDatabase(getApplicationContext());
        access.setEntity(entity);
        switch (item.getItemId()) {
            case R.id.action_delete:
                access.execute(ASyncSimpleReadDatabase.DELETE_ENTITY);
                finish();
                return true;
            case R.id.action_favourite:
                if (!entity.isFavourite()) {
                    menu.getItem(0).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_fav_white_fill_24dp, null));
                } else {
                    menu.getItem(0).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_fav_outline_24dp, null));
                }
                access.execute(ASyncSimpleReadDatabase.TOGGLE_FAVOURITE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity e) {
        this.entity = e;
    }
}

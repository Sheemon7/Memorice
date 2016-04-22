package cz.cvut.fel.memorice.view.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.database.SQLiteHelper;
import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.view.fragments.DividerItemDecoration;
import cz.cvut.fel.memorice.view.fragments.EntityListAdapter;

/**
 * Created by sheemon on 14.4.16.
 */
public class EntryActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private EntityListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionsMenu fabMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        Toolbar toolbar =
                (Toolbar) findViewById(R.id.entry_toolbar);
        setSupportActionBar(toolbar);


        prepareRecyclerView();
        prepareFAB();
        prepareShadowView();

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void prepareShadowView() {
        findViewById(R.id.shadowView).bringToFront();
        findViewById(R.id.shadowView).setAlpha(0.6f);
        findViewById(R.id.fab_menu).bringToFront();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        fabMenu.collapse();
        prepareShadowView();
        mAdapter.showAll(getApplicationContext());
    }

    private void prepareRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    fabMenu.collapse();
                    AlphaAnimation animation1 = new AlphaAnimation(1, 0);
                    animation1.setDuration(500);
                    animation1.setStartOffset(150);
                    animation1.setFillAfter(true);
                    fabMenu.startAnimation(animation1);
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    AlphaAnimation animation1 = new AlphaAnimation(0, 1);
                    animation1.setDuration(500);
                    animation1.setFillAfter(true);
                    fabMenu.startAnimation(animation1);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new EntityListAdapter(mRecyclerView);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getApplicationContext(), R.drawable.separator));
        mAdapter.showAll(getApplicationContext());
    }

    private void prepareFAB() {
        fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        fabMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                findViewById(R.id.shadowView).setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                findViewById(R.id.shadowView).setVisibility(View.INVISIBLE);
            }
        });
        findViewById(R.id.fab_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(EntryActivity.this, SequenceInputActivity.class);
                EntryActivity.this.startActivity(myIntent);
            }
        });
        findViewById(R.id.fab_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(EntryActivity.this, SetInputActivity.class);
                EntryActivity.this.startActivity(myIntent);
            }
        });
        findViewById(R.id.fab_dictionary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(EntryActivity.this, DictionaryInputActivity.class);
                EntryActivity.this.startActivity(myIntent);
            }
        });
        fabMenu.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sets, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return onQueryTextChange(query);
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.filter(newText, getApplicationContext());
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent;
        switch (item.getItemId()) {
            case R.id.action_search:
                Toast.makeText(getApplicationContext(), "Search pressed", Toast.LENGTH_SHORT).show();
                //TODO - search action
                return true;
            case R.id.action_settings:
                myIntent = new Intent(EntryActivity.this, SettingsActivity.class);
                EntryActivity.this.startActivity(myIntent);
                return true;
            case android.R.id.home: // Intercept the click on the home button
                finish();
                return true;
            case R.id.action_help:
                myIntent = new Intent(EntryActivity.this, HelpActivity.class);
                EntryActivity.this.startActivity(myIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }






}

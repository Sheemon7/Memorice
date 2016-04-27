package cz.cvut.fel.memorice.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.view.fragments.DividerItemDecoration;
import cz.cvut.fel.memorice.view.fragments.EntityListAdapter;

/**
 * Created by sheemon on 14.4.16.
 */
public class EntityViewActivity extends AppCompatActivity {

    private static final int FAB_ANIMATION_DURATION = 300;
    private static final int FAB_ANIMATION_OFFSET = 150;

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

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


//        // Specify that tabs should be displayed in the action bar.
//        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//
//        // Create a tab listener that is called when the user changes tabs.
//        TabListener tabListener = new TabListener() {
//            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
//                // show the given tab
//            }
//
//            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
//                // hide the given tab
//            }
//
//            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
//                // probably ignore this event
//            }
//        };
//
//        // Add 3 tabs, specifying the tab's text and TabListener
//        for (int i = 0; i < 3; i++) {
//            ab.addTab(
//                    ab.newTab()
//                            .setText("Tab " + (i + 1))
//                            .setTabListener(tabListener));
//        }
    }

    private void prepareShadowView() {
        findViewById(R.id.shadowView).bringToFront();
        findViewById(R.id.shadowView).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                fabMenu.collapse();
                return true;
            }
        });
        findViewById(R.id.shadowView).setAlpha(0.6f);
        findViewById(R.id.fab_menu).bringToFront();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        //TODO thread?
//        findViewById(R.id.shadowView).setAlpha(0f);
//        fabMenu.collapse();
//        findViewById(R.id.shadowView).setAlpha(0.6f);
        mAdapter.showAll(getApplicationContext());
    }

    @Override
    protected void onResume() {
        //TODO
        super.onResume();
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
                    animation1.setDuration(FAB_ANIMATION_DURATION);
                    animation1.setStartOffset(FAB_ANIMATION_OFFSET);
                    animation1.setFillAfter(true);
                    fabMenu.startAnimation(animation1);
                    //todo
                    getSupportActionBar().hide();
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    AlphaAnimation animation1 = new AlphaAnimation(0, 1);
                    animation1.setDuration(FAB_ANIMATION_DURATION);
                    animation1.setFillAfter(true);
                    fabMenu.startAnimation(animation1);
                    getSupportActionBar().show();
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
                findViewById(R.id.shadowView).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        fabMenu.collapse();
                        return true;
                    }
                });
                AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setDuration(FAB_ANIMATION_DURATION);
                fadeIn.setFillAfter(true);
                View shadowView = findViewById(R.id.shadowView);
                shadowView.startAnimation(fadeIn);
            }

            @Override
            public void onMenuCollapsed() {
                AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
                fadeOut.setDuration(FAB_ANIMATION_DURATION);
                fadeOut.setFillAfter(true);
                View shadowView = findViewById(R.id.shadowView);
                shadowView.startAnimation(fadeOut);
                shadowView.setOnTouchListener(null);
            }
        });
        findViewById(R.id.fab_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(EntityViewActivity.this, SequenceInputActivity.class);
                EntityViewActivity.this.startActivity(myIntent);
            }
        });
        findViewById(R.id.fab_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(EntityViewActivity.this, SetInputActivity.class);
                EntityViewActivity.this.startActivity(myIntent);
            }
        });
        findViewById(R.id.fab_dictionary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(EntityViewActivity.this, DictionaryInputActivity.class);
                EntityViewActivity.this.startActivity(myIntent);
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
            case R.id.action_settings:
                myIntent = new Intent(EntityViewActivity.this, SettingsActivity.class);
                EntityViewActivity.this.startActivity(myIntent);
                return true;
            case android.R.id.home: // Intercept the click on the home button
                finish();
                return true;
            case R.id.action_help:
                myIntent = new Intent(EntityViewActivity.this, HelpActivity.class);
                EntityViewActivity.this.startActivity(myIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }






}

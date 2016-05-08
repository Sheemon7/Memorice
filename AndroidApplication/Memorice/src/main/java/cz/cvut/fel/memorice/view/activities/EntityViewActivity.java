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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.view.activities.input.DictionaryInputActivity;
import cz.cvut.fel.memorice.view.activities.input.SequenceInputActivity;
import cz.cvut.fel.memorice.view.activities.input.SetInputActivity;
import cz.cvut.fel.memorice.view.fragments.DividerItemDecoration;
import cz.cvut.fel.memorice.view.fragments.EntityListAdapter;

/**
 * This activity is responsible for viewing list of datasets. It is basically the main part of the
 * application.
 */
public class EntityViewActivity extends AppCompatActivity {
    private static final Logger LOG = Logger.getLogger(EntityViewActivity.class.getName());

    private static final long ANIMATION_DURATION = 200;
    private static final long FAB_HIDE_DURATION = 3000;
    private static final long FAB_ANIMATION_OFFSET = 150;

    private RecyclerView mRecyclerView;
    private EntityListAdapter mAdapter;
    private FloatingActionsMenu fabMenu;

    private Thread fabHideThread;
    private View shadowView;

    /* listeners */
    private RecyclerView.OnScrollListener onScrollListener = new CustomOnScrollListener();
    private View.OnClickListener onClickListener = new CustomOnClickListener();
    private SearchView.OnCloseListener onCloseListener = new CustomOnCloseListener();
    private SearchView.OnQueryTextListener onQueryTextListener = new CustomOnQueryTextChangeListener();
    private FloatingActionsMenu.OnFloatingActionsMenuUpdateListener onFloatingActionsMenuUpdateListener = new CustomOnFloatingActionsMenuChangeListener();

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_list);
        Toolbar toolbar =
                (Toolbar) findViewById(R.id.entry_toolbar);
        setSupportActionBar(toolbar);

        prepareSwitch();
        prepareRecyclerView();
        prepareFAB();
        prepareFABHideThread();
        fabHideThread.start();

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.showAll(getApplicationContext());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sets, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnSearchClickListener(onClickListener);
        searchView.setOnCloseListener(onCloseListener);
        searchView.setOnQueryTextListener(onQueryTextListener);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent;
        switch (item.getItemId()) {
            case R.id.action_settings:
                myIntent = new Intent(EntityViewActivity.this, SettingsActivity.class);
                EntityViewActivity.this.startActivity(myIntent);
                return true;
            case android.R.id.home:
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

    /**
     * Prepares switch responsible for switching between all/favourite-only views
     */
    private void prepareSwitch() {
        final Switch switchFav = (Switch) findViewById(R.id.switch_fav);
        final ImageView indicator = (ImageView) findViewById(R.id.fav_indicator);
        if (switchFav != null) {
            switchFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mAdapter.showFavorites(getApplicationContext());
                        indicator.setImageResource(R.drawable.ic_fav_white_fill_24dp);
                    } else {
                        mAdapter.showAll(getApplicationContext());
                        indicator.setImageResource(R.drawable.ic_fav_outline_24dp);
                    }
                }
            });
        }
    }

    /**
     * Set ups thread responsible for hiding floating action button after some time
     */
    private void prepareFABHideThread() {
        if (fabHideThread != null && fabHideThread.isAlive()) {
            fabHideThread.interrupt();
        }
        fabHideThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(FAB_HIDE_DURATION);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!fabMenu.isExpanded()) {
                                fadeOut(fabMenu);
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    LOG.log(Level.INFO, "Interrupting fab hide thread", e);
                }
            }
        });
    }

    /**
     * Prepares view displayed each time floating action button is expanded
     */
    private void prepareShadowView() {
        shadowView = findViewById(R.id.shadowView);
        shadowView.bringToFront();
        shadowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                fabMenu.collapse();
                return true;
            }
        });
        shadowView.setAlpha(0.6f);
        fabMenu.bringToFront();
    }

    /**
     * Prepares recycler view responsible for viewing list of entities
     */
    private void prepareRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(onScrollListener);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new EntityListAdapter(mRecyclerView);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getApplicationContext(), R.drawable.separator));
        mAdapter.showAll(getApplicationContext());
    }

    /**
     * Prepares floating action button with menu
     */
    private void prepareFAB() {
        fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        fabMenu.setOnFloatingActionsMenuUpdateListener(onFloatingActionsMenuUpdateListener);
        prepareFABActionsMenu();
    }

    /**
     * Prepares floating action button menu, providing it with appropriate listeners
     */
    private void prepareFABActionsMenu() {
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
    }

    /**
     * Fades out view with animation offset specified
     *
     * @param view   view
     * @param offset animation offset
     */
    private void fadeOut(final View view, final long offset) {
        AlphaAnimation fadeOut = new AlphaAnimation(view.getAlpha(), 0);
        fadeOut.setStartOffset(offset);
        carryOutAnimation(view, fadeOut);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(ANIMATION_DURATION + offset);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.setEnabled(false);
                        }
                    });
                } catch (InterruptedException e) {
                    LOG.log(Level.WARNING, "Interrupted fadeOut!", e);
                }
            }
        }).start();
    }

    /**
     * Fades in view with animation offset specified
     *
     * @param view   view
     * @param offset animation offset
     */
    private void fadeIn(View view, long offset) {
        AlphaAnimation fadeIn = new AlphaAnimation(view.getAlpha(), 1);
        fadeIn.setStartOffset(offset);
        carryOutAnimation(view, fadeIn);
        view.setEnabled(true);
    }

    /**
     * Fades in view
     *
     * @param view view
     */
    private void fadeIn(View view) {
        fadeIn(view, 0);
    }

    /**
     * Fades out view
     *
     * @param view view
     */
    private void fadeOut(View view) {
        fadeOut(view, 0);
    }

    /**
     * Carries out provided alpha animation on view
     *
     * @param view      view
     * @param animation animation
     */
    private void carryOutAnimation(View view, AlphaAnimation animation) {
        animation.setDuration(ANIMATION_DURATION);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    /* listeners */

    /**
     * Custom listener defining what happens when user scrolls recycler view.
     * Basically, it occupies with setting up floating action button to hide after a while
     */
    private class CustomOnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                fadeIn(fabMenu, FAB_ANIMATION_OFFSET);
                if (fabHideThread.isAlive()) {
                    fabHideThread.interrupt();
                }
            } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                prepareFABHideThread();
                fabHideThread.start();
            }
        }
    }

    /**
     * Custom listener defining what happens after the user tries to showFiltered the entities
     */
    private class CustomOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mRecyclerView.removeOnScrollListener(onScrollListener);
            if (fabHideThread.isAlive()) {
                fabHideThread.interrupt();
            }
            if (fabMenu.isEnabled()) {
                fadeOut(fabMenu);
            }
        }
    }

    /**
     * Custom listener defining what happens when user stops filtering entities
     */
    private class CustomOnCloseListener implements SearchView.OnCloseListener {

        @Override
        public boolean onClose() {
            mRecyclerView.addOnScrollListener(onScrollListener);
            return false;
        }
    }

    /**
     * Custom listener listening to changes of showFiltered text and displaying results
     */
    private class CustomOnQueryTextChangeListener implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            return onQueryTextChange(query);
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            mAdapter.showFiltered(newText, getApplicationContext());
            return true;
        }
    }

    /**
     * Custom listener defining floating action button behaviour
     */
    private class CustomOnFloatingActionsMenuChangeListener implements FloatingActionsMenu.OnFloatingActionsMenuUpdateListener {

        @Override
        public void onMenuExpanded() {
            prepareShadowView();
            shadowView.setVisibility(View.VISIBLE);
            fadeIn(shadowView);
        }

        @Override
        public void onMenuCollapsed() {
            fadeOut(shadowView);
            shadowView.setVisibility(View.INVISIBLE);
            shadowView.setAlpha(0f);
            if (fabHideThread.isAlive()) {
                fabHideThread.interrupt();
            }
            prepareFABHideThread();
            fabHideThread.start();
        }
    }
}


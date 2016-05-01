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
import android.widget.Switch;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.view.fragments.DividerItemDecoration;
import cz.cvut.fel.memorice.view.fragments.EntityListAdapter;

/**
 * Created by sheemon on 14.4.16.
 */
public class EntityViewActivity extends AppCompatActivity {

    private static final int ANIMATION_DURATION = 200;
    private static final int FAB_HIDE_DURATION = 3000;
    private static final int FAB_ANIMATION_OFFSET = 150;

    private RecyclerView mRecyclerView;
    private EntityListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionsMenu fabMenu;

    private Thread fabHideThread;
    private View shadowView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_list);

        Toolbar toolbar =
                (Toolbar) findViewById(R.id.entry_toolbar);
        setSupportActionBar(toolbar);


        prepareRecyclerView();
        prepareFAB();
        shadowView = findViewById(R.id.shadowView);
        prepareShadowView();
        prepareFABHideThread();
        prepareSwitch();
        fabHideThread.start();

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

    }

    private void prepareSwitch() {
        final Switch switchFav = (Switch) findViewById(R.id.switch_fav);
        if (switchFav != null) {
            switchFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mAdapter.showFavorites(getApplicationContext());
                    } else {
                        mAdapter.showAll(getApplicationContext());
                    }
                }
            });
        }
    }

    private void prepareFABHideThread() {
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
                    return;
                }
            }
        });
    }

    private void prepareShadowView() {
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

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mAdapter.showAll(getApplicationContext());
    }

    @Override
    protected void onResume() {
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
                    fadeIn(fabMenu, FAB_ANIMATION_OFFSET);
                    if (fabHideThread.isAlive()) {
                        fabHideThread.interrupt();
                    }
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (fabHideThread.isAlive()) {
                        fabHideThread.interrupt();
                    }
                    prepareFABHideThread();
                    fabHideThread.start();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                fadeIn(fabMenu, FAB_ANIMATION_OFFSET);
                if (fabHideThread.isAlive()) {
                    fabHideThread.interrupt();
                }
                prepareFABHideThread();
                fabHideThread.start();
                return false;
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
                fadeIn(shadowView);
                shadowView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        fabMenu.collapse();
                        return true;
                    }
                });
            }

            @Override
            public void onMenuCollapsed() {
                shadowView.setOnTouchListener(null);
                fadeOut(shadowView);

                if (fabHideThread.isAlive()) {
                    fabHideThread.interrupt();
                }
                prepareFABHideThread();
                fabHideThread.start();
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
    }

    private void fadeOut(final View view, final int offset) {
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
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void carryOutAnimation(View view, AlphaAnimation animation) {
        animation.setDuration(ANIMATION_DURATION);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    private void fadeIn(View view, int offset) {
        AlphaAnimation fadeOut = new AlphaAnimation(view.getAlpha(), 1);
        fadeOut.setStartOffset(offset);
        carryOutAnimation(view, fadeOut);
        view.setEnabled(true);
    }

    private void fadeIn(View view) {
        fadeIn(view, 0);
    }

    private void fadeOut(View view) {
        fadeOut(view, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sets, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shadowView.setAlpha(0);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                shadowView.setAlpha(0);
                return false;
            }
        });
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


}

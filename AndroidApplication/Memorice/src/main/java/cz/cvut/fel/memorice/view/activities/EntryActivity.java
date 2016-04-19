package cz.cvut.fel.memorice.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.logging.Logger;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.database.SQLiteHelper;
import cz.cvut.fel.memorice.model.entities.Entity;

/**
 * Created by sheemon on 14.4.16.
 */
public class EntryActivity extends AppCompatActivity {
    private static final Logger LOG = Logger.getLogger(EntryActivity.class.getName());

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
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

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }


    private void prepareRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //TODO
                fabMenu.setVisibility(View.VISIBLE);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //TODO
                fabMenu.setVisibility(View.INVISIBLE);
            }
        });


        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        addDataToAdapter();


    }

    private void addDataToAdapter() {
        // specify an adapter (see also next example)
        ArrayList<Entity> mDataset = new SQLiteHelper(getApplicationContext()).getAllEntities();

        mAdapter = new MyAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void prepareFAB() {
//        mRecyclerView.getBackground().setAlpha(0);
        fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        fabMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
//                mRecyclerView.getBackground().setAlpha(255);
                mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                fabMenu.collapse();
                            }
                        }));

            }

            @Override
            public void onMenuCollapsed() {
//                mRecyclerView.getBackground().setAlpha(0);
                mRecyclerView.setOnTouchListener(null);
            }
        });
        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(EntryActivity.this, SequenceInputActivity.class);
                EntryActivity.this.startActivity(myIntent);
            }
        });
        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(EntryActivity.this, SetInputActivity.class);
                EntryActivity.this.startActivity(myIntent);
            }
        });
        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(EntryActivity.this, DictionaryInputActivity.class);
                EntryActivity.this.startActivity(myIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sets, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //TODO -  Configure the search info and add any event listeners...

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Toast.makeText(getApplicationContext(), "Search pressed", Toast.LENGTH_SHORT).show();
                //TODO - search action
                return true;
            case R.id.action_settings:
                Intent myIntent = new Intent(EntryActivity.this, SettingsActivity.class);
                EntryActivity.this.startActivity(myIntent);
            case android.R.id.home: // Intercept the click on the home button
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private ArrayList<Entity> mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            private TextView txtHeader;
            private TextView txtFooter;
            private ImageView imageType;
            private ImageView imageFav;



            public ViewHolder(View v) {
                super(v);
                txtHeader = (TextView) v.findViewById(R.id.firstLine);
                txtFooter = (TextView) v.findViewById(R.id.secondLine);
                imageType = (ImageView) v.findViewById(R.id.icon_type);
                imageFav= (ImageView) v.findViewById(R.id.icon_favorite);
            }
        }

//        public void add(int position, String item) {
//            mDataset.add(position, item);
//            notifyItemInserted(position);
//        }
//
//        public void remove(String item) {
//            int position = mDataset.indexOf(item);
//            mDataset.remove(position);
//            notifyItemRemoved(position);
//        }

        public MyAdapter(ArrayList<Entity> myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // init a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entity_line, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Entity e = mDataset.get(position);
            holder.txtHeader.setText(e.getName());
            holder.txtHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    remove(name);
                }
            });

//            holder.txtFooter.setText("Footer: " + e.getName());

            switch (e.getType()) {
                case GROUP:
                    holder.imageType.setImageResource(R.drawable.ic_set_inverted_24dp);
                    break;
                case SEQUENCE:
                    holder.imageType.setImageResource(R.drawable.ic_list_inverted_24dp);
                    break;
                case DICTIONARY:
                    holder.imageType.setImageResource(R.drawable.ic_dictionary_inverted_24dp);
                    break;
            }

            if (e.isFavourite()) {
                holder.imageFav.setImageResource(R.drawable.ic_favorite_true_24dp);
            } else {
                holder.imageFav.setImageResource(R.drawable.ic_favorite_false_24dp);
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

        //reference na stack overflow
        // http://stackoverflow.com/questions/24471109/recyclerview-onclick
        private OnItemClickListener mListener;

        public interface OnItemClickListener {
            public void onItemClick(View view, int position);
        }

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildPosition(childView));
                return true;
            }
            return false;
        }

        @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

        @Override
        public void onRequestDisallowInterceptTouchEvent (boolean disallowIntercept){}
    }
}

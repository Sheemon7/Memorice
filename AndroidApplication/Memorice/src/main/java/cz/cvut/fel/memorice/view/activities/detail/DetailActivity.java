package cz.cvut.fel.memorice.view.activities.detail;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.database.dataaccess.ASyncEntityRead;
import cz.cvut.fel.memorice.model.database.dataaccess.ASyncSimpleAccessDatabase;
import cz.cvut.fel.memorice.model.database.helpers.SQLiteHelper;
import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.view.fragments.DividerItemDecoration;
import cz.cvut.fel.memorice.view.fragments.detail.EntityDetailListAdapter;
import cz.cvut.fel.memorice.view.fragments.detail.SequenceDetailListAdapter;

/**
 * This activity is responsible for viewing entity detail. It also setups recycler view, which is
 * responsible for entity actions, such as deletion and edit
 */
public abstract class DetailActivity extends AppCompatActivity {

    private Entity entity;
    private String label;
    private Menu menu;

    protected EditText textType;
    protected ImageView iconType;
    private ImageView editIcon;
    private boolean editable = false;

    protected RecyclerView mRecyclerView;
    protected EntityDetailListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        iconType = (ImageView) findViewById(R.id.entity_icon);
        textType = (EditText) findViewById(R.id.entity_type);
        setLabel(getIntent().getStringExtra(getString(R.string.entity_label_resource)));
        prepareToolbar();
        prepareTypeIcon(iconType);
        prepareTypeText(textType);
        prepareRecyclerView();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        ASyncEntityRead access = new ASyncEntityRead(getApplicationContext());
        access.setDetailActivity(this);
        access.execute(getLabel());
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ASyncSimpleAccessDatabase access = new ASyncSimpleAccessDatabase(getApplicationContext());
        access.setEntity(entity);
        switch (item.getItemId()) {
            case R.id.action_delete:
                access.execute(ASyncSimpleAccessDatabase.DELETE_ENTITY);
                finish();
                return true;
            case R.id.action_favourite:
                if (!entity.isFavourite()) {
                    menu.getItem(0).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_fav_white_fill_24dp, null));
                } else {
                    menu.getItem(0).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_fav_outline_24dp, null));
                }
                access.execute(ASyncSimpleAccessDatabase.TOGGLE_FAVOURITE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Returns the entity, which is currently being displayed
     *
     * @return entity
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Sets another entity to display on this activity, it also appropriately changes the
     * activity layout to agree with entity attributes
     *
     * @param entity entity
     */
    public void setEntity(Entity entity) {
        mAdapter.setData(entity.getListOfEntries());
        if (entity.isFavourite()) {
            menu.getItem(0).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_fav_white_fill_24dp, null));
        } else {
            menu.getItem(0).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_fav_outline_24dp, null));
        }
        this.entity = entity;
        mAdapter.setEntity(entity);
    }

    /**
     * Gets label of the entity displayed
     *
     * @return label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label of the entity to the activity
     *
     * @param label new label
     */
    public void setLabel(String label) {
        setTitle(label);
        this.label = label;
    }

    /**
     * Prepares icon indicating type of the entity
     *
     * @param iconType icon
     */
    protected abstract void prepareTypeIcon(ImageView iconType);

    /**
     * Prepares text indicating type of the entity
     *
     * @param textType text
     */
    protected abstract void prepareTypeText(EditText textType);

    /**
     * Provides correct {@link android.widget.ListAdapter} which is capable of viewing the entries
     * of the entity
     *
     * @param view recycler view
     * @return correct adapter
     */
    protected abstract EntityDetailListAdapter provideListAdapter(RecyclerView view);

    /**
     * Prepares toolbar of the activity - sets appropriate color to the status bar and prepares
     * the header of the activity with appropriate listeners
     */
    private void prepareToolbar() {
        Toolbar toolbar =
                (Toolbar) findViewById(R.id.detail_toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        setToolbarColor();
        prepareEditIcon();
    }

    /**
     * Sets toolbar color if the device has complying sdk
     */
    private void setToolbarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorInvertDark));
        }
    }

    /**
     * Prepares edit icon to be displayed
     */
    private void prepareEditIcon() {
        editIcon = (ImageView) findViewById(R.id.editable_icon);
        editIcon.setImageResource(R.drawable.ic_edit_white_24dp);
        editIcon.setOnClickListener(new InitEditListener(new SQLiteHelper(getApplicationContext())));
    }

    /**
     * Hides keyboard input at the beginning of the activity (edit text gets focused and keyboard
     * is displayed)
     */
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Prepares recycler view to display entries of the activity
     */
    private void prepareRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getApplicationContext(), R.drawable.separator));
        mAdapter = provideListAdapter(mRecyclerView);
    }

    /**
     * This listener is responsible for handling entity label change, displaying correct imgae at the icon
     * and writing changes to the database
     */
    private class InitEditListener implements View.OnClickListener {

        private SQLiteHelper helper;
        private String oldLabel;

        /**
         * Initiates listener with {@link SQLiteHelper} instance provided
         *
         * @param helper helper
         */
        public InitEditListener(SQLiteHelper helper) {
            this.helper = helper;
        }

        /**
         * Handles on click event - reaction depends on the edit state of the fragment
         *
         * @param v view
         */
        @Override
        public void onClick(View v) {
            editable = !editable;
            if (editable) {
                prepareViewToEdit();
            } else {
                String newLabel = textType.getText().toString().trim();
                if (isLabelOkay(newLabel, oldLabel)) {
                    if (!newLabel.equals(oldLabel)) {
                        changeLabel(newLabel);
                    }
                    textType.setEnabled(false);
                    prepareTypeText(textType);
                    editIcon.setImageResource(R.drawable.ic_edit_white_24dp);
                    hideKeyboard();
                } else {
                    editable = true;
                    showErrorSign(newLabel);
                }
            }
            mAdapter.setEditable(editable);
        }

        /**
         * Changes entity's label
         *
         * @param text text
         */
        private void changeLabel(String text) {
            entity.setLabel(text);
            setLabel(text);
            writeNewLabelToDatabase(oldLabel);
        }

        /**
         * Set ups all components of the view to the editable state
         */
        private void prepareViewToEdit() {
            textType.setEnabled(true);
            textType.setText(entity.getLabel());
            oldLabel = entity.getLabel();
            editIcon.setImageResource(R.drawable.ic_done_white_24dp);
        }

        /**
         * Writes new label to the database
         *
         * @param oldLabel old label of the entity
         */
        private void writeNewLabelToDatabase(String oldLabel) {
            ASyncSimpleAccessDatabase access = new ASyncSimpleAccessDatabase(getApplicationContext());
            access.setOldValue(oldLabel);
            access.setEntity(entity);
            access.execute(ASyncSimpleAccessDatabase.RENAME_ENTITY);
        }

        /**
         * Decides whether the new name is compliant - there must be no duplicate in the database
         * and the name cannot be empty as well
         *
         * @param newLabel new label
         * @param oldLabel old label
         * @return true if the new label is possible else false
         */
        private boolean isLabelOkay(String newLabel, String oldLabel) {
            return newLabel.equals(oldLabel) || helper.getEntity(newLabel) == null && !newLabel.equals("");
        }

        /**
         * Displays error sign if the new label is not compliant
         *
         * @param newLabel new label
         */
        private void showErrorSign(String newLabel) {
            String errorString;
            if (newLabel.equals("")) {
                errorString = "Name cannot be empty!";
            } else {
                errorString = "Name is already used!";
            }
            int color = ContextCompat.getColor(getApplicationContext(), R.color.pink);
            ForegroundColorSpan fgcspan = new ForegroundColorSpan(color);
            SpannableStringBuilder builder = new SpannableStringBuilder(errorString);
            builder.setSpan(fgcspan, 0, errorString.length(), 0);
            textType.setError(builder);
        }
    }
}

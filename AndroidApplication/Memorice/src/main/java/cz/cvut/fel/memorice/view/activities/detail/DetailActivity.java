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
 * Created by sheemon on 24.4.16.
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        ASyncEntityRead access = new ASyncEntityRead(getApplicationContext());
        access.setDetailActivity(this);
        access.execute(getLabel());
        return true;
    }

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

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity e) {
        mAdapter.setData(e.getListOfEntries());
        if (e.isFavourite()) {
            menu.getItem(0).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_fav_white_fill_24dp, null));
        } else {
            menu.getItem(0).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_fav_outline_24dp, null));
        }
        this.entity = e;
        mAdapter.setEntity(e);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        setTitle(label);
        this.label = label;
    }

    protected abstract void prepareTypeIcon(ImageView iconType);

    protected abstract void prepareTypeText(EditText textType);

    protected abstract EntityDetailListAdapter provideListAdapter(RecyclerView view);

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

    private void setToolbarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorInvertDark));
        }
    }

    private void prepareEditIcon() {
        editIcon = (ImageView) findViewById(R.id.editable_icon);
        editIcon.setImageResource(R.drawable.ic_edit_white_24dp);
        editIcon.setOnClickListener(new InitEditListener(new SQLiteHelper(getApplicationContext())));
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void prepareRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getApplicationContext(), R.drawable.separator));
        mAdapter = provideListAdapter(mRecyclerView);
    }

    private class InitEditListener implements View.OnClickListener {

        private SQLiteHelper helper;
        private String oldLabel;

        public InitEditListener(SQLiteHelper helper) {
            this.helper = helper;
        }

        @Override
        public void onClick(View v) {
            editable = !editable;
            if (editable) {
                prepareViewToEdit();
            } else {
                String text = textType.getText().toString().trim();
                if (isLabelOkay(text, oldLabel)) {
                    if (text != oldLabel) {
                        changeLabel(text);
                    }
                    textType.setEnabled(false);
                    prepareTypeText(textType);
                    editIcon.setImageResource(R.drawable.ic_edit_white_24dp);
                    hideKeyboard();
                } else {
                    editable = true;
                    showErrorSign(text);
                }
            }
            mAdapter.setEditable(editable);
        }

        private void changeLabel(String text) {
            entity.setLabel(text);
            setLabel(text);
            writeNewLabelToDatabase(oldLabel);
        }

        private void prepareViewToEdit() {
            textType.setEnabled(true);
            textType.setText(entity.getLabel());
            oldLabel = entity.getLabel();
            editIcon.setImageResource(R.drawable.ic_done_white_24dp);
        }

        private void writeNewLabelToDatabase(String oldLabel) {
            ASyncSimpleAccessDatabase access = new ASyncSimpleAccessDatabase(getApplicationContext());
            access.setOldValue(oldLabel);
            access.setEntity(entity);
            access.execute(ASyncSimpleAccessDatabase.RENAME_ENTITY);
        }

        private boolean isLabelOkay(String text, String oldLabel) {
            return text.equals(oldLabel) || helper.getEntity(text) == null && !text.equals("");
        }

        private void showErrorSign(String text) {
            String errorString;
            if (text.equals("")) {
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

package cz.cvut.fel.memorice.view.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.database.SQLiteHelper;
import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.model.util.WrongNameException;

/**
 * Created by sheemon on 24.4.16.
 */
public class DetailActivity extends AppCompatActivity {

    private Entity entity;

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
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final SQLiteHelper helper = new SQLiteHelper(getApplicationContext());
        switch (item.getItemId()) {
            case R.id.action_delete:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getApplicationContext());
                alertDialogBuilder.setTitle("Delete " + getEntity().getName());
                alertDialogBuilder.setMessage("Are you sure?");
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        helper.deleteEntity(getEntity());
                        dialog.cancel();
                        finish();
                    }
                });
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
                return true;
            case R.id.action_favourite:
                try {
                    helper.toggleFavorite(getEntity());
                    ImageView favIcon = (ImageView) findViewById(R.id.icon_favorite);
                    if (getEntity().isFavourite()) {
                        favIcon.setImageResource(R.drawable.ic_favorite_true_pink_24dp);
                    } else {
                        favIcon.setImageResource(R.drawable.ic_favorite_false_24dp);
                    }
                } catch (WrongNameException e) {
                    e.printStackTrace();
                }
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

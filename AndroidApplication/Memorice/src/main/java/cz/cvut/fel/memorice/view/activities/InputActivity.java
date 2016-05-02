package cz.cvut.fel.memorice.view.activities;

import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.database.SQLiteHelper;
import cz.cvut.fel.memorice.view.fragments.EntryInputListAdapter;

/**
 * Created by sheemon on 19.4.16.
 */
public abstract class InputActivity extends AppCompatActivity {

    protected EditText labelInput;
    protected TextView labelWarn;
    protected RecyclerView mRecyclerView;
    protected EntryInputListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_input, menu);
        return true;
    }

    protected void showLabelUsedDialog(AlertDialog.Builder alertDialogBuilder) {
        alertDialogBuilder.setMessage("Please insert another one");
        alertDialogBuilder.setTitle(getString(R.string.used_name_quote));
        showDialog(alertDialogBuilder);
    }

    protected void showLabelEmptyDialog(AlertDialog.Builder alertDialogBuilder) {
        alertDialogBuilder.setTitle(getString(R.string.empty_name_quote));
        alertDialogBuilder.setMessage("Please insert another one");
        showDialog(alertDialogBuilder);
    }

    protected void showValueDuplicateDialog(AlertDialog.Builder alertDialogBuilder) {
        alertDialogBuilder.setTitle(getString(R.string.duplicate_quote));
        showDialog(alertDialogBuilder);
    }

    protected void showValueEmptyDialog(AlertDialog.Builder alertDialogBuilder) {
        alertDialogBuilder.setTitle(getString(R.string.empty_value));
        showDialog(alertDialogBuilder);
    }

    protected void showDefinitionDuplicateDialog(AlertDialog.Builder alertDialogBuilder) {
        alertDialogBuilder.setTitle(getString(R.string.duplicate_def_quote));
        showDialog(alertDialogBuilder);
    }

    private void showDialog(AlertDialog.Builder alertDialogBuilder) {
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    protected void prepareInputLabels() {
        labelInput = (EditText) findViewById(R.id.entity_type);
        labelWarn = (TextView) findViewById(R.id.text_used_label);
        labelWarn.setVisibility(View.INVISIBLE);
        labelInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String label = s.toString().trim();
                SQLiteHelper h = new SQLiteHelper(getApplicationContext());
                if (h.getEntity(label) != null) {
                    labelWarn.setText(R.string.used_label);
                    labelWarn.setVisibility(View.VISIBLE);
                } else if (s.toString().length() == 0) {
                    labelWarn.setText(R.string.empty_label);
                    labelWarn.setVisibility(View.VISIBLE);
                } else {
                    labelWarn.setText("");
                    labelWarn.setVisibility(View.GONE);
                }
            }
        });
        labelInput.requestFocus();
    }

    protected void setColourToStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorInvertDarker));
        }
    }
}

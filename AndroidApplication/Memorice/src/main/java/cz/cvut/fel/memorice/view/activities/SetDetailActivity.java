package cz.cvut.fel.memorice.view.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.entities.EntityEnum;
import cz.cvut.fel.memorice.model.entities.Group;
import cz.cvut.fel.memorice.model.entities.entries.Entry;

/**
 * Created by sheemon on 24.4.16.
 */
public class SetDetailActivity extends DetailActivity {

    private Group g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEntity((Group) getIntent().getSerializableExtra("entity"));
        setContentView(R.layout.activity_detail);
        prepareToolbar();

        ImageView icon = (ImageView) findViewById(R.id.entity_icon);
        icon.setImageResource(R.drawable.ic_set_white_24dp);
        TextView text = (TextView) findViewById(R.id.entity_type);
        text.setText(EntityEnum.GROUP.getName());

        TextView label = (TextView) findViewById(R.id.entity_type);
//        String testName = "";
//        for (Entry entry : (Iterable<Entry>) getEntity()) {
//            testName += entry.getValue();
//        }
        label.setText(EntityEnum.GROUP.getName());
//        label.setText(s.getName());

    }

}

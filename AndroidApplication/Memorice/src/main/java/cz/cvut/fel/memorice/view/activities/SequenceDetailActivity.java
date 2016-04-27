package cz.cvut.fel.memorice.view.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.entities.EntityEnum;
import cz.cvut.fel.memorice.model.entities.Sequence;
import cz.cvut.fel.memorice.model.entities.entries.SequenceEntry;

/**
 * Created by sheemon on 24.4.16.
 */
public class SequenceDetailActivity extends DetailActivity {

    private Sequence s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEntity((Sequence) getIntent().getSerializableExtra("entity"));
        setContentView(R.layout.activity_detail);
        prepareToolbar();

        ImageView icon = (ImageView) findViewById(R.id.entity_icon);
        icon.setImageResource(R.drawable.ic_list_white_24dp);
        TextView text = (TextView) findViewById(R.id.entity_type);
        text.setText(EntityEnum.SEQUENCE.getName());

        TextView label = (TextView) findViewById(R.id.entity_type);
//        String testName = "";
//        for (SequenceEntry entry : (Iterable<SequenceEntry>) getEntity()
//                ) {
//            testName += entry.getValue();
//        }
        label.setText(EntityEnum.SEQUENCE.getName());
//        label.setText(s.getName());

    }

}

package cz.cvut.fel.memorice.view.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.entities.Dictionary;
import cz.cvut.fel.memorice.model.entities.EntityEnum;
import cz.cvut.fel.memorice.model.entities.entries.DictionaryEntry;

/**
 * Created by sheemon on 18.4.16.
 */
public class DictionaryDetailActivity extends DetailActivity{

    private Dictionary d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEntity((Dictionary) getIntent().getSerializableExtra("entity"));
        setContentView(R.layout.activity_detail);
        prepareToolbar();

        ImageView icon = (ImageView) findViewById(R.id.entity_icon);
        icon.setImageResource(R.drawable.ic_dictionary_white_24dp);
        TextView text = (TextView) findViewById(R.id.entity_type);
        text.setText(EntityEnum.DICTIONARY.getName());

        TextView label = (TextView) findViewById(R.id.entity_type);
//        String testName = "";
//        for (DictionaryEntry entry : (Iterable<DictionaryEntry>) getEntity()) {
//            testName += entry.getValue();
//            testName += entry.getDefinition();
//        }
        label.setText(EntityEnum.DICTIONARY.getName());
//        label.setText(s.getName());


    }
}

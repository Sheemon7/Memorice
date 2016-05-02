package cz.cvut.fel.memorice.model.database;

import android.app.Activity;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cz.cvut.fel.memorice.model.entities.Dictionary;
import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.model.entities.Group;
import cz.cvut.fel.memorice.model.entities.Sequence;
import cz.cvut.fel.memorice.model.entities.builders.Builder;
import cz.cvut.fel.memorice.model.entities.builders.DictionaryBuilder;
import cz.cvut.fel.memorice.model.entities.builders.SequenceBuilder;
import cz.cvut.fel.memorice.model.entities.builders.SetBuilder;
import cz.cvut.fel.memorice.model.entities.entries.DictionaryEntry;
import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.entities.entries.SequenceEntry;
import cz.cvut.fel.memorice.view.activities.MainActivity;

/**
 * Created by sheemon on 2.5.16.
 */
public class SQLiteHelperTest extends AndroidTestCase {

    private SQLiteHelper helper;
    private List<Entity> entities = new ArrayList<>();
    private Group g1, g2;
    private Sequence s1, s2;
    private Dictionary d1, d2;

    public void setUp() throws Exception {
        super.setUp();
        populateWithEntities();
        initHelper();
    }

    private void populateWithEntities() {
        g1 = prepareTestingSet("g1");
        g2 = prepareTestingSet("g2");
        entities.add(g1);
        entities.add(g2);
        s1 = prepareTestingList("s1");
        s2 = prepareTestingList("s2");
        entities.add(s1);
        entities.add(s2);
        d1 = prepareTestingDict("d1");
        d2 = prepareTestingDict("d2");
        entities.add(s1);
        entities.add(s2);
        g2.setFavourite(true);
        s2.setFavourite(true);
        d2.setFavourite(true);
    }

    private Group prepareTestingSet(String label) {
        Builder b = SetBuilder.getInstance();
        b.init(label);
        for (int i = 0; i < 100; i++) {
            b.add(new Entry(UUID.randomUUID().toString()));
        }
        return (Group) b.wrap();
    }

    private Sequence prepareTestingList(String label) {
        Builder b = SequenceBuilder.getInstance();
        b.init(label);
        for (int i = 0; i < 100; i++) {
            b.add(new SequenceEntry(UUID.randomUUID().toString(), i));
        }
        return (Sequence) b.wrap();
    }

    private Dictionary prepareTestingDict(String label) {
        Builder b = DictionaryBuilder.getInstance();
        b.init(label);
        for (int i = 0; i < 100; i++) {
            //for the sake of simplicity, dictionary definitions are kept that simple
            b.add(new DictionaryEntry(String.valueOf(i), UUID.randomUUID().toString()));
        }
        return (Dictionary) b.wrap();
    }

    private void initHelper() {
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        helper = new SQLiteHelper(context);
    }

    public void testGetEntity() throws Exception {
        for (Entity e :
                entities) {
            helper.addEntity(e);
        }
        for (Entity e :
                entities) {
            assertEquals(helper.getEntity(e.getName()), e);
        }
    }

    public void testAddSequenceEntry() throws Exception {

    }

    public void testAddDictEntry() throws Exception {

    }

    public void testAddSetEntry() throws Exception {

    }


    public void testGetAllEntitiesFiltered() throws Exception {

    }

    public void testGetAllSequenceEntries() throws Exception {

    }

    public void testGetAllDictEntries() throws Exception {

    }

    public void testGetAllSetEntries() throws Exception {

    }

    public void testGetAllEntities() throws Exception {

    }

    public void testGetAllFavouriteEntities() throws Exception {

    }

    public void testGetAllFavouriteFilteredEntities() throws Exception {

    }

    public void testDeleteEntity() throws Exception {

    }

    public void testDeleteSequenceEntry() throws Exception {

    }

    public void testDeleteSetEntry() throws Exception {

    }

    public void testDeleteDictionaryEntry() throws Exception {

    }

    public void testToggleFavorite() throws Exception {

    }

    public void testGetAllLabels() throws Exception {

    }

    public void testIsEntityFavourite() throws Exception {

    }

}
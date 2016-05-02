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

    private static final int NUMBER_OF_ENTRIES_WITHIN_ENTITIES = 1;
    private SQLiteHelper helper;
    private List<Entity> entities = new ArrayList<>();
    private List<Entity> entitiesFavourite = new ArrayList<>();
    private Group g1, g2;
    private Sequence s1, s2;
    private Dictionary d1, d2;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        populateWithEntities();
        initHelper();
        for (Entity e :
                entities) {
            helper.addEntity(e);
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        for (Entity e :
                entities) {
            helper.deleteEntity(e);
        }
    }



    private void populateWithEntities() {
        g1 = prepareTestingSet("g1");
        g2 = prepareTestingSet("g2");
        s1 = prepareTestingList("s1");
        s2 = prepareTestingList("s2");
        d1 = prepareTestingDict("d1");
        d2 = prepareTestingDict("d2");
        g2.setFavourite(true);
        s2.setFavourite(true);
        d2.setFavourite(true);
        entitiesFavourite.add(d2);
        entitiesFavourite.add(g2);
        entitiesFavourite.add(s2);
        entities.add(d1);
        entities.add(d2);
        entities.add(g1);
        entities.add(g2);
        entities.add(s1);
        entities.add(s2);
    }

    private Group prepareTestingSet(String label) {
        Builder b = SetBuilder.getInstance();
        b.init(label);
        for (int i = 0; i < NUMBER_OF_ENTRIES_WITHIN_ENTITIES; i++) {
            b.add(new Entry(UUID.randomUUID().toString()));
        }
        return (Group) b.wrap();
    }

    private Sequence prepareTestingList(String label) {
        Builder b = SequenceBuilder.getInstance();
        b.init(label);
        for (int i = 0; i < NUMBER_OF_ENTRIES_WITHIN_ENTITIES; i++) {
            b.add(new SequenceEntry(UUID.randomUUID().toString(), i + 1));
        }
        return (Sequence) b.wrap();
    }

    private Dictionary prepareTestingDict(String label) {
        Builder b = DictionaryBuilder.getInstance();
        b.init(label);
        for (int i = 0; i < NUMBER_OF_ENTRIES_WITHIN_ENTITIES; i++) {
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
            assertEquals(helper.getEntity(e.getName()), e);
        }
    }

    public void testGetAllEntitiesFiltered() throws Exception {
        String filter = "1";
        ArrayList<Entity> filtered = helper.getAllEntitiesFiltered(filter);
        ArrayList<Entity> target = new ArrayList<>();
        target.add(d1);
        target.add(g1);
        target.add(s1);
        assertEquals(filtered, target);
        filter = "s";
        filtered = helper.getAllEntitiesFiltered(filter);
        target = new ArrayList<>();
        target.add(s1);
        target.add(s2);
        assertEquals(filtered, target);
    }


    public void testGetAllEntities() throws Exception {
        assertEquals(entities, helper.getAllEntities());
    }

    public void testGetAllFavouriteEntities() throws Exception {
        assertEquals(entitiesFavourite, helper.getAllFavouriteEntities());
    }

    public void testGetAllFavouriteFilteredEntities() throws Exception {
        String filter = "1";
        ArrayList<Entity> filtered = helper.getAllFavouriteFilteredEntities(filter);
        ArrayList<Entity> target = new ArrayList<>();
        assertEquals(filtered, target);
        filter = "s";
        filtered = helper.getAllFavouriteFilteredEntities(filter);
        target = new ArrayList<>();
        target.add(s2);
        assertEquals(filtered, target);
    }

    public void testDeleteEntity() throws Exception {
        for (Entity e :
                entities) {
            assertEquals(e, helper.getEntity(e.getName()));
            helper.deleteEntity(e);
            assertEquals(null, helper.getEntity(e.getName()));
        }
    }

    public void testToggleFavorite() throws Exception {
        for (Entity e : entities) {
            helper.toggleFavorite(e);
            assertEquals(e.isFavourite(), helper.isEntityFavourite(e));
            helper.toggleFavorite(e);
            assertEquals(e.isFavourite(), helper.isEntityFavourite(e));
        }
    }

    public void testGetAllLabels() throws Exception {
        ArrayList<String> expected = new ArrayList<>();
        expected.add("d1");
        expected.add("d2");
        expected.add("g1");
        expected.add("g2");
        expected.add("s1");
        expected.add("s2");
        assertEquals(expected, helper.getAllLabels());
    }

    public void testIsEntityFavourite() throws Exception {
        for (Entity e :
                entities) {
            assertEquals(e.isFavourite(), helper.isEntityFavourite(e));
        }
    }

}
package cz.cvut.fel.memorice.model.database.helpers;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cvut.fel.memorice.model.entities.Dictionary;
import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.model.entities.EntityEnum;
import cz.cvut.fel.memorice.model.entities.Sequence;
import cz.cvut.fel.memorice.model.entities.Set;
import cz.cvut.fel.memorice.model.entities.builders.Builder;
import cz.cvut.fel.memorice.model.entities.entries.DictionaryEntry;
import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.entities.entries.SequenceEntry;
import cz.cvut.fel.memorice.model.util.WrongNameException;

/**
 * Testing {@link SQLiteHelper} class
 */
public class SQLiteHelperTest extends AndroidTestCase {

    private static final Logger LOG = Logger.getLogger(SQLiteHelperTest.class.getName());

    private static final int NUMBER_OF_ENTRIES_WITHIN_ENTITIES = 10;
    private SQLiteHelper helper;
    private List<Entity> entities = new ArrayList<>();
    private List<Entity> entitiesFavourite = new ArrayList<>();
    private Set g1, g2;
    private Sequence s1, s2;
    private Dictionary d1, d2;

    /**
     * Set Up method - adds test data to database
     * @throws Exception
     */
    @BeforeClass
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

    /**
     * Tear Down method - deletes test data from database
     * @throws Exception
     */
    @AfterClass
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        for (Entity e :
                entities) {
            helper.deleteEntity(e);
        }
    }

    @Test
    public void testGetEntity()  {
        for (Entity e :
                entities) {
            assertEquals(helper.getEntity(e.getLabel()), e);
        }
    }

    @Test
    public void testGetAllEntitiesFiltered()  {
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

    @Test
    public void testGetAllEntities()  {
        assertEquals(entities, helper.getAllEntities());
    }


    @Test
    public void testGetAllFavouriteEntities()  {
        assertEquals(entitiesFavourite, helper.getAllFavouriteEntities());
    }

    @Test
    public void testGetAllFavouriteFilteredEntities()  {
        String filter = "1";
        ArrayList<Entity> filtered = helper.getAllFavouriteEntitiesFiltered(filter);
        ArrayList<Entity> target = new ArrayList<>();
        assertEquals(filtered, target);
        filter = "s";
        filtered = helper.getAllFavouriteEntitiesFiltered(filter);
        target = new ArrayList<>();
        target.add(s2);
        assertEquals(filtered, target);
    }

    @Test
    public void testDeleteEntity()  {
        for (Entity e :
                entities) {
            assertEquals(e, helper.getEntity(e.getLabel()));
            helper.deleteEntity(e);
            assertEquals(null, helper.getEntity(e.getLabel()));
        }
    }

    @Test
    public void testToggleFavorite() throws WrongNameException {
        for (Entity e : entities) {
            helper.toggleFavourite(e);
            assertEquals(e.isFavourite(), helper.isEntityFavourite(e.getLabel()));
            helper.toggleFavourite(e);
            assertEquals(e.isFavourite(), helper.isEntityFavourite(e.getLabel()));
        }
    }

    @Test
    public void testGetAllLabels()  {
        ArrayList<String> expected = new ArrayList<>();
        expected.add("d1");
        expected.add("d2");
        expected.add("g1");
        expected.add("g2");
        expected.add("s1");
        expected.add("s2");
        assertEquals(expected, helper.getAllLabels());
    }

    @Test
    public void testIsEntityFavourite() {
        for (Entity e :
                entities) {
            try {
                assertEquals(e.isFavourite(), helper.isEntityFavourite(e.getLabel()));
            } catch (WrongNameException e1) {
                LOG.log(Level.INFO, "Wrong name!", e1);
                fail();
            }
        }
    }

    @Test
    public void testCountEntities() {
        assertEquals(helper.countEntities(), entities.size());
    }

    @Test
    public void testCountEntries() {
        assertEquals(helper.countEntries(), entities.size() * NUMBER_OF_ENTRIES_WITHIN_ENTITIES);

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

    private Set prepareTestingSet(String label) {
        Builder b = Builder.getCorrectBuilder(EntityEnum.SET);
        b.init(label);
        for (int i = 0; i < NUMBER_OF_ENTRIES_WITHIN_ENTITIES; i++) {
            b.add(new Entry(UUID.randomUUID().toString()));
        }
        return (Set) b.wrap();
    }

    private Sequence prepareTestingList(String label) {
        Builder b = Builder.getCorrectBuilder(EntityEnum.SEQUENCE);
        b.init(label);
        for (int i = 0; i < NUMBER_OF_ENTRIES_WITHIN_ENTITIES; i++) {
            b.add(new SequenceEntry(UUID.randomUUID().toString(), i + 1));
        }
        return (Sequence) b.wrap();
    }

    private Dictionary prepareTestingDict(String label) {
        Builder b = Builder.getCorrectBuilder(EntityEnum.DICTIONARY);
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

}
package cz.cvut.fel.memorice.view.fragments.input;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.memorice.model.database.dataaccess.ASyncSimpleAccessDatabase;
import cz.cvut.fel.memorice.model.database.helpers.SQLiteHelper;
import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.model.entities.EntityEnum;
import cz.cvut.fel.memorice.model.entities.builders.Builder;
import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.util.DefinitionAlreadyUsedException;
import cz.cvut.fel.memorice.model.util.EmptyDefinitionException;
import cz.cvut.fel.memorice.model.util.EmptyLabelException;
import cz.cvut.fel.memorice.model.util.NameAlreadyUsedException;

/**
 * Adapter responsible for correct display of the entries input items
 */
public abstract class EntryInputListAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    protected static final int INITIAL_CAPACITY = 3;
    protected List<ItemList> items;
    protected RecyclerView view;
    protected ImageView plusIcon;

    /**
     * Creates a new adapter binded to the given recycler view
     *
     * @param view recycler view
     */
    public EntryInputListAdapter(RecyclerView view) {
        this.view = view;
        view.setAdapter(this);
    }

    /**
     * Removes list item from the given position
     *
     * @param position position
     */
    public void remove(int position) {
        notifyItemRemoved(position);
        items.remove(position);
    }

    /**
     * Shows initial stub of the input view
     */
    public void show() {
        items = new ArrayList<>();
        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            addRow();
        }
    }

    /**
     * Sets plus icon to the items
     *
     * @param plusIcon plus icon
     */
    public void setPlusIcon(ImageView plusIcon) {
        this.plusIcon = plusIcon;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Adds another row to the view
     */
    public void addRow() {
        plusIcon.setEnabled(false);
        items.add(new ItemList());
        notifyItemInserted(items.size() - 1);
        view.scrollToPosition(items.size() - 1);
        plusIcon.setEnabled(true);
    }

    /**
     * Builds completely new entity from input
     *
     * @param label label of the entity
     * @param type  type of the entity
     * @throws NameAlreadyUsedException       if database already contains the same label
     * @throws EmptyLabelException            if label of the entity is empty
     * @throws DefinitionAlreadyUsedException if there exists two entries with the same values
     * @throws EmptyDefinitionException       if there exists two entries with the same values
     */
    public void buildNew(String label, EntityEnum type) throws NameAlreadyUsedException, EmptyLabelException, DefinitionAlreadyUsedException, EmptyDefinitionException {
        SQLiteHelper helper = new SQLiteHelper(view.getContext());
        if (label.length() == 0) {
            throw new EmptyLabelException();
        } else if (helper.getEntity(label) != null) {
            throw new NameAlreadyUsedException();
        }
        Builder builder = Builder.getCorrectBuilder(type);
        builder.init(label);

        try {
            for (Entry e : getInput()) {
                builder.add(e);
            }
        } catch (DefinitionAlreadyUsedException | EmptyDefinitionException e) {
            emphasizeErrors();
            builder.wrap();
            throw e;
        }

        addEntityToDatabase(builder.wrap());
    }

    /**
     * Adds new entity to the database
     *
     * @param entity entity
     */
    protected void addEntityToDatabase(Entity entity) {
        ASyncSimpleAccessDatabase access = new ASyncSimpleAccessDatabase(view.getContext());
        access.setEntity(entity);
        access.execute(ASyncSimpleAccessDatabase.ADD_ENTITY);
    }

    /* Childer must define those methods */
    public abstract T onCreateViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindViewHolder(final T holder, final int position);

    /**
     * Returns input currently held at the list view
     *
     * @return list of entries
     * @throws DefinitionAlreadyUsedException if there exists two entries with the same values
     * @throws EmptyDefinitionException       if there exists two entries with the same values
     */
    public abstract List<? extends Entry> getInput() throws DefinitionAlreadyUsedException, EmptyDefinitionException;

    /**
     * Emphasizes errors in the current recycle view
     */
    public abstract void emphasizeErrors();

    /**
     * Auxiliary class representing list items. After the end of user's input, these items
     * are transformed into entries.
     */
    protected static class ItemList {
        private String value = "";
        private String definition = "";
        private boolean correct = true;

        public boolean isCorrect() {
            return correct;
        }

        public void setCorrect(boolean correct) {
            this.correct = correct;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDefinition() {
            return definition;
        }

        public void setDefinition(String definition) {
            this.definition = definition;
        }
    }

}

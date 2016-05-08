package cz.cvut.fel.memorice.view.fragments.detail;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.memorice.model.database.dataaccess.ASyncSimpleAccessDatabase;
import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.model.entities.entries.Entry;

/**
 * This adapter is responsible for displaying entity's entries
 */
public abstract class EntityDetailListAdapter<T extends RecyclerView.ViewHolder, U extends Entry> extends RecyclerView.Adapter<T> {

    protected Entity entity;
    protected List<U> items = new ArrayList<>();
    protected RecyclerView view;
    protected boolean editable = false;

    /**
     * Creates new instance. Recycler view is required
     * @param view recycler view
     */
    public EntityDetailListAdapter(RecyclerView view) {
        this.view = view;
        view.setAdapter(this);
    }

    /**
     * Deletes entry from the recycler view and from the database
     * @param position position of the entry
     */
    public void deleteEntry(int position) {
        U entry = items.get(position);
        entity.deleteEntry(entry);
        items.remove(entry);
        ASyncSimpleAccessDatabase access = new ASyncSimpleAccessDatabase(view.getContext());
        access.setEntry(entry);
        access.setEntity(entity);
        access.execute(ASyncSimpleAccessDatabase.DELETE_ENTRY);
        notifyItemRemoved(position);
    }

    /**
     * Changes value of the entry at the given position
     * @param position position of the entry
     * @param value new value of the entry
     */
    public void editEntry(int position, String value) {
        U entry = items.get(position);
        ASyncSimpleAccessDatabase access = new ASyncSimpleAccessDatabase(view.getContext());
        access.setEntry(entry);
        access.setEntity(entity);
        access.setOldValue(entry.getValue());
        access.execute(ASyncSimpleAccessDatabase.EDIT_ENTRY);
        entry.setValue(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Sets different data to the recycler view
     * @param data new data
     */
    public void setData(List<U> data) {
        this.items = data;
        notifyDataSetChanged();
    }

    /**
     * Sets this instance to the editable state, every item in the list is now displayed
     * as editable
     * @param editable editable
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
        notifyDataSetChanged();
    }

    /**
     * Returns whether the instance is in editable state
     * @return true if the instance is in the editable state else false
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Sets entity, whose entries are displayed afterwards
     * @param entity entity
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    /* Children must define those methods */
    public abstract T onCreateViewHolder(ViewGroup parent, int viewType);
    public abstract void onBindViewHolder(final T holder, final int position);
}


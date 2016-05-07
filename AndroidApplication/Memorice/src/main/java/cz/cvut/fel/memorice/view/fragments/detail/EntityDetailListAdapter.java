package cz.cvut.fel.memorice.view.fragments.detail;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.memorice.model.database.dataaccess.ASyncSimpleAccessDatabase;
import cz.cvut.fel.memorice.model.database.helpers.SQLiteEntryTableHelper;
import cz.cvut.fel.memorice.model.database.helpers.SQLiteHelper;
import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.util.TermAlreadyUsedException;

/**
 * Created by sheemon on 30.4.16.
 */
public abstract class EntityDetailListAdapter<T extends RecyclerView.ViewHolder, U extends Entry> extends RecyclerView.Adapter<T> {

    protected Entity entity;
    protected List<U> items = new ArrayList<>();
    protected RecyclerView view;
    protected boolean editable = false;

    public EntityDetailListAdapter(RecyclerView view) {
        this.view = view;
        view.setAdapter(this);
    }

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

    public void editEntry(int position, String value) {
        U entry = items.get(position);
        ASyncSimpleAccessDatabase access = new ASyncSimpleAccessDatabase(view.getContext());
        access.setEntry(entry);
        access.setEntity(entity);
        access.setOldValue(entry.getValue());
        access.execute(ASyncSimpleAccessDatabase.EDIT_ENTRY);
        entry.setValue(value);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(List<U> data) {
        this.items = data;
        notifyDataSetChanged();
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        notifyDataSetChanged();
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public abstract T onCreateViewHolder(ViewGroup parent, int viewType);
    public abstract void onBindViewHolder(final T holder, final int position);
}


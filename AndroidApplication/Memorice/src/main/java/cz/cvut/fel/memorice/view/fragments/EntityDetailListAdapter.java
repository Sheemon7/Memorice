package cz.cvut.fel.memorice.view.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.util.TermAlreadyUsedException;

/**
 * Created by sheemon on 30.4.16.
 */
public abstract class EntityDetailListAdapter<T extends RecyclerView.ViewHolder, U extends Entry> extends RecyclerView.Adapter<T> {

    protected static final int INITIAL_CAPACITY = 3;
    protected List<U> items;
    protected RecyclerView view;

    public EntityDetailListAdapter(RecyclerView view) {
        this.view = view;
        view.setAdapter(this);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(List<U> data) {
        this.items = data;
    }

    public abstract T onCreateViewHolder(ViewGroup parent, int viewType);
    public abstract void onBindViewHolder(final T holder, final int position);
}


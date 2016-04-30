package cz.cvut.fel.memorice.view.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.util.TermAlreadyUsedException;

/**
 * Created by sheemon on 28.4.16.
 */
public abstract class EntryInputListAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    protected static final int INITIAL_CAPACITY = 3;
    protected List<ItemList> items;
    protected RecyclerView view;

    public EntryInputListAdapter(RecyclerView view) {
        this.view = view;
        view.setAdapter(this);
    }

    public void remove(int position) {
        notifyItemRemoved(position);
        items.remove(position);
    }

    public void show() {
        items = new ArrayList<>();
        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            addRow();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addRow() {
        items.add(new ItemList());
        notifyItemInserted(items.size() - 1);
    }

    public abstract T onCreateViewHolder(ViewGroup parent, int viewType);
    public abstract void onBindViewHolder(final T holder, final int position);
    public abstract ArrayList<? extends Entry> getInput() throws TermAlreadyUsedException;
    public abstract void emphasizeErrors();

    protected static class ItemList {
        private String value = "";
        private String definition = "";

        public boolean isCorrect() {
            return correct;
        }

        public void setCorrect(boolean correct) {
            this.correct = correct;
        }

        private boolean correct = true;

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

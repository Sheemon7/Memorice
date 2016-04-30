package cz.cvut.fel.memorice.view.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.entities.entries.DictionaryEntry;
import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.util.TermAlreadyUsedException;

/**
 * Created by sheemon on 30.4.16.
 */
public class DictionaryDetailListAdapter extends EntityDetailListAdapter<DictionaryDetailListAdapter.ViewHolder, DictionaryEntry> {

    public DictionaryDetailListAdapter(RecyclerView view) {
        super(view);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_dictionary_line, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtDefinition.setText(items.get(position).getDefinition());
        holder.txtValue.setText(items.get(position).getValue());
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtValue;
        private TextView txtDefinition;

        public ViewHolder(View v) {
            super(v);
            txtValue = (TextView) v.findViewById(R.id.entry_value);
            txtDefinition = (TextView) v.findViewById(R.id.entry_definition);
        }
    }


}

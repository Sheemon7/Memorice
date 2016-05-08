package cz.cvut.fel.memorice.view.fragments.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.entities.entries.DictionaryEntry;

/**
 * This adapter is responsible for displaying entries of dictionary entity
 */
public class DictionaryDetailListAdapter extends EntityDetailListAdapter<DictionaryDetailListAdapter.ViewHolder, DictionaryEntry> {

    /**
     * Constructs new instance
     *
     * @param view recycler view
     */
    public DictionaryDetailListAdapter(RecyclerView view) {
        super(view);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_dictionary_line, parent, false);
        return new ViewHolder(view);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        DictionaryEntry entry = items.get(position);
        holder.txtDefinition.setText(entry.getDefinition());
        holder.txtValue.setText(entry.getValue());
    }

    /**
     * View Holder class encapsulates recycler view items' components
     */
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtValue;
        private TextView txtDefinition;

        public ViewHolder(View v) {
            super(v);
            txtValue = (TextView) v.findViewById(R.id.entry_value);
            txtDefinition = (TextView) v.findViewById(R.id.entry_definition);
            txtValue.setEnabled(false);
            txtDefinition.setEnabled(false);
        }
    }


}

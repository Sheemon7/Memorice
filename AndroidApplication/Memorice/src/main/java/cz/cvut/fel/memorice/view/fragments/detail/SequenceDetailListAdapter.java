package cz.cvut.fel.memorice.view.fragments.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.entities.entries.Entry;

/**
 * This adapter is responsible for displaying entries of sequence entity
 */
public class SequenceDetailListAdapter extends EntityDetailListAdapter<SequenceDetailListAdapter.ViewHolder, Entry> {

    /**
     * Constructs new instance
     *
     * @param view recycler view
     */
    public SequenceDetailListAdapter(RecyclerView view) {
        super(view);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_sequence_line, parent, false);
        return new ViewHolder(view);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtValue.setText(items.get(position).getValue());
        String number = (position + 1) + ".";
        holder.txtNumber.setText(number);
    }

    /**
     * View Holder class encapsulates recycler view items' components
     */
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtValue;
        private TextView txtNumber;

        public ViewHolder(View v) {
            super(v);
            txtValue = (TextView) v.findViewById(R.id.entry_value);
            txtNumber = (TextView) v.findViewById(R.id.entry_num);
        }
    }
}

package cz.cvut.fel.memorice.view.fragments.input;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.entities.entries.SequenceEntry;
import cz.cvut.fel.memorice.model.util.EmptyDefinitionException;

/**
 * Adapter responsible for correct display of the sequence entries input items
 */
public class SequenceInputListAdapter extends EntryInputListAdapter<SequenceInputListAdapter.ViewHolder> {

    /**
     * Creates a new adapter binded to the given recycler view
     *
     * @param view recycler view
     */
    public SequenceInputListAdapter(RecyclerView view) {
        super(view);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public SequenceInputListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_sequence_line, parent, false);
        return new ViewHolder(v);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ItemList itemList = items.get(position);
        if (!itemList.isCorrect()) {
            holder.txtValue.setError("empty");
        } else {
            holder.txtValue.setError(null);
        }
        itemList.setCorrect(true);
        holder.txtValue.setText(itemList.getValue());
        holder.txtValue.addTextChangedListener(new CustomTextWatcher(holder));
        String number = (position + 1) + ".";
        holder.number.setText(number);
        holder.icon.setImageResource(R.drawable.ic_minus_24dp);
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getAdapterPosition() >= 0) {
                    items.get(holder.getAdapterPosition()).setCorrect(true);
                    remove(holder.getAdapterPosition());
                    notifyItemRangeChanged(position, items.size());
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SequenceEntry> getInput() throws EmptyDefinitionException {
        ArrayList<SequenceEntry> ret = new ArrayList<>(items.size());
        for (int i = 0; i < items.size(); i++) {
            String value = items.get(i).getValue();
            if ("".equals(value)) {
                throw new EmptyDefinitionException();
            }
            ret.add(new SequenceEntry(value, i + 1));
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void emphasizeErrors() {
        for (ItemList list :
                items) {
            String value = list.getValue();
            if ("".equals(value)) {
                list.setCorrect(false);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * View Holder class encapsulates recycler view items' components
     */
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private EditText txtValue;
        private TextView number;
        private ImageView icon;

        public ViewHolder(View v) {
            super(v);
            txtValue = (EditText) v.findViewById(R.id.entry_value);
            number = (TextView) v.findViewById(R.id.entry_num);
            icon = (ImageView) v.findViewById(R.id.icon_minus);
        }
    }

    /**
     * Custom watcher takes care of changing the list items value of value attribut once the user changes the input
     */
    private class CustomTextWatcher implements TextWatcher {

        private ViewHolder holder;

        public CustomTextWatcher(ViewHolder holder) {
            this.holder = holder;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                /* event passed */
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                items.get(holder.getAdapterPosition()).setValue(s.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
                /* event passed */
        }
    }
}

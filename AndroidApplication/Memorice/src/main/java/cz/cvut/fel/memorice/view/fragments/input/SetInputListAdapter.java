package cz.cvut.fel.memorice.view.fragments.input;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.util.DefinitionAlreadyUsedException;
import cz.cvut.fel.memorice.model.util.EmptyDefinitionException;

/**
 * Adapter responsible for correct display of the set entries input items
 */
public class SetInputListAdapter extends EntryInputListAdapter<SetInputListAdapter.ViewHolder> {

    /**
     * Creates a new adapter binded to the given recycler view
     *
     * @param view recycler view
     */
    public SetInputListAdapter(RecyclerView view) {
        super(view);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SetInputListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_set_line, parent, false);
        return new ViewHolder(v);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ItemList itemList = items.get(position);
        if (!itemList.isCorrect()) {
            if ("".equals(itemList.getValue())) {
                holder.txtValue.setError("empty");
            } else {
                holder.txtValue.setError("duplicate");
            }
        } else {
            holder.txtValue.setError(null);
        }
        itemList.setCorrect(true);
        holder.txtValue.setText(itemList.getValue());
        holder.txtValue.addTextChangedListener(new CustomTextWatcher(holder));
        holder.icon.setImageResource(R.drawable.ic_minus_24dp);
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getAdapterPosition() >= 0) {
                    items.get(holder.getAdapterPosition()).setCorrect(true);
                    remove(holder.getAdapterPosition());
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Entry> getInput() throws DefinitionAlreadyUsedException, EmptyDefinitionException {
        HashSet<String> valueCheck = new HashSet<>(items.size());
        ArrayList<Entry> ret = new ArrayList<>(items.size());
        for (ItemList item :
                items) {
            String value = item.getValue();
            if (valueCheck.contains(value)) {
                throw new DefinitionAlreadyUsedException();
            } else if ("".equals(value)) {
                throw new EmptyDefinitionException();
            } else {
                valueCheck.add(value);
                ret.add(new Entry(value));
            }
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void emphasizeErrors() {
        ArrayList<String> valueCheck = new ArrayList<>(items.size());
        for (ItemList list :
                items) {
            String value = list.getValue();
            if ("".equals(value)) {
                list.setCorrect(false);
            } else {
                if (valueCheck.contains(value)) {
                    list.setCorrect(false);
                    items.get(valueCheck.indexOf(value)).setCorrect(false);
                }
                valueCheck.add(value);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * View Holder class encapsulates recycler view items' components
     */
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private EditText txtValue;
        private ImageView icon;

        public ViewHolder(View v) {
            super(v);
            txtValue = (EditText) v.findViewById(R.id.entry_value);
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

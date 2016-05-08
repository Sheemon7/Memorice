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

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.entities.entries.DictionaryEntry;
import cz.cvut.fel.memorice.model.util.DefinitionAlreadyUsedException;
import cz.cvut.fel.memorice.model.util.EmptyDefinitionException;

/**
 * Adapter responsible for correct display of the dictionary entries input items
 */
public class DictionaryInputListAdapter extends EntryInputListAdapter<DictionaryInputListAdapter.ViewHolder> {

    /**
     * Creates a new adapter binded to the given recycler view
     *
     * @param view recycler view
     */
    public DictionaryInputListAdapter(RecyclerView view) {
        super(view);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DictionaryInputListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_dictionary_line, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ItemList itemList = items.get(position);
        if (!itemList.isCorrect()) {
            if (itemList.getValue().equals("")) {
                holder.txtValue.setError("empty");
            } else {
                holder.txtDefinition.setError("duplicate definition");
            }
        } else {
            holder.txtDefinition.setError(null);
            holder.txtValue.setError(null);
        }
        itemList.setCorrect(true);
        holder.txtValue.setText(itemList.getValue());
        holder.txtDefinition.setText(itemList.getDefinition());
        holder.txtValue.addTextChangedListener(new CustomTextWatcherValue(holder));
        holder.txtDefinition.addTextChangedListener(new CustomTextWatcherDefinition(holder));
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
    public ArrayList<DictionaryEntry> getInput() throws DefinitionAlreadyUsedException, EmptyDefinitionException {
        HashSet<String> valueCheck = new HashSet<>(items.size());
        ArrayList<DictionaryEntry> ret = new ArrayList<>(items.size());
        for (ItemList item :
                items) {
            String definition = item.getDefinition();
            String value = item.getValue();
            if (value.equals("")) {
                throw new EmptyDefinitionException();
            } else {
                if (valueCheck.contains(definition)) {
                    throw new DefinitionAlreadyUsedException();
                } else {
                    valueCheck.add(definition);
                    ret.add(new DictionaryEntry(definition, item.getValue()));
                }
            }
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void emphasizeErrors() {
        ArrayList<String> definitionCheck = new ArrayList<>(items.size());
        for (ItemList list :
                items) {
            String definition = list.getDefinition();
            String value = list.getValue();
            if (value.equals("")) {
                list.setCorrect(false);
            } else {
                if (definitionCheck.contains(definition)) {
                    list.setCorrect(false);
                    items.get(definitionCheck.indexOf(definition)).setCorrect(false);
                }
                definitionCheck.add(definition);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * View Holder class encapsulates recycler view items' components
     */
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private EditText txtDefinition;
        private EditText txtValue;
        private ImageView icon;

        public ViewHolder(View v) {
            super(v);
            txtDefinition = (EditText) v.findViewById(R.id.entry_definition);
            txtValue = (EditText) v.findViewById(R.id.entry_value);
            icon = (ImageView) v.findViewById(R.id.icon_minus);
        }
    }

    /**
     * Custom watcher takes care of changing the list items value of value attribut once the user changes the input
     */
    private class CustomTextWatcherValue implements TextWatcher {

        private ViewHolder holder;

        public CustomTextWatcherValue(ViewHolder holder) {
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

    /**
     * Custom watcher takes care of changing the list items value of value attribut once the user changes the input
     */
    private class CustomTextWatcherDefinition implements TextWatcher {

        private ViewHolder holder;

        public CustomTextWatcherDefinition(ViewHolder holder) {
            this.holder = holder;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                /* event passed */
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                items.get(holder.getAdapterPosition()).setDefinition(s.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
                /* event passed */
        }
    }
}


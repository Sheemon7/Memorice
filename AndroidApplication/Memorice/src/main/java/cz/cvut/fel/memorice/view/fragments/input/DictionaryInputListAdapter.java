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
import cz.cvut.fel.memorice.model.util.EmptyTermException;
import cz.cvut.fel.memorice.model.util.TermAlreadyUsedException;

/**
 * Created by sheemon on 28.4.16.
 */
public class DictionaryInputListAdapter extends EntryInputListAdapter<DictionaryInputListAdapter.ViewHolder> {

    public DictionaryInputListAdapter(RecyclerView view) {
        super(view);
    }

    @Override
    public DictionaryInputListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_dictionary_line, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

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
        holder.txtValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                    items.get(holder.getAdapterPosition()).setValue(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        holder.txtDefinition.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                    items.get(holder.getAdapterPosition()).setDefinition(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
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

    @Override
    public ArrayList<DictionaryEntry> getInput() throws TermAlreadyUsedException, EmptyTermException {
        HashSet<String> valueCheck = new HashSet<>(items.size());
        ArrayList<DictionaryEntry> ret = new ArrayList<>(items.size());
        for (ItemList item :
                items) {
            String definition = item.getDefinition();
            String value = item.getValue();
            if (value.equals("")) {
                throw new EmptyTermException();
            } else {
                if (valueCheck.contains(definition)) {
                    throw new TermAlreadyUsedException();
                } else {
                    valueCheck.add(definition);
                    ret.add(new DictionaryEntry(definition, item.getValue()));
                }
            }
        }
        return ret;
    }

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
}


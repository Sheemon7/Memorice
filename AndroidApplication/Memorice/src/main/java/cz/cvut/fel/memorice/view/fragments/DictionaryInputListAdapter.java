package cz.cvut.fel.memorice.view.fragments;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.entities.entries.DictionaryEntry;
import cz.cvut.fel.memorice.model.entities.entries.Entry;

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
        if (!items.get(position).isCorrect()) {
            holder.txtValue.setError("");
        } else {
            holder.txtValue.setError(null);
        }
        holder.txtValue.setText(items.get(position).getValue());
        holder.txtDefinition.setText(items.get(position).getDefinition());
        holder.txtDefinition.setHint("definition");
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
        holder.txtValue.setHint("value");
        holder.txtDefinition.requestFocus();
        holder.icon.setImageResource(R.drawable.ic_minus_24dp);
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public ArrayList<DictionaryEntry> getInput() {
        ArrayList<DictionaryEntry> ret = new ArrayList<>(items.size());
        return ret;
    }

    @Override
    public void emphasizeErrors() {

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
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


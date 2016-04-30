package cz.cvut.fel.memorice.view.fragments;

import android.app.LauncherActivity;
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
import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.util.TermAlreadyUsedException;

/**
 * Created by sheemon on 28.4.16.
 */
public class SetInputListAdapter extends EntryInputListAdapter<SetInputListAdapter.ViewHolder> {

    public SetInputListAdapter(RecyclerView view) {
        super(view);
    }

    @Override
    public SetInputListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_set_line, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (!items.get(position).isCorrect()) {
            holder.txtValue.setError("duplicate value");
        } else {
            holder.txtValue.setError(null);
        }
        holder.txtValue.setText(items.get(position).getValue());
        holder.txtValue.setHint("value");
        holder.txtValue.requestFocus();
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
        holder.icon.setImageResource(R.drawable.ic_minus_24dp);
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.get(holder.getAdapterPosition()).setCorrect(true);
                remove(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public ArrayList<Entry> getInput() throws TermAlreadyUsedException {
        HashSet<String> valueCheck = new HashSet<>(items.size());
        ArrayList<Entry> ret = new ArrayList<>(items.size());
        for (ItemList item :
                items) {
            String value = item.getValue();
            if (valueCheck.contains(value)) {
                throw new TermAlreadyUsedException();
            } else {
                valueCheck.add(value);
                ret.add(new Entry(value));
            }
        }
        return ret;
    }

    @Override
    public void emphasizeErrors() {
        ArrayList<String> valueCheck = new ArrayList<>(items.size());
        for (ItemList list :
               items) {
            if (valueCheck.contains(list.getValue())) {
                list.setCorrect(false);
                items.get(valueCheck.indexOf(list.getValue())).setCorrect(false);
            }
            valueCheck.add(list.getValue());
        }
        notifyDataSetChanged();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private EditText txtValue;
        private ImageView icon;

        public ViewHolder(View v) {
            super(v);
            txtValue = (EditText) v.findViewById(R.id.entry_value);
            icon = (ImageView) v.findViewById(R.id.icon_minus);
        }
    }
}

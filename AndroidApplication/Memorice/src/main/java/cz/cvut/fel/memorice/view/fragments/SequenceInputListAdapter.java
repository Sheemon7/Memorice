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
import android.widget.TextView;

import java.util.ArrayList;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.entities.entries.Entry;
import cz.cvut.fel.memorice.model.entities.entries.SequenceEntry;
import cz.cvut.fel.memorice.model.util.EmptyTermException;

/**
 * Created by sheemon on 28.4.16.
 */
public class SequenceInputListAdapter extends EntryInputListAdapter<SequenceInputListAdapter.ViewHolder> {

    public SequenceInputListAdapter(RecyclerView view) {
        super(view);
    }

    @Override
    public SequenceInputListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_sequence_line, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (!items.get(position).isCorrect()) {
            holder.txtValue.setError("empty");
        } else {
            holder.txtValue.setError(null);
        }
        holder.txtValue.setText(items.get(position).getValue());
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
        holder.number.setText(String.valueOf(position + 1) + ".");
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

    @Override
    public ArrayList<SequenceEntry> getInput() throws EmptyTermException {
        ArrayList<SequenceEntry> ret = new ArrayList<>(items.size());
        for (int i = 0; i < items.size(); i++) {
            String value = items.get(i).getValue();
            if (value.equals("")) {
                throw new EmptyTermException();
            }
            ret.add(new SequenceEntry(value, i + 1));
        }
        return ret;
    }

    @Override
    public void emphasizeErrors() {
        for (ItemList list :
                items) {
            String value = list.getValue();
            if (value.equals("")) {
                list.setCorrect(false);
            }
        }
        notifyDataSetChanged();
    }

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
}

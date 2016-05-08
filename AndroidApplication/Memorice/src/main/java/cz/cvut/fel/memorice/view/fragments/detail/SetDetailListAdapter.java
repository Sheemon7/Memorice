package cz.cvut.fel.memorice.view.fragments.detail;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.List;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.entities.entries.Entry;

/**
 * Created by sheemon on 30.4.16.
 */
public class SetDetailListAdapter extends EntityDetailListAdapter<SetDetailListAdapter.ViewHolder, Entry> {

    public SetDetailListAdapter(RecyclerView view) {
        super(view);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_set_line, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.txtValue.setText(items.get(position).getValue());
        if (isEditable()) {
            prepareItemToEdit(holder);
        } else {
            prepareItem(holder);
        }
    }

    private void prepareItem(ViewHolder holder) {
        holder.txtValue.setError(null);
        holder.txtValue.setEnabled(false);
        holder.iconDelete.setVisibility(View.GONE);
    }

    private void prepareItemToEdit(final ViewHolder holder) {
        holder.iconDelete.setVisibility(View.VISIBLE);
        holder.iconDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                deleteEntry(adapterPosition);
            }
        });
        holder.txtValue.setEnabled(true);
        holder.txtValue.addTextChangedListener(new CustomSetEntryTextChangeListener(holder, items));
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private EditText txtValue;
        private ImageView iconDelete;

        public ViewHolder(View v) {
            super(v);
            txtValue = (EditText) v.findViewById(R.id.entry_value);
            txtValue.setEnabled(false);
            iconDelete = (ImageView) v.findViewById(R.id.delete_icon);
        }
    }

    private class CustomSetEntryTextChangeListener implements TextWatcher {

        private ViewHolder holder;
        private List<Entry> items;


        public CustomSetEntryTextChangeListener(ViewHolder holder, List<Entry> items) {
            this.holder = holder;
            this.items = items;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            Entry entry = items.get(holder.getAdapterPosition());
            String previous = entry.getValue();
            if (s.toString().equals("")) {
                holder.txtValue.setError("Cannot be empty!");
            } else if (items.contains(new Entry(s.toString())) && !s.toString().equals(previous)) {
                holder.txtValue.setError("Cannot contain duplicates!");
            } else {
                editEntry(holder.getAdapterPosition(), s.toString());
            }
        }
    }
}

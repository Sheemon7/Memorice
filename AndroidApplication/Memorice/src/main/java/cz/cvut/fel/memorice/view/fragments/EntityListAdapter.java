package cz.cvut.fel.memorice.view.fragments;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.database.SQLiteHelper;
import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.model.util.WrongNameException;

/**
 * Created by sheemon on 21.4.16.
 */
public class EntityListAdapter extends RecyclerView.Adapter<EntityListAdapter.ViewHolder> {

    private ArrayList<Entity> mDataset;
    private RecyclerView view;
    private String filter = "";

    public EntityListAdapter(RecyclerView view) {
        this.view = view;
    }

    public void showAll(Context context) {
        mDataset = new SQLiteHelper(context).getAllEntities();
        view.setAdapter(this);
    }

    public void showFavorites() {
        //TODO
    }

    public void remove(Entity item, Context context) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        new SQLiteHelper(context).deleteEntity(item);
        notifyItemRemoved(position);
    }

    public void toggleFavorite(Entity item, Context context) {
        try {
            new SQLiteHelper(context).toggleFavorite(item);
            notifyItemChanged(mDataset.indexOf(item));
        } catch (WrongNameException e) {
            e.printStackTrace();
        }
    }

    public void filter(String filter, Context context) {
        mDataset = new SQLiteHelper(context).getAllEntitiesFiltered(filter);
        this.filter = filter;
        notifyDataSetChanged();
    }


    // Create new views (invoked by the layout manager)
    @Override
    public EntityListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // init a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entity_line, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Entity e = mDataset.get(position);
        holder.txtHeader.setText(e.getName(), TextView.BufferType.SPANNABLE);

        if (filter != null) {
            int i = e.getName().toLowerCase().indexOf(filter.toLowerCase());
            if (i != - 1) {
                Spannable str = (Spannable) holder.txtHeader.getText();
                int color;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    color = holder.txtHeader.getHighlightColor();
                } else {
                    color = Color.BLACK;
                }
                str.setSpan(new ForegroundColorSpan(color), i, i + filter.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

    switch (e.getType()) {
            case GROUP:
                holder.imageType.setImageResource(R.drawable.ic_set_inverted_24dp);
                break;
            case SEQUENCE:
                holder.imageType.setImageResource(R.drawable.ic_list_inverted_24dp);
                break;
            case DICTIONARY:
                holder.imageType.setImageResource(R.drawable.ic_dictionary_inverted_24dp);
                break;
        }

        if (e.isFavourite()) {
            holder.imageFav.setImageResource(R.drawable.ic_favorite_true_24dp);
        } else {
            holder.imageFav.setImageResource(R.drawable.ic_favorite_false_24dp);
        }
        holder.imageFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavorite(e, view.getContext());
            }
        });

        holder.imageDel.setImageResource(R.drawable.ic_delete_inverted_24dp);
        holder.imageDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(e, view.getContext());
            }
        });
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtHeader;
        private ImageView imageType;
        private ImageView imageFav;
        private ImageView imageDel;

        public ViewHolder(View v) {
            super(v);
            txtHeader = (TextView) v.findViewById(R.id.labelLine);
            imageType = (ImageView) v.findViewById(R.id.icon_type);
            imageFav= (ImageView) v.findViewById(R.id.icon_favorite);
            imageDel= (ImageView) v.findViewById(R.id.icon_delete);
        }
    }
}


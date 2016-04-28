package cz.cvut.fel.memorice.view.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
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
import cz.cvut.fel.memorice.view.activities.DictionaryDetailActivity;
import cz.cvut.fel.memorice.view.activities.SequenceDetailActivity;
import cz.cvut.fel.memorice.view.activities.SetDetailActivity;

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

    @Override
    public EntityListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entity_line, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Entity e = mDataset.get(position);
        holder.txtHeader.setText(e.getName(), TextView.BufferType.SPANNABLE);
        holder.txtType.setText(e.getType().getName(), TextView.BufferType.SPANNABLE);

        if (filter != null) {
            int i = e.getName().toLowerCase().indexOf(filter.toLowerCase());
            if (i != -1) {
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
        View.OnClickListener listener = null;
        switch (e.getType()) {
            case GROUP:
                holder.imageType.setImageResource(R.drawable.ic_set_inverted_24dp);
                listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(view.getContext(), SetDetailActivity.class);
                        myIntent.putExtra("entity", e);
                        view.getContext().startActivity(myIntent);
                    }
                };
                break;
            case SEQUENCE:
                holder.imageType.setImageResource(R.drawable.ic_list_inverted_24dp);
                listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(view.getContext(), SequenceDetailActivity.class);
                        myIntent.putExtra("entity", e);
                        view.getContext().startActivity(myIntent);
                    }
                };
                break;
            case DICTIONARY:
                holder.imageType.setImageResource(R.drawable.ic_dictionary_inverted_24dp);
                listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(view.getContext(), DictionaryDetailActivity.class);
                        myIntent.putExtra("entity", e);
                        view.getContext().startActivity(myIntent);
                    }
                };
                break;
        }
        holder.txtHeader.setOnClickListener(listener);
        holder.txtType.setOnClickListener(listener);
        holder.imageType.setOnClickListener(listener);

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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                alertDialogBuilder.setTitle("Delete " + e.getName());
                alertDialogBuilder.setMessage("Are you sure?");
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        remove(e, view.getContext());
                        dialog.cancel();
                    }
                });
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtHeader;
        private TextView txtType;
        private ImageView imageType;
        private ImageView imageFav;
        private ImageView imageDel;

        public ViewHolder(View v) {
            super(v);
            txtHeader = (TextView) v.findViewById(R.id.label_line);
            txtType = (TextView) v.findViewById(R.id.typeLine);
            imageType = (ImageView) v.findViewById(R.id.icon_type);
            imageFav = (ImageView) v.findViewById(R.id.icon_favorite);
            imageDel = (ImageView) v.findViewById(R.id.icon_delete);
        }
    }
}


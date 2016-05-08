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
import java.util.List;

import cz.cvut.fel.memorice.R;
import cz.cvut.fel.memorice.model.database.dataaccess.ASyncListReadDatabase;
import cz.cvut.fel.memorice.model.database.dataaccess.ASyncSimpleAccessDatabase;
import cz.cvut.fel.memorice.model.entities.Entity;
import cz.cvut.fel.memorice.view.activities.detail.DictionaryDetailActivity;
import cz.cvut.fel.memorice.view.activities.detail.SequenceDetailActivity;
import cz.cvut.fel.memorice.view.activities.detail.SetDetailActivity;

/**
 * This class is responsible for displaying entity list and handling aactions connected with them
 */
public class EntityListAdapter extends RecyclerView.Adapter<EntityListAdapter.ViewHolder> {

    private List<Entity> mDataset;
    private RecyclerView view;
    private String filter = "";

    /**
     * Creates an instance of adapter
     *
     * @param view recycler view
     */
    public EntityListAdapter(RecyclerView view) {
        this.view = view;
        mDataset = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entity_line, parent, false);
        return new ViewHolder(v);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Entity entity = mDataset.get(position);
        holder.txtHeader.setText(entity.getLabel(), TextView.BufferType.SPANNABLE);
        holder.txtType.setText(entity.getType().getName(), TextView.BufferType.SPANNABLE);
        emphasizeFilteredText(holder, entity);
        switch (entity.getType()) {
            case SEQUENCE:
                holder.imageType.setImageResource(R.drawable.ic_list_inverted_24dp);
                break;
            case DICTIONARY:
                holder.imageType.setImageResource(R.drawable.ic_dictionary_inverted_24dp);
                break;
            default:
                holder.imageType.setImageResource(R.drawable.ic_set_inverted_24dp);
                break;
        }
        View.OnClickListener listener = getCorrectListener(entity);
        holder.txtHeader.setOnClickListener(listener);
        holder.txtType.setOnClickListener(listener);
        holder.imageType.setOnClickListener(listener);
        prepareFavIcon(holder, entity);
        prepareDeleteIcon(holder, entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    /**
     * Shows all entities from the database in the recycler view
     *
     * @param context application context
     */
    public void showAll(Context context) {
        ASyncListReadDatabase access = new ASyncListReadDatabase(context);
        access.setAdapter(this);
        access.execute(ASyncListReadDatabase.ALL_ENTITIES);
        view.setAdapter(this);
    }

    /**
     * Shows only entities filtered by a string in the recycler view
     *
     * @param context application context
     * @param filter  filter string
     */
    public void showFiltered(String filter, Context context) {
        this.filter = filter;
        ASyncListReadDatabase access = new ASyncListReadDatabase(context);
        access.setAdapter(this);
        access.setFilter(filter);
        access.execute(ASyncListReadDatabase.FILTERED_ENTITIES);
    }

    /**
     * Shows only favourite entities in the recycler view
     *
     * @param context application context
     */
    public void showFavorites(Context context) {
        ASyncListReadDatabase access = new ASyncListReadDatabase(context);
        access.setAdapter(this);
        if ("".equals(filter)) {
            access.execute(ASyncListReadDatabase.FAVOURITE_ENTITIES);
        } else {
            access.setFilter(filter);
            access.execute(ASyncListReadDatabase.FAVOURITE_FILTER_ENTITIES);
        }
    }

    /**
     * Sets different entities list to display, notifies recycler view to change its layout
     *
     * @param entities list of entities
     */
    public void setData(List<Entity> entities) {
        mDataset = entities;
        notifyDataSetChanged();
    }

    /**
     * Completely removes entity from the recycler view and the database
     *
     * @param entity  entity
     * @param context application context
     */
    public void remove(Entity entity, Context context) {
        int position = mDataset.indexOf(entity);
        mDataset.remove(position);
        ASyncSimpleAccessDatabase access = new ASyncSimpleAccessDatabase(context);
        access.setEntity(entity);
        access.execute(ASyncSimpleAccessDatabase.DELETE_ENTITY);
        notifyItemRemoved(position);
    }

    /**
     * Change favourite entity to not favourite and vice versa. Changes also occur in the database
     *
     * @param entity  entity
     * @param context application context
     */
    public void toggleFavorite(Entity entity, Context context) {
        ASyncSimpleAccessDatabase access = new ASyncSimpleAccessDatabase(context);
        access.setEntity(entity);
        access.execute(ASyncSimpleAccessDatabase.TOGGLE_FAVOURITE);
        notifyItemChanged(mDataset.indexOf(entity));
    }

    /**
     * View holder serves as an encapsulation for the whole recyler view line
     */
    protected static class ViewHolder extends RecyclerView.ViewHolder {

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

    /**
     * Returns correct on click listener, depending of entity type
     *
     * @param entity entity
     * @return correct listener
     */
    private View.OnClickListener getCorrectListener(final Entity entity) {
        Intent myIntent;
        switch (entity.getType()) {
            case SEQUENCE:
                myIntent = new Intent(view.getContext(), SequenceDetailActivity.class);
                break;
            case DICTIONARY:
                myIntent = new Intent(view.getContext(), DictionaryDetailActivity.class);
                break;
            default:
                myIntent = new Intent(view.getContext(), SetDetailActivity.class);
                break;
        }
        myIntent.putExtra(view.getContext().getString(R.string.entity_label_resource), entity.getLabel());
        final Intent finalMyIntent = myIntent;
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.getContext().startActivity(finalMyIntent);
            }
        };
    }

    /**
     * Emphasizes entities, which complies with the filtering text
     *
     * @param holder holder
     * @param entity entity
     */
    private void emphasizeFilteredText(ViewHolder holder, Entity entity) {
        int i = entity.getLabel().toLowerCase().indexOf(filter.toLowerCase());
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

    /**
     * Shows confirmation dialog upon the deletion of an entity
     *
     * @param entity entity to be deleted
     */
    private void showConfirmationDialog(final Entity entity) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext(), R.style.AlertDialogTheme);
        alertDialogBuilder.setTitle("Delete " + entity.getLabel());
        alertDialogBuilder.setMessage("Are you sure?");
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                remove(entity, view.getContext());
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

    /**
     * Prepares favourite icon and all its listeners
     *
     * @param holder holder
     * @param entity entity
     */
    private void prepareFavIcon(ViewHolder holder, final Entity entity) {
        if (entity.isFavourite()) {
            holder.imageFav.setImageResource(R.drawable.ic_favorite_true_24dp);
        } else {
            holder.imageFav.setImageResource(R.drawable.ic_favorite_false_24dp);
        }
        holder.imageFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavorite(entity, view.getContext());
            }
        });
    }

    /**
     * Prepares delete icon and all its listeners
     *
     * @param holder holder
     * @param entity entity
     */
    private void prepareDeleteIcon(ViewHolder holder, final Entity entity) {
        holder.imageDel.setImageResource(R.drawable.ic_delete_inverted_24dp);
        holder.imageDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog(entity);
            }
        });
    }
}


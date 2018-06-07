package ru.ratanov.kinoman.model.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.ratanov.kinoman.R;
import ru.ratanov.kinoman.model.content.TopItem;
import ru.ratanov.kinoman.model.parsers.FilmParser;
import ru.ratanov.kinoman.model.utils.QueryPreferences;
import ru.ratanov.kinoman.model.views.TopItemView;
import ru.ratanov.kinoman.ui.activity.detail.DetailActivity;

/**
 * Created by ACER on 27.11.2016.
 */

public class TopAdapter extends RecyclerView.Adapter<TopAdapter.TopViewHolder> {

    public static final String TAG = "TopAdapter";

    private Context mContext;
    private List<TopItem> mItems;

    public TopAdapter(Context context, List<TopItem> items) {
        mContext = context;
        mItems = items;
    }

    @Override
    public TopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = new TopItemView(mContext);
        return new TopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TopViewHolder holder, int position) {
        TopItem resultsItem = mItems.get(position);
        holder.bindItem(resultsItem);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class TopViewHolder extends RecyclerView.ViewHolder {

        TopViewHolder(View view) {
            super(view);
        }

        void bindItem(final TopItem resultsItem) {

            Picasso.with(mContext)
                    .load(resultsItem.getPictureUrl())
                    .into((ImageView) itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = DetailActivity.newIntent(mContext, resultsItem.getLink());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}

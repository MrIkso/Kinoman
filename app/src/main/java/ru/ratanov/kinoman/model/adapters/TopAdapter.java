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
import ru.ratanov.kinoman.ui.activity.detail.DetailActivity;

/**
 * Created by ACER on 27.11.2016.
 */

public class TopAdapter extends RecyclerView.Adapter<TopAdapter.TopViewHolder> {

    public static final String TAG = "TopAdapter";

    private int posterWidth;
    private int posterHeight;

    private Context mContext;
    private List<TopItem> mItems;

    public TopAdapter(Context context, List<TopItem> items) {
        mContext = context;
        mItems = items;
    }

    @Override
    public TopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.main_tile_item, parent, false);

        int spans = Integer.parseInt(QueryPreferences.getStoredQuery(mContext, "number_of_spans", "2"));
        posterWidth = parent.getMeasuredWidth() / spans;
        posterHeight = (int) (posterWidth * 1.5);

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
        private ImageView mImageView;

        TopViewHolder(View view) {
            super(view);
            mImageView = (ImageView) itemView.findViewById(R.id.tile_picture);
        }

        void bindItem(final TopItem resultsItem) {

            Picasso.with(mContext)
                    .load(resultsItem.getPictureUrl())
                    .resize(posterWidth - 2, posterHeight - 2)
                    .into(mImageView);

            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = DetailActivity.newIntent(mContext, resultsItem.getLink());
                    mContext.startActivity(intent);
                    /*FilmParser filmParser = new FilmParser();
                    if (filmParser.isFilmBlocked(resultsItem.getLink())) {
                        Snackbar.make(view, R.string.blocked, Snackbar.LENGTH_SHORT).show();
                    } else {
                        Intent intent = DetailActivity.newIntent(mContext, resultsItem.getLink());
                        mContext.startActivity(intent);
                    }*/
                }
            });
        }
    }
}

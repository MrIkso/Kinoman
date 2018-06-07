package ru.ratanov.kinoman.model.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.ratanov.kinoman.R;
import ru.ratanov.kinoman.model.content.ISearchItem;
import ru.ratanov.kinoman.ui.activity.detail.DetailActivity;

public class ISearchAdapter extends RecyclerView.Adapter<ISearchAdapter.ISearchViewHolder> {

    private Context mContext;
    private List<ISearchItem> mItems;

    public ISearchAdapter(Context mContext, List<ISearchItem> mItems) {
        this.mContext = mContext;
        this.mItems = mItems;
    }

    @Override
    public ISearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.i_search_result_item, parent, false);
        return new ISearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ISearchViewHolder holder, int position) {
        holder.bindItem(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ISearchViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageView poster;
        private TextView year;
        private TextView genre;
        private TextView rating;
        private TextView country;

        public ISearchViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.search_title);
            poster = (ImageView) itemView.findViewById(R.id.search_poster);
            year = (TextView) itemView.findViewById(R.id.search_year);
            genre = (TextView) itemView.findViewById(R.id.search_genre);
            rating = (TextView) itemView.findViewById(R.id.search_rating);
            country = (TextView) itemView.findViewById(R.id.search_country);
        }

        void bindItem(final ISearchItem item) {
            title.setText(item.getTitle());
            year.setText(item.getYear());
            genre.setText(item.getGenre());
            rating.setText(item.getRating());
            country.setText(item.getCountry());

            Picasso.with(mContext)
                    .load(item.getPosterUrl())
                    .into(poster);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = DetailActivity.newIntent(mContext, item.getLink());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}

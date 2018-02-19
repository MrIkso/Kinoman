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
import ru.ratanov.kinoman.model.content.SearchItem;
import ru.ratanov.kinoman.ui.activity.detail.DetailActivity;

public class MySearchAdapter extends RecyclerView.Adapter<MySearchAdapter.MySearchViewHolder> {

    private Context mContext;
    private List<SearchItem> mItems;

    public MySearchAdapter(Context context, List<SearchItem> items) {
        mContext = context;
        mItems = items;
    }

    @Override
    public MySearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_result_item, parent, false);
        return new MySearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MySearchViewHolder holder, int position) {
        holder.bindItem(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class MySearchViewHolder extends RecyclerView.ViewHolder {

        private ImageView poster;
        private TextView title;
        private TextView year;
        private TextView genre;
        private TextView rating;
        private TextView country;

        public MySearchViewHolder(View view) {
            super(view);
            poster = (ImageView) view.findViewById(R.id.search_poster);
            title = (TextView) view.findViewById(R.id.search_title);
            year = (TextView) view.findViewById(R.id.search_year);
            genre = (TextView) view.findViewById(R.id.search_genre);
            rating = (TextView) view.findViewById(R.id.search_rating);
            country = (TextView) view.findViewById(R.id.search_country);
        }

        public void bindItem(final SearchItem item) {
            Picasso.with(mContext).load(item.getPosterUrl()).into(poster);
            title.setText(item.getTitle());
            year.setText(item.getYear());
            genre.setText(item.getGenre());
            rating.setText(item.getRating());
            country.setText(item.getCountry());

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

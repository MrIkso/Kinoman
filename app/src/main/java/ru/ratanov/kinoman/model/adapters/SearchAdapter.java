package ru.ratanov.kinoman.model.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.ratanov.kinoman.R;
import ru.ratanov.kinoman.model.content.SearchItem;
import ru.ratanov.kinoman.ui.activity.detail.DetailActivity;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private Context mContext;
    private List<SearchItem> mItems;

    public SearchAdapter(Context mContext, List<SearchItem> mItems) {
        this.mContext = mContext;
        this.mItems = mItems;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_result_item, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        holder.bindItem(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView date;
        private TextView size;
        private TextView seeds;

        public SearchViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.search_title);
            date = (TextView) itemView.findViewById(R.id.search_date);
            size = (TextView) itemView.findViewById(R.id.search_size);
            seeds = (TextView) itemView.findViewById(R.id.search_seeds);
        }

        void bindItem(final SearchItem item) {
            title.setText(item.getTitle());
            date.setText(item.getDate());
            size.setText(item.getSize());
            seeds.setText(item.getSeeds());

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

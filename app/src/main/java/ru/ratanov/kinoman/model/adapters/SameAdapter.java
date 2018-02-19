package ru.ratanov.kinoman.model.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import ru.ratanov.kinoman.R;
import ru.ratanov.kinoman.model.content.SameItem;
import ru.ratanov.kinoman.presentation.presenter.detail.SamePresenter;
import ru.ratanov.kinoman.ui.activity.detail.DetailActivity;

public class SameAdapter extends RecyclerView.Adapter<SameAdapter.SameViewHolder> {

    private Context mContext;
    private Activity mActivity;
    private SamePresenter mSamePresenter;
    private List<SameItem> mItems;

    public SameAdapter(Context context, SamePresenter samePresenter, List<SameItem> items) {
        mContext = context;
        mActivity = (Activity) context;
        mSamePresenter = samePresenter;
        mItems = items;
    }

    @Override
    public SameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.detail_same_item, parent, false);
        return new SameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SameViewHolder holder, int position) {
        holder.bindItem(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class SameViewHolder extends RecyclerView.ViewHolder {

        private TextView quality;
        private TextView translate;
        private TextView seeds;
        private TextView series;
        private TextView date;
        private Button downloadButton;

        SameViewHolder(View view) {
            super(view);
            quality = (TextView) view.findViewById(R.id.same_quality);
            translate = (TextView) view.findViewById(R.id.same_translate);
            seeds = (TextView) view.findViewById(R.id.same_seeds);
            series = (TextView) view.findViewById(R.id.same_series);
            date = (TextView) view.findViewById(R.id.same_date);
            downloadButton = (Button) view.findViewById(R.id.same_download_button);
        }

        void bindItem(final SameItem sameItem) {
            quality.setText(sameItem.getQuality());
            translate.setText(sameItem.getTranslate());
            seeds.setText(sameItem.getSeeds());
            series.setText(sameItem.getSeries());
            date.setText(sameItem.getDate());
            downloadButton.setText(sameItem.getSize());
            downloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSamePresenter.download(mActivity, sameItem.getPageUrl());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = DetailActivity.newIntent(mContext, sameItem.getPageUrl());
                    mContext.startActivity(intent);
                }
            });
        }

    }
}

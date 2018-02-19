package ru.ratanov.kinoman.model.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import ru.ratanov.kinoman.R;
import ru.ratanov.kinoman.model.content.Film;

/**
 * Created by ACER on 02.09.2017.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> implements RealmChangeListener {

    private RealmResults<Film> mFilms;
    private Context mContext;

    public FavoriteAdapter(RealmResults<Film> films) {
        mFilms = films;
        mFilms.addChangeListener(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindItem(mFilms.get(position));
    }

    @Override
    public int getItemCount() {
        return mFilms.size();
    }

    @Override
    public void onChange(Object o) {
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView poster;
        private TextView title;

        ViewHolder(View view) {
            super(view);
            poster = (ImageView) view.findViewById(R.id.fav_poster);
            title = (TextView) view.findViewById(R.id.fav_title);
        }

        void bindItem(Film film) {
            Picasso.with(mContext).load(film.getPosterUrl()).into(poster);
            title.setText(film.getTitle());
        }
    }
}

package ru.ratanov.kinoman.ui.activity.fav;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import ru.ratanov.kinoman.R;
import ru.ratanov.kinoman.model.adapters.FavoriteAdapter;
import ru.ratanov.kinoman.model.content.Film;
import ru.ratanov.kinoman.ui.activity.base.BaseActivity;

/**
 * Created by ACER on 02.09.2017.
 */

public class FavActivity extends BaseActivity {

    @BindView(R.id.my_recycler_view)
    RecyclerView mRecyclerView;

    Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        mRealm = Realm.getDefaultInstance();
        ButterKnife.bind(this);

        setupToolBar();
        setupSearchView();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new FavoriteAdapter(mRealm.where(Film.class).findAll()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}

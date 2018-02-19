package ru.ratanov.kinoman.ui.activity.search;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.ratanov.kinoman.R;
import ru.ratanov.kinoman.model.adapters.MySearchAdapter;
import ru.ratanov.kinoman.model.content.SearchItem;
import ru.ratanov.kinoman.presentation.presenter.search.SearchPresenter;
import ru.ratanov.kinoman.presentation.view.search.SearchView;
import ru.ratanov.kinoman.ui.activity.base.BaseActivity;

public class SearchActivity extends BaseActivity implements SearchView {

    public static final String TAG = "SearchActivity";
    public static final String EXTRA_QUERY = "extra_query";

    @InjectPresenter
    SearchPresenter mSearchPresenter;

    private ProgressDialog mProgressDialog;
    @BindView(R.id.my_recycler_view)
    RecyclerView mRecyclerView;


    public static Intent newIntent(Context context, String query) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(EXTRA_QUERY, query);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        ButterKnife.bind(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        setupToolBar();
        setupSearchView();

        String query = getIntent().getStringExtra(EXTRA_QUERY);
        if (query != null) {
            doSearch(query);
            Log.d(TAG, "onCreate: search " + query);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String query = intent.getStringExtra(EXTRA_QUERY);
        if (query != null) {
            doSearch(query);
            Log.d(TAG, "onNewIntent: search " + query);
        }
    }

    private void doSearch(String query) {
        mSearchPresenter.doSearch(query);
    }

    @Override
    public void showProgress() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Поиск...");
        mProgressDialog.show();
    }

    @Override
    public void hideProgress() {
        mProgressDialog.dismiss();
    }

    @Override
    public void updatePage(List<SearchItem> items) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(String.format(Locale.ROOT,"%s: %d", "Найдено", items.size()));

        MySearchAdapter adapter = new MySearchAdapter(this, items);
        mRecyclerView.setAdapter(adapter);
    }

}

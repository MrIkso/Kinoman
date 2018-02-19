package ru.ratanov.kinoman.ui.activity.base;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;

import java.util.List;

import ru.ratanov.kinoman.R;
import ru.ratanov.kinoman.ui.activity.fav.FavActivity;
import ru.ratanov.kinoman.ui.activity.pref.OldPreferenceActivity;
import ru.ratanov.kinoman.ui.activity.pref.SettingsActivity;
import ru.ratanov.kinoman.ui.activity.search.SearchActivity;

public class BaseActivity extends MvpAppCompatActivity {

    private SearchView mSearchView;
    private SearchHistoryTable mHistoryTable;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            mSearchView.open(true, item);
        }
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.action_fav) {
            Intent intent = new Intent(this, FavActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchView.SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && results.size() > 0) {
                String query = results.get(0);
                if (!TextUtils.isEmpty(query)) {
                    if (mSearchView != null) {
                        mSearchView.setQuery(query, true);
                    }
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    protected void setupSearchView() {
        mSearchView = (SearchView) findViewById(R.id.searchview);

        mHistoryTable = new SearchHistoryTable(this);
        mHistoryTable.setHistorySize(5);

        SearchAdapter searchAdapter = new SearchAdapter(this);
        searchAdapter.addOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                String query = textView.getText().toString();
                doSearch(query);
            }
        });

        mSearchView.setAdapter(searchAdapter);

        mSearchView.setHint(R.string.search);
        mSearchView.setVoiceText(getString(R.string.please_talk));

        mSearchView.setVersion(SearchView.VERSION_MENU_ITEM);
        mSearchView.setVersionMargins(SearchView.VERSION_MARGINS_MENU_ITEM);
        mSearchView.setTheme(SearchView.THEME_LIGHT);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                doSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    void doSearch(String query) {
        mHistoryTable.addItem(new SearchItem(query));
        mSearchView.close(true);

        Intent intent = SearchActivity.newIntent(this, query);
        startActivity(intent);
    }


}

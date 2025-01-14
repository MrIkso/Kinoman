package ru.ratanov.kinoman.model.search;


import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.ratanov.kinoman.model.base.Constants;
import ru.ratanov.kinoman.model.content.SearchItem;
import ru.ratanov.kinoman.model.parsers.Cookies;
import ru.ratanov.kinoman.model.utils.QueryPreferences;
import ru.ratanov.kinoman.presentation.presenter.search.SearchPresenter;

public class SearchAPI {

    private static final boolean DEBUG = true;
    private static final String TAG = SearchAPI.class.getSimpleName();

    private SearchPresenter mSearchPresenter;
    private List<SearchItem> mSearchItems = new ArrayList<>();

    public SearchAPI(SearchPresenter searchPresenter) {
        mSearchPresenter = searchPresenter;
    }

    public void search(String query) {
        new SearchTask().execute(query);
    }

    private class SearchTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            String filter = QueryPreferences.getStoredQuery(mSearchPresenter.getContext(), "search_filter_type");
            String sort = QueryPreferences.getStoredQuery(mSearchPresenter.getContext(), "search_sort_direction");

            String query = strings[0];
            int pages = 0;

            String url = Uri.parse(Constants.geUrlSearch())
                    .buildUpon()
                    .appendQueryParameter("s", query)
                    .appendQueryParameter("t", filter)
                    .appendQueryParameter("f", sort)
                    .build()
                    .toString();

            try {
                Document doc = Jsoup
                        .connect(url)
                        .cookies(Cookies.getCookies())
                        .get();

                Elements elements = doc.select("td");
                if (DEBUG) Log.v(TAG,"Elements size " + elements.size());
                for (Element element : elements) {
                    if (element.text().contains("Найдено")) {
                        pages = getNumberPages(element.text());
                        if (DEBUG) {
                            Log.i(TAG, element.text());
                            Log.i(TAG, "pages = " + pages);
                        }
                        break;
                    }
                }

                for (int i = 0; i < pages; i++) {
                    String pageUrl = Uri.parse(Constants.geUrlSearch())
                            .buildUpon()
                            .appendQueryParameter("s", query)
                            .appendQueryParameter("t", filter)
                            .appendQueryParameter("f", sort)
                            .appendQueryParameter("page", String.valueOf(i))
                            .build()
                            .toString();

                    Document document = Jsoup.connect(pageUrl)
                            .cookies(Cookies.getCookies())
                            .get();

                    Elements sElements = document.select("tr.bg");

                    for (Element element : sElements) {
                        String title = element.select("td.nam").select("a").text();
                        String link = Constants.getBaseUrl() + element.select("td.nam").select("a").attr("href");
                        String size = element.select("td.s").get(1).text();
                        String seeds = element.select("td.sl_s").text();
                        String date = element.select("td.s").get(2).text();

                        SearchItem item = new SearchItem();
                        item.setTitle(title);
                        item.setLink(link);
                        item.setSize(size);
                        item.setSeeds(seeds);
                        item.setDate(date);

                        mSearchItems.add(item);

                        if (DEBUG) Log.i(TAG, "doInBackground: " + mSearchItems.size());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (DEBUG) Log.i(TAG, "onPostExecute: " + mSearchItems.size());
            mSearchPresenter.updatePage(mSearchItems);
        }

        private int getNumberPages(String found) {
            int count = Integer.parseInt(found.split(" ")[1]);
            int a = count / 50;
            return (a * 50 == count) ? a : a + 1;
        }
    }
}

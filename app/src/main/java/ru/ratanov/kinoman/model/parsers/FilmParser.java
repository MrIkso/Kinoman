package ru.ratanov.kinoman.model.parsers;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ru.ratanov.kinoman.model.content.Film;
import ru.ratanov.kinoman.model.content.SameItem;
import ru.ratanov.kinoman.model.content.TopItem;
import ru.ratanov.kinoman.model.utils.QueryPreferences;
import ru.ratanov.kinoman.presentation.presenter.detail.DetailPresenter;
import ru.ratanov.kinoman.presentation.presenter.detail.SamePresenter;
import ru.ratanov.kinoman.presentation.presenter.main.TopPresenter;

import static ru.ratanov.kinoman.model.base.Constants.BASE_URL;
import static ru.ratanov.kinoman.model.base.Constants.BASE_URL_TOP;
import static ru.ratanov.kinoman.model.base.Constants.KINOPOISK_URL;

/**
 * Created by ACER on 27.11.2016.
 */

public class FilmParser {

    private Context mContext;
    private TopPresenter mTopPresenter;
    private DetailPresenter mDetailPresenter;
    private SamePresenter mSamePresenter;
    public static final String TAG = "FilmParser";

    public FilmParser() {

    }

    public FilmParser(TopPresenter topPresenter) {
        mTopPresenter = topPresenter;
    }

    public FilmParser(DetailPresenter detailPresenter) {
        mDetailPresenter = detailPresenter;
    }

    public FilmParser(SamePresenter samePresenter) {
        mSamePresenter = samePresenter;
    }

    // Public Methods

    public void getTopFilms(Context context, String pageNumber) {
        mContext = context;
        new GetTopTask().execute(pageNumber);
    }

    public boolean isFilmBlocked(String url) {
        try {
            return new IsFilmBlockedTask().execute(url).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void getFilm(String url) {
        new GetFilmTask().execute(url);
    }

    public void getTrailerUrl(String kpUrl) {
        new GetTrailerUrl().execute(kpUrl);
    }

    public void getSameFilms(String searchUrl) {
        new GetSameFilms().execute(searchUrl);
    }


    // private AsyncTasks according to methods

    private class GetTopTask extends AsyncTask<String, Void, List<TopItem>> {


        @Override
        protected List<TopItem> doInBackground(String... params) {

            String page = params[0];

            String category_pref_key = "title_tab_" + page;
            String year_pref_key = "year_tab_" + page;
            String country_pref_key = "country_tab_" + page;
            String format_pref_key = "format_tab_" + page;
            String added_pref_key = "added_tab_" + page;
            String sort_pref_key = "sort_tab_" + page;

            String category = QueryPreferences.getStoredQuery(mContext, category_pref_key, String.valueOf(page));
            String year = QueryPreferences.getStoredQuery(mContext, year_pref_key, "0");
            String country = QueryPreferences.getStoredQuery(mContext, country_pref_key, "0");
            String format = QueryPreferences.getStoredQuery(mContext, format_pref_key, "0");
            String added = QueryPreferences.getStoredQuery(mContext, added_pref_key, "0");
            String sort = QueryPreferences.getStoredQuery(mContext, sort_pref_key, "0");

            System.out.println(category_pref_key + " = " + category);

            List<TopItem> items = new ArrayList<>();
            String url = Uri.parse(BASE_URL_TOP)
                    .buildUpon()
                    .appendQueryParameter("t", category)
                    .appendQueryParameter("d", year)
                    .appendQueryParameter("k", country)
                    .appendQueryParameter("f", format)
                    .appendQueryParameter("w", added)
                    .appendQueryParameter("s", sort)
                    .build()
                    .toString();

            System.out.println(url);

            try {
                Document doc = Jsoup
                        .connect(url)
                        .cookies(Cookies.getCookies())
                        .get();
                if (doc != null) {
                    Elements elements = doc.select("div.bx1").select("a");
                    for (Element entry : elements) {
                        String link = BASE_URL + entry.select("a").attr("href");
                        String title = entry.select("a").attr("title");
                        String pictureUrl = entry.select("a").select("img").attr("src");

                        TopItem item = new TopItem();
                        item.setLink(link);
                        item.setTitle(title);
                        item.setPictureUrl(pictureUrl);

                        items.add(item);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return items;
        }

        @Override
        protected void onPostExecute(List<TopItem> topItems) {
            mTopPresenter.onLoadComplete(topItems);
        }
    }

    private class IsFilmBlockedTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                Document document = Jsoup.connect(urls[0])
                        .cookies(Cookies.getCookies())
                        .get();
                String pageTitle = document.select("title").text();
                if (pageTitle.equals("Доступ закрыт")) {
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    private class GetFilmTask extends AsyncTask<String, Void, Film> {

        HashMap<String, String> hashMap = new HashMap<>();

        static final String KEY_TITLE = "Название";
        static final String KEY_ORIGINAL_TITLE = "Оригинальное название";
        static final String KEY_YEAR = "Год выпуска";
        static final String KEY_GENRE = "Жанр";
        static final String KEY_PRODUCTION = "Выпущено";
        static final String KEY_DIRECTOR = "Режиссер";
        static final String KEY_CAST = "В ролях";
        static final String KEY_QUALITY = "Качество";
        static final String KEY_VIDEO = "Видео";
        static final String KEY_AUDIO = "Аудио";
        static final String KEY_SIZE = "Размер";
        static final String KEY_LENGTH = "Продолжительность";
        static final String KEY_TRANSLATE = "Перевод";
        static final String KEY_CATEGORY = "Категория";

        @Override
        protected Film doInBackground(String... strings) {

            try {
                String link = strings[0];
                Document doc = Jsoup.connect(link)
                        .cookies(Cookies.getCookies())
                        .get();
                fillHashMap(doc.select("h2").html());
                fillHashMap(doc.select("div.justify.mn2.pad5x5").html());

                Film film = new Film();

                film.setLink(link);
                film.setId(link.substring(link.indexOf("=") + 1));
                film.setTitle(doc.select("h1").text());
                film.setPosterUrl(doc.select("img.p200").attr("src"));
                film.setQuality(getPair(KEY_QUALITY));
                film.setVideo(getPair(KEY_VIDEO));
                film.setAudio(getPair(KEY_AUDIO));
                film.setSize(getPair(KEY_SIZE));
                film.setLength(getPair(KEY_LENGTH));
                film.setTranslate((hashMap.get(KEY_TRANSLATE) == null) ? KEY_TRANSLATE + ": Не требуется" : getPair(KEY_TRANSLATE));
                film.setYear(getPair(KEY_YEAR));
                film.setGenre(getPair(KEY_GENRE));

                Elements elements = doc.select("ul.men.w200").select("li");
                for (Element element : elements) {
                    if (element.text().contains("Раздают")) {
                        film.setSeeds(element.select("span").text());
                    }
                    if (element.text().contains("Обновлен")) {
                        film.setDateTitle("Обновлен");
                        film.setDate(element.select("span").text());
                    }
                    if (element.text().contains("Залит")) {
                        film.setDateTitle("Залит");
                        film.setDate(element.select("span").text());
                    }
                    if (element.text().contains("Кинопоиск")) {
                        film.setRating(element.text().substring(9));
                        film.setKpUrl(element.select("a").attr("href"));
                    }
                    if (element.text().contains("Трейлер")) {
                        film.setTrailerUrl(element.select("a").attr("href"));
                    }
                }

                film.setDescription(doc.select("div.bx1.justify").select("p").text());
                film.setSameLink(BASE_URL + doc.select("td.w90p").select("a").attr("href"));

                return film;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Film film) {
            if (film != null) {
                mDetailPresenter.updatePage(film);
            }
        }

        private void fillHashMap(String html) {
            Document doc = Jsoup.parse(html);
            doc.outputSettings(new Document.OutputSettings().prettyPrint(false));
            doc.select("br").append("_break_");
            String string = Jsoup.clean(doc.html(), "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
            String[] block = string.split("_break_");
            for (int i = 0; i < block.length; i++) {
                block[i] = block[i].trim();
                String[] pairs = block[i].split(": ");
                hashMap.put(pairs[0], pairs[1]);
            }
        }

        private String getPair(String key) {
            return key + ": " + hashMap.get(key);
        }
    }

    private class GetTrailerUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {

                Connection.Response response = Jsoup.connect(strings[0]).followRedirects(true).execute();
                Log.i(TAG, "Response: " + response.url());
                String mainUrl = response.url().toString();

                if (mainUrl.contains("showcaptcha")) {
                    mainUrl = mainUrl.replace("https://www.kinopoisk.ru/showcaptcha?cc=1&repath=", "");
                    mainUrl = mainUrl.replace("%3A", ":");
                }

                Document doc = Jsoup.connect(mainUrl).get();

                Elements frameLink = doc.select("meta[property=og:video:url]");

                Log.i(TAG, "Meta: " + frameLink.size());

                //
                String pageUrl = strings[0] + "/video/type/1";
                Log.i(TAG, "doInBackground: pageUrl " + pageUrl);
                Document document = Jsoup.connect(pageUrl).get();

                Elements videos = document.select("a.continue");
                Log.i(TAG, "doInBackground: found videos " + videos.size() );

                for (Element video : videos) {
                    Log.i(TAG, "doInBackground: v " + video.attr("href"));
                }


                for (Element video : videos) {
                    if (video.text().contains("Высокое качество")) {
                        int start = video.attr("href").indexOf("http");
                        return video.attr("href").substring(start);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String trailerUrl) {
            mDetailPresenter.showTrailer(trailerUrl);
        }
    }

    private class GetSameFilms extends AsyncTask<String, Void, List<SameItem>> {

        List<SameItem> items = new ArrayList<>();

        @Override
        protected List<SameItem> doInBackground(String... strings) {
            try {
                Document doc = Jsoup
                        .connect(strings[0])
                        .cookies(Cookies.getCookies())
                        .get();
                Elements elements = doc.select("tr.bg");
                for (Element row : elements) {
                    SameItem sameItem = new SameItem();

                    sameItem.setTitle(row.select("td.nam").text());
                    sameItem.setSize(row.select("td").get(3).text());
                    sameItem.setSeeds(row.select("td").get(4).text());
                    sameItem.setDate(row.select("td").get(6).text());
                    sameItem.setPageUrl(BASE_URL + row.select("td.nam").select("a").attr("href"));

                    items.add(sameItem);
                }
                return items;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<SameItem> sameItems) {
            mSamePresenter.showSameFilms(sameItems);
        }
    }
}

package ru.ratanov.kinoman.model.utils;

import android.content.Context;
import android.preference.PreferenceManager;

public class QueryPreferences {
    public static final String SERVER_TYPE = "torrent_client_type";
    public static final String PREF_SERVER = "server";
    public static final String PREF_PORT = "port";
    public static final String PREF_LOGIN = "login";
    public static final String PREF_PASSWORD = "password";

    public static String getStoredQuery(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(key, null);
    }

    public static String getStoredQuery(Context context, String key, String defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(key, defaultValue);
    }

    public static void setStoredQuery(Context context, String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(key, value)
                .apply();
    }

}

package ru.ratanov.kinoman.model.utils;

import android.content.Context;
import android.preference.PreferenceManager;

public class QueryPreferences {
    public static final String SERVER_TYPE = "torrent_client_type";
    public static final String PREF_SERVER = "server";
    public static final String PREF_PORT = "port";
    public static final String PREF_LOGIN = "login";
    public static final String PREF_PASSWORD = "password";
    public static final String PREF_KINOZAL_URL = "kinozal_url";
    public static final String PREF_SHOW_SET_URL = "show_set_kinozal_url";

    public static String getStoredQuery(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(key, null);
    }

    public static String getStoredQuery(Context context, String key, String defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(key, defaultValue);
    }

    public static boolean getStoredBoolean(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(key, true);
    }

    public static void setStoredBoolean(Context context, String key, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    public static void setStoredQuery(Context context, String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(key, value)
                .apply();
    }

}

package ru.ratanov.kinoman.model.parsers;


import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ru.ratanov.kinoman.model.base.Constants;

public class Cookies {

    private static Map<String, String> cookies = null;

    public static Map<String, String> getCookies() {
        if (cookies == null) {
           cookies = updateCookies();
        }
        return cookies;
    }

    private static Map<String, String> updateCookies() {

            Connection.Response response = null;

            try {
                response = Jsoup
                        .connect(Constants.getBaseUrl() + "/login.php")
                        .method(Connection.Method.GET)
                        .execute();

                response = Jsoup
                        .connect(Constants.getBaseUrl() + "/takelogin.php")
                        .cookies(response.cookies())
                        .data("username", "rbaloo")
                        .data("password", "756530")
                        .method(Connection.Method.POST)
                        .followRedirects(true)
                        .execute();
                return response.cookies();
            } catch (IOException e) {
                e.printStackTrace();
            }

          return new HashMap<>();
        }
}

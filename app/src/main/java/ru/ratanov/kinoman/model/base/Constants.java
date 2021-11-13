package ru.ratanov.kinoman.model.base;

/**
 * Created by ACER on 28.12.2016.
 */

public class Constants {

    private static String BASE_URL = "http://kinozal.tv";

    public static void setBaseUrl(String url){
        BASE_URL = url;
    }

    public static String getBaseUrl(){
        return BASE_URL;
    }

    public static String geUrlTop(){
        return  getBaseUrl() + "/top.php";
    }

    public static String geUrlSearch(){
        return getBaseUrl() + "/browse.php";
    }
}

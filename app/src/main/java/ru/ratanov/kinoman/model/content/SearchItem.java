package ru.ratanov.kinoman.model.content;

import java.util.Arrays;
import java.util.List;

public class SearchItem {
    private String posterUrl;
    private String title;
    private String year;
    private String genre;
    private String rating;
    private String country;
    private String link;

    private static final List<String> SERIALS = Arrays.asList("45", "46");
    private static final List<String> FILMS = Arrays.asList("8", "6", "15", "17", "35", "39", "13", "14", "24", "11", "10", "9", "47", "18", "37", "12", "7", "48", "49", "50", "38", "16");
    private static final List<String> MULTS = Arrays.asList("21", "22", "20");

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    // TODO: 14.12.2016 Re-write logic. Can pass 'Terminator (3)' for example
    public static String getTitleWithoutSeries(String title) {
        if (title.contains("(") && title.contains(")")) {
            int start = title.lastIndexOf("(") - 1;
            int end = title.lastIndexOf(")") + 1;
            String series = title.substring(start, end);
            return title.replace(series, "");
        } else {
            return title;
        }
    }

    public static boolean isFilm(String category) {
        return FILMS.contains(category);
    }

    public static boolean isSerial(String category) {
        return SERIALS.contains(category);
    }

    public static boolean isMult(String category) {
        return MULTS.contains(category);
    }

    public static boolean isValid(String category) {
        return isFilm(category) || isSerial(category) || isMult(category);
    }

}

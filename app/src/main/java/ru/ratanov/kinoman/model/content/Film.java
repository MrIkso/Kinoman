package ru.ratanov.kinoman.model.content;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by ACER on 08.12.2016.
 */

public class Film extends RealmObject {

    private String link;
    private String id;
    private String title;
    private String posterUrl;
    private String quality;
    private String video;
    private String audio;
    private String size;
    private String length;
    private String translate;
    private String year;
    private String genre;
    private String rating;
    private String date;
    private String dateTitle;
    private String seeds;
    private String description;
    private String trailerUrl;
    private boolean isFavorite;
    private String KpUrl;
    private String sameLink;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public String getYearText() {
        return year;
    }

    public String getYearNumber() {
        return year.substring(year.length() - 4);
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateTitle() {
        return dateTitle;
    }

    public void setDateTitle(String dateTitle) {
        this.dateTitle = dateTitle;
    }

    public String getSeeds() {
        return seeds;
    }

    public void setSeeds(String seeds) {
        this.seeds = seeds;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getKpUrl() {
        return KpUrl;
    }

    public void setKpUrl(String kpUrl) {
        KpUrl = kpUrl;
    }

    public String getSameLink() {
        return sameLink;
    }

    public void setSameLink(String sameLink) {
        this.sameLink = sameLink;
    }

}

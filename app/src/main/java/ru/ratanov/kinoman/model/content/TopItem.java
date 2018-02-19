package ru.ratanov.kinoman.model.content;

import java.io.Serializable;

public class TopItem implements Serializable {
    private String mPictureUrl;
    private String mTitle;
    private String mLink;

    public String getPictureUrl() {
        return mPictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        mPictureUrl = pictureUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public String getId() {
        return mLink.substring(33);
    }
}

package ru.ratanov.kinoman.model.content;

import io.realm.RealmObject;

/**
 * Created by ACER on 02.09.2017.
 */

public class FavFilm extends RealmObject {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.id;
    }
}

package com.suribada.rxjavabook.chap1;

/**
 * Created by lia on 2018-02-05.
 */

public class Bookmark {

    String userId;
    String url;
    int popularity;

    public Bookmark(String userId, String url, int popularity) {
        this.userId = userId;
        this.url = url;
        this.popularity = popularity;
    }

    public String getUrl() {
        return url;
    }

    public int getPopularity() {
        return popularity;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Bookmark{" +
                "userId='" + userId + '\'' +
                ", url='" + url + '\'' +
                ", popularity=" + popularity +
                '}';
    }
}

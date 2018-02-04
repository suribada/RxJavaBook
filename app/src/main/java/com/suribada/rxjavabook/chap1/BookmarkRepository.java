package com.suribada.rxjavabook.chap1;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by lia on 2018-01-31.
 */
public class BookmarkRepository {

    private long id;
    private String title;
    private String url;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getTitle(Callback<String> callback) {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTitle(String title, Callback<Void> callback) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public String getUrl(Callback<String> callback) {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUrl(String url, Callback<Void> callback) {
        this.url = url;
    }

    public String getFetchedTitle() {
        return title;
    }

    public String getFetchedUrl() {
        return url;
    }

    public boolean savedSuccessful() {
        return true;
    }

    public boolean fetchedTitleSuccessful() {
        return true;
    }

    interface Callback<T> {
        void onSucess(T t);
        void onFailed(Exception e);
    }

    Completable setTitleRxJava(String title) {
        return Completable.fromCallable(() -> {
            setTitle("heetak");
            return null;
        });
    }

    Single<String> getTitleRxJava() {
        return Single.fromCallable(() -> "http://endofhope");
    }

}

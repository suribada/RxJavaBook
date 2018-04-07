package com.suribada.rxjavabook.api.model;

/**
 * Created by Noh.Jaechun on 2018-04-08.
 */
public class Book {
    public int categoryId;
    public String title;
    public String author;

    @Override
    public String toString() {
        return "Book{" +
                "categoryId=" + categoryId +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}

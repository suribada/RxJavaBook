package com.suribada.rxjavabook.api.model;

/**
 * Created by Noh.Jaechun on 2018-04-08.
 */
public class Book {
    public int categoryId;
    public String title;
    public String author;

    public Book(int categoryId, String title, String author) {
        this.categoryId = categoryId;
        this.title = title;
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book{" +
                "categoryId=" + categoryId +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}

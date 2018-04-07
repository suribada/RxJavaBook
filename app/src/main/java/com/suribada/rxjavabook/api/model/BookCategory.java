package com.suribada.rxjavabook.api.model;

/**
 {
 "categoryId" : 1,
 "categoryName" : "소설"
 }
 * Created by Noh.Jaechun on 2018-04-08.
 */
public class BookCategory {
    public int categoryId;
    public String categoryName;

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}

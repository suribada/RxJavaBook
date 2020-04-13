package com.suribada.rxjavabook.chap7;

import com.suribada.rxjavabook.api.model.Book;

import org.junit.Test;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class OnErrorTest {

    @Test
    public void onErrorResume() {
        getRecommendedBooks()
                .onErrorResumeWith(getBestSellerBooks())
                .subscribe(this::showBooks);
    }

    private void showBooks(List<Book> books) {
    }

    private Observable<List<Book>> getRecommendedBooks() {
        return null;
    }

    private Observable<List<Book>> getBestSellerBooks() {
        return null;
    }
}

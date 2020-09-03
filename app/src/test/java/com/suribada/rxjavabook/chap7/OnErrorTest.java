package com.suribada.rxjavabook.chap7;

import android.location.Location;

import com.suribada.rxjavabook.api.model.Book;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OnErrorTest {

    @Test
    public void onErrorResume() {
        getRecommendedBooks()
                .onErrorResumeWith(getBestSellerBooks())
                .subscribe(this::showBooks);
    }

    private void showBooks(List<Book> books) {
        System.out.println(books);
    }

    private Observable<List<Book>> getRecommendedBooks() {
        return Observable.error(new NullPointerException("why null?"));
    }

    private Observable<List<Book>> getBestSellerBooks() {
        return Observable.just(Arrays.asList(new Book(1234, "노재춘", "RxJava")));
    }

    @Test
    public void convertKRW() {
        Observable.just(1, 2, -1, 1, 2)
                .flatMap(dollar -> getCurrentPrice4(dollar)
                        .onErrorReturnItem("0 won")) // (1)
                .subscribe(System.out::println,
                        System.err::println,
                        () -> System.out.println("onComplete"));
    }

    @Test
    public void testOnErrorReturnItemContinued() {
        Observable.just(1, 2, -1, 1, 2)
                .flatMap(dollar -> getCurrentPrice4(dollar)
                        .onErrorComplete()) // (1)
                .subscribe(System.out::println,
                        System.err::println,
                        () -> System.out.println("onComplete"));
    }

    private Observable<String> getCurrentPrice4(int dollar) {
        if (dollar < 0) {
            return Observable.error( // (1) 시작
                    new IllegalArgumentException("dollar should be bigger than 0")); // (1) 끝
        }
        return Observable.just((dollar * 1000) + " won"); // (2)
    }

}

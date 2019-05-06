package com.suribada.rxjavabook.chap7;

import org.junit.Test;

import io.reactivex.Observable;

public class SkipTest {

    @Test
    public void skipLast() {
        Observable.range(1, 7)
                .skipLast(3)
                .subscribe(System.out::println);
    }

    @Test
    public void skipWhile() {
        Observable.just(1, 3, 5, 2, 7)
                .skipWhile(x -> x <= 4)
                .subscribe(System.out::println);
        System.out.println("divider");
        Observable.just(1, 3, 5, 2, 7)
                .filter(x -> x > 4)
                .subscribe(System.out::println);
    }
}

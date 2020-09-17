package com.suribada.rxjavabook.chap7;

import org.junit.Test;

import io.reactivex.rxjava3.core.Observable;

public class FilterTest {

    @Test
    public void filter() {
        Observable.just(1, 2, 4, 7, 8, 11, 14)
                .filter(x -> x % 2 == 0) // (1)
                .subscribe(System.out::println);
    }
}

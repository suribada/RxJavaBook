package com.suribada.rxjavabook.chap7;

import org.junit.Test;

import io.reactivex.rxjava3.core.Observable;

public class BooleanTest {

    @Test
    public void testContains() {
        Observable.just("김", "이", "노").contains("이")
                .subscribe(System.out::println);
        Observable.just("김", "이", "노").any(value -> value == "이" || value.equals("이"))
                .subscribe(System.out::println);
    }

    @Test
    public void testIsEmpty() {
        Observable.just(1).all(value -> false)
                .subscribe(System.out::println);
    }
}

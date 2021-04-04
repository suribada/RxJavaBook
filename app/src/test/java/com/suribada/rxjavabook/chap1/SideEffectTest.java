package com.suribada.rxjavabook.chap1;

import android.util.Log;

import org.junit.Test;

import io.reactivex.rxjava3.core.Observable;

public class SideEffectTest {

    @Test
    public void includeSideEffect() {
        Observable.just(1, 2, 4, 7, 8, 11, 14)
                .filter(x -> x % 2 == 0)
                .map(x -> { // (1) 시작
                    System.out.println("value=" + x);
                    return x * 100;
                }) // (1) 끝
                .subscribe(System.out::println);
    }

    @Test
    public void separateSideEffect() {
        Observable.just(1, 2, 4, 7, 8, 11, 14)
                .filter(x -> x % 2 == 0)
                .doOnNext(x -> System.out.println("value=" + x)) // (1)
                .map(x -> x * 100)  // (2)
                .subscribe(System.out::println);
    }
}

package com.suribada.rxjavabook.chap6;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class ConcatMapTest {

    @Test
    public void testOnErrorReturnItem() {
        Observable.just(1, 2, 0, 3, 2)
                .map(value -> 10 /value)
                .onErrorReturnItem(0)
                .subscribe(System.out::println,
                        System.err::println,
                        () -> System.out.println("onComplete"));
    }

    @Test
    public void testConcatOnErrorReturnItem() {
        List<Observable<Integer>> messageObservables = new ArrayList<>();
        messageObservables.add(Observable.just(1, 2, 0, 3, 2)
                .map(value -> 10 /value)
                .onErrorReturnItem(0));
        messageObservables.add(Observable.just(1, 4, 4, 0, 2)
                .map(value -> 10 /value)
                .onErrorReturnItem(0));
        messageObservables.add(Observable.just(8));
        Observable.concat(messageObservables)
                .subscribe(System.out::println,
                e -> e.printStackTrace(),
                () -> System.out.println("onComplete"));
    }

    @Test
    public void testConcatOnErrorReturnItem2() {
        List<Observable<Integer>> messageObservables = new ArrayList<>();
        messageObservables.add(Observable.just(1, 2, 0, 3, 2)
                .map(value -> 10 /value));
        messageObservables.add(Observable.just(1, 4, 4, 0, 2)
                .map(value -> 10 /value)
                .onErrorReturnItem(0));
        Observable.concat(messageObservables)
                .subscribe(System.out::println,
                        e -> e.printStackTrace(),
                        () -> System.out.println("onComplete"));
    }

}

package com.suribada.rxjavabook.chap4;

import org.junit.Test;

import io.reactivex.Observable;

/**
 * Created by Noh.Jaechun on 2018. 5. 24..
 */
public class CollectionTest {

    @Test
    public void testCollection() {
        Observable<Integer> firstObervable = Observable.just(1, 2, 4, 5, 7, 3);
        Observable<Integer> secondObervable = Observable.just(1, 3, 5, 7, 9);
        Observable<Integer> resultObservable = firstObervable.flatMap(x -> secondObervable,
                (x, y) -> {
                    if (x == y) {
                        return Observable.just(x);
                    } else {
                        return Observable.<Integer>empty();
                    }
                }).flatMap(x -> x);
        resultObservable.subscribe(System.out::println);
        resultObservable.toList().subscribe(System.out::println);

    }
}

package com.suribada.rxjavabook.chap5;

import org.junit.Test;

import io.reactivex.Observable;

public class ConcatTest {

    @Test
    public void justConcat() {
        Observable<Integer> obs = Observable.just(1, 2, 3);
        Observable.concat(obs, obs).subscribe(System.out::println);
    }

    @Test
    public void firstNotComplete() {
        Observable<Integer> obs = Observable.create(emitter -> {
            emitter.onNext(6);
            emitter.onNext(7);
        });
        Observable<Integer> obs2 = Observable.just(1, 2, 3);
        Observable.concat(obs, obs2).subscribe(System.out::println);
    }
}

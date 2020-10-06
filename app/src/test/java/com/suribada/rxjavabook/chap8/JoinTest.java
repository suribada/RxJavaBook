package com.suribada.rxjavabook.chap8;

import androidx.core.util.Pair;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;

public class JoinTest {

    /**
    http://androidahead.com/2018/01/09/rxjava-operators-part-8-join-operator/ 참고
     */
    @Test
    public void join() {
        Observable<Long> left = Observable.interval(100, TimeUnit.MILLISECONDS).take(4); // (1)
        Observable<Long> right = Observable.interval(150, TimeUnit.MILLISECONDS).take(5); // (2)
        left.join(right,
                    l -> Observable.timer(200, TimeUnit.MILLISECONDS), // (3)
                    r -> Observable.timer(250, TimeUnit.MILLISECONDS), // (4)
                    (l, r) -> Arrays.asList(l, r)) // (5)
        .subscribe(System.out::println);
        SystemClock.sleep(5000);
    }

    @Test
    public void groupJoin() {
        Observable<Long> left = Observable.interval(100, TimeUnit.MILLISECONDS).take(4);
        Observable<Long> right = Observable.interval(150, TimeUnit.MILLISECONDS).take(5);
        left.groupJoin(right,
                i -> Observable.timer(200, TimeUnit.MILLISECONDS),
                i -> Observable.timer(250, TimeUnit.MILLISECONDS),
                (l, r) -> r.toList().map(list -> Pair.create(l, list))) // (1)
                .flatMapSingle(x -> x) // (2)
                .subscribe(System.out::println);
        SystemClock.sleep(5000);
    }
}

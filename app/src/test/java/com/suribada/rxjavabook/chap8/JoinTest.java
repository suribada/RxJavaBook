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
        Observable<Long> left = Observable
                .interval(100, TimeUnit.MILLISECONDS).take(4);

        Observable<Long> right = Observable
                .interval(150, TimeUnit.MILLISECONDS).take(5);

        left.join(right,
                    i -> Observable.timer(200, TimeUnit.MILLISECONDS),
                    i -> Observable.timer(250, TimeUnit.MILLISECONDS),
                    (l, r) -> Arrays.asList(l.intValue(), r.intValue()))
        .subscribe(System.out::println);
        SystemClock.sleep(5000);
    }

    @Test
    public void groupJoin() {
        Observable<Long> left = Observable
                .interval(100, TimeUnit.MILLISECONDS).take(4);

        Observable<Long> right = Observable
                .interval(150, TimeUnit.MILLISECONDS).take(5);

        left.groupJoin(right,
                i -> Observable.timer(200, TimeUnit.MILLISECONDS),
                i -> Observable.timer(250, TimeUnit.MILLISECONDS),
                (l, r) -> Pair.create(l, r.toList()))
                .flatMapSingle(x -> x.second)
                .subscribe(System.out::println);
        SystemClock.sleep(5000);
    }
}

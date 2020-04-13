package com.suribada.rxjavabook.chap7;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Created by Noh.Jaechun on 2019. 7. 11..
 */
public class ThrottleTest {

    @Test
    public void throttleFirst() {
        Observable.interval(0, 100, TimeUnit.MILLISECONDS)
                .take(20)
                .throttleFirst(300, TimeUnit.MILLISECONDS, Schedulers.single())
                .subscribe(value -> System.out.println(Thread.currentThread().getName() + ":" + value));
        SystemClock.sleep(3000);
    }

    /**
     * 1, 3, 5, 6
     */
    @Test
    public void throttleLast() {
        Observable.interval(5, TimeUnit.SECONDS) // (1)
                .take(7) // (2)
                .sample(10, TimeUnit.SECONDS, Schedulers.single(), true) // (3)
                .subscribe(value -> System.out.println(Thread.currentThread().getName() + ":" + value));
        SystemClock.sleep(40000);
    }

    @Test
    public void throttleLatest() {
        Observable.interval(5, TimeUnit.SECONDS)
                .take(7)
                .throttleLatest(10, TimeUnit.SECONDS, Schedulers.single(), true) // (1)
                .subscribe(value -> System.out.println(Thread.currentThread().getName() + ":" + value));
        SystemClock.sleep(40000);
    }

    @Test
    public void debounce() {
        Observable<String> obs1 = Observable.interval(100, TimeUnit.MILLISECONDS) // (1) 시작
                .map(value -> "obs1: " + value)
                .take(3); // (1) 끝
        Observable<String> obs2 = Observable.interval(200, TimeUnit.MILLISECONDS) // (2) 시작
                .map(value -> "obs2: " + value)
                .take(3); // (2) 시작
        Observable<String> obs3 = Observable.interval(400, TimeUnit.MILLISECONDS) // (3) 시작
                .map(value -> "obs3: " + value)
                .take(3); // (3) 시작
        Observable.concat(obs1, obs2, obs3) // (4)
                .debounce(300, TimeUnit.MILLISECONDS) // (5)
                .subscribe(System.out::println);
        SystemClock.sleep(5000);
    }

}

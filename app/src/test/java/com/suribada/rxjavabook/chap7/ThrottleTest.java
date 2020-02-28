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

}

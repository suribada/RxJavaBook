package com.suribada.rxjavabook.chap7;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

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
}

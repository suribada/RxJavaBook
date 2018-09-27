package com.suribada.rxjavabook.chap5;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public class IntervalTest {

    @Test
    public void testSwalow() {
        Observable.interval(1, TimeUnit.SECONDS)
                .flatMap(value -> mappedValue(value)
                        .onErrorResumeNext(Observable.empty()))
                .subscribe(System.out::println);
        SystemClock.sleep(20000);
    }

    private Observable mappedValue(long value) {
        if (value == 5) {
            return Observable.error(new IllegalArgumentException("5 is not allowd"));
        }
        return Observable.just(value * 1000);
    }
}

package com.suribada.rxjavabook.chap11;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;

public class BlockingTest {

    @Test
    public void elapsedSeconds() {
        Observable.interval(5, TimeUnit.SECONDS).take(12)
                .map(v -> (v + 1) * 5 + "초 경과")
                .subscribe(System.out::println, e -> {}, () -> System.out.println("1분 경과"));
    }

    /**
     * 1분 경과는 나올 수도 있고 아날 수도 있다. 스레드니까
     */
    @Test
    public void elapsedSeconds2() {
        Observable.interval(5, TimeUnit.SECONDS).take(12)
                .map(v -> (v + 1) * 5 + "초 경과")
                .subscribe(System.out::println, e -> {}, () -> System.out.println("1분 경과"));
        SystemClock.sleep(61000);
    }

    @Test
    public void elapsedSeconds3() {
        Observable.interval(5, TimeUnit.SECONDS).take(12)
                .map(v -> (v + 1) * 5 + "초 경과")
                .blockingSubscribe(System.out::println, e -> {}, () -> System.out.println("1분 경과"));
    }
}

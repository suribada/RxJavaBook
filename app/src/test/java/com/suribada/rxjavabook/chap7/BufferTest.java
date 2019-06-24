package com.suribada.rxjavabook.chap7;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class BufferTest {

    @Test
    public void testBuffer() {
        Observable.range(1, 10)
                .buffer(3, 2) // (1)
                .subscribe(System.out::println);
        System.out.println("----");
        Observable.range(1, 10)
                .buffer(3, 3)
                .subscribe(System.out::println);
        System.out.println("----");
        Observable.range(1, 10)
                .buffer(3, 5)
                .subscribe(System.out::println);
    }

    @Test
    public void testScheduler() {
        Observable.interval(40, TimeUnit.MILLISECONDS, Schedulers.io()) // (1)
                .take(8)
                .buffer(100, TimeUnit.MILLISECONDS) // (2)
                .subscribe(value -> System.out.println(Thread.currentThread().getName() + ", " + value));
        SystemClock.sleep(3000);
    }

    @Test
    public void testScheduler2() {
        // buffer 기준 미달이기 때문에 무의미
        Observable.range(1, 10)
                .buffer(500, TimeUnit.MILLISECONDS)
                .subscribe(value -> System.out.println(Thread.currentThread().getName() + ", " + value));
        SystemClock.sleep(1000);
    }
}

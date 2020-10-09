package com.suribada.rxjavabook.chap8;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;

public class ZipTest {

    @Test
    public void zip() {
        Observable.zip(Observable.range(1, 4), Observable.just("A", "B", "C"), (x, y) -> (x + y))
                .subscribe(System.out::println, System.err::println, () -> System.out.println("onComplete"));
    }

    /**
     * 두 Observable 간에 시간차가 있음
     */
    @Test
    public void zip_interval_problem() {
        Observable.zip(Observable.interval(10, TimeUnit.SECONDS).map(x -> x * 10000), // (1)
                Observable.interval(11, TimeUnit.SECONDS), // (2)
                (x, y) -> x + y) // (3)
                .subscribe(System.out::println);
                //.subscribe(v -> System.out.println(System.currentTimeMillis() + ":" + v));
        SystemClock.sleep(250000);
    }

    @Test
    public void zip_much_differnce() {
        Observable.zip(Observable.interval(1, TimeUnit.MINUTES),
                Observable.interval(1, TimeUnit.NANOSECONDS), (x, y) -> x + y)
                .subscribe(System.out::println);
        SystemClock.sleep(1000 * 60 * 10);
    }

    @Test
    public void zipWithIterable() {
        Observable.just(1, 2, 3, 4).zipWith(Arrays.asList("A", "B", "C"), (x, y) -> x + y)
                .subscribe(System.out::println);
    }

    @Test
    public void zipWithIterable2() {
        Observable.just(1, 2, 3, 4).zipWith(Observable.fromIterable(Arrays.asList("A", "B", "C")), (x, y) -> x + y)
                .subscribe(System.out::println);
    }
}

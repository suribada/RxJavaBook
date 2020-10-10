package com.suribada.rxjavabook.chap8;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;

public class ZipTest {

    @Test
    public void zip() {
        Observable.zip(Observable.interval(0, 5, TimeUnit.SECONDS)
                .doOnNext(System.out::println)
                        .doOnDispose(() -> System.out.println("disposed"))
                , Observable.just("A", "B", "C"), (x, y) -> (x + y))
                .subscribe(System.out::println, System.err::println, () -> System.out.println("onComplete"));
        SystemClock.sleep(200000);
    }

    /**
     * 여기서 disposed가 출력 안 되는 이유는 메인 스레드에서 1~4까지 이미 소비했기 때문이다.
     */
    @Test
    public void zip2() {
        Observable.zip(Observable.range(1, 4)
                        .doOnNext(System.out::println)
                        .doOnDispose(() -> System.out.println("disposed"))
                , Observable.just("A", "B", "C"), (x, y) -> (x + y))
                .subscribe(System.out::println, System.err::println, () -> System.out.println("onComplete"));
    }

    /**
     * 두 Observable 간에 시간차가 있음
     */
    @Test
    public void zip_interval_problem() {
        Observable.zip(Observable.interval(10, TimeUnit.SECONDS).map(x -> x * 10000),
                Observable.interval(11, TimeUnit.SECONDS), (x, y) -> x + y)
                .subscribe(v -> System.out.println(System.currentTimeMillis() + ":" + v));
        SystemClock.sleep(250000);
    }

    @Test
    public void zip_much_differnce() {
        Observable.zip(Observable.interval(1, TimeUnit.MINUTES),
                Observable.interval(1, TimeUnit.NANOSECONDS), (x, y) -> x + y)
                .subscribe(System.out::println);
        SystemClock.sleep(1000 * 60 * 10);
    }
}

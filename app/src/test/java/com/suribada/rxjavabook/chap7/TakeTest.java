package com.suribada.rxjavabook.chap7;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public class TakeTest {

    @Test
    public void intervalAndTake() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .doOnNext(x -> System.out.println("onNext=" + x))
                .filter(x -> x < 10)
                .subscribe(System.out::println);
        SystemClock.sleep(2000);
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .doOnNext(x -> System.out.println("onNext=" + x))
                .take(10)
                .subscribe(System.out::println);
        SystemClock.sleep(2000);
    }

    @Test
    public void defaultTake() {
        Observable.just(1, 2, 3, 4)
                .take(2, TimeUnit.SECONDS)
                .subscribe(value -> System.out.println("Thread=" + Thread.currentThread().getName() + ", value=" + value));
        SystemClock.sleep(5000);
    }

    @Test
    public void takeTime() {
        Observable.interval(60, TimeUnit.MILLISECONDS) // (1)
                .doOnDispose(() -> System.out.println("dispose time=" + System.currentTimeMillis())) // (2)
                .take(200, TimeUnit.MILLISECONDS) // (3)
                .doOnComplete(() -> System.out.println("onComplete time=" + System.currentTimeMillis())) // (4)
                .subscribe(value -> System.out.println("onNext time=" + System.currentTimeMillis()
                        + ",value=" + value)); // (5)
        SystemClock.sleep(2000);
    }

    /**
     * 큐에 추가한 것이 onComplete하는 순간에 한꺼번에 내보내지는 지 확인
     */
    @Test
    public void takeLast() {
        Observable<Integer> obs = Observable.create(emitter -> {
            emitter.onNext(1);
            SystemClock.sleep(1000);
            emitter.onNext(2);
            SystemClock.sleep(1000);
            emitter.onNext(3);
            SystemClock.sleep(1000);
            emitter.onNext(4);
            SystemClock.sleep(1000);
            emitter.onNext(5);
            emitter.onComplete();
        });
        obs.takeLast(2, TimeUnit.SECONDS)
                .subscribe(System.out::println);
        SystemClock.sleep(2000);
    }

    /**
     * takeUntil과 takeWhile 비교
     */
    @Test
    public void takeUntilAndTakeWhile() {
        Observable.just(1, 3, 5, 2, 7)
                .takeUntil(x -> x >= 4)
                .subscribe(System.out::println);
        System.out.println("divider");
        Observable.just(1, 3, 5, 2, 7)
                .takeWhile(x -> x < 4)
                .subscribe(System.out::println);
    }
}

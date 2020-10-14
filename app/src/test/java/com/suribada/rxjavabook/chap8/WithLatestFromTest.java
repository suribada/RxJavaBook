package com.suribada.rxjavabook.chap8;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WithLatestFromTest {

    @Test
    public void withLatestFrom_sameThread() {
        Observable.range(1, 4) // (1)
                .withLatestFrom(Observable.just("A", "B", "C"), // (2)
                (x, y) -> (x + y)) // (3)
                .subscribe(System.out::println);
    }

    @Test
    public void withLatestFrom_sameThread2() {
        Observable.interval(4, TimeUnit.SECONDS)
                .withLatestFrom(Observable.just("A", "B", "C").doOnNext(v -> System.out.println(Thread.currentThread().getName())), // (2)
                        (x, y) -> (x + y))
                .subscribe(System.out::println);
        SystemClock.sleep(5000);
    }

    @Test
    public void withLatestFrom_differentThread() {
       Observable.interval(4, TimeUnit.SECONDS, Schedulers.single())
               .withLatestFrom(Observable.just("A", "B", "C").delay(1, TimeUnit.SECONDS).doOnNext(v -> System.out.println(Thread.currentThread().getName())), // (2)
               (x, y) -> (x + y))
               .subscribe(System.out::println);
       SystemClock.sleep(20000);
    }

    @Test
    public void withLatestFrom() {
        Observable<Integer> engishScores = Observable.interval(0, 2, TimeUnit.SECONDS) // (1) 시작
                .map(ignored -> ThreadLocalRandom.current().nextInt(60, 80))
                .doOnNext(v -> System.out.println("englishTest=" + v)); // (1)  끝
        Observable<Integer> toeicScores = Observable.interval(1, TimeUnit.SECONDS) // (2) 시작
                .map(ignored -> ThreadLocalRandom.current().nextInt(600, 900))
                .doOnNext(v -> System.out.println("toeic=" + v)); // (2) 끝
        engishScores.withLatestFrom(toeicScores, (x, y) -> x + y) // (3)
                .subscribe(total -> System.out.println("total=" + total));
        SystemClock.sleep(20000);

    }

    public Observable<Integer> getEnglishTestScore() {
        return Observable.interval(2, TimeUnit.SECONDS)
                .map(ignored -> ThreadLocalRandom.current().nextInt(60, 80))
                .doOnNext(v -> System.out.println("englishTest=" + v));

    }

    public Observable<Integer> getToeicScore() {
        return Observable.interval(1, TimeUnit.SECONDS)
                .map(ignored -> ThreadLocalRandom.current().nextInt(600, 900))
                .doOnNext(v -> System.out.println("toeic=" + v));
    }
}

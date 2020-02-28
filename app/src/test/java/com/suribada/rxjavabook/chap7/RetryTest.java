package com.suribada.rxjavabook.chap7;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;

public class RetryTest {

    @Test
    public void normal() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10)
                .map(i -> 10 / (5 - i))
                .subscribe(System.out::println, System.err::println);
        SystemClock.sleep(3000);
    }

    @Test
    public void retry() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10)
                .map(i -> 10 / (5 - i))
                .retry()
                .subscribe(System.out::println, System.err::println);
        SystemClock.sleep(3000);
    }

    @Test
    public void retryUntil() {
        long start = System.currentTimeMillis();
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10)
                .map(i -> 10 / (5 - i))
                .retryUntil(() -> System.currentTimeMillis() - start > 5000)
                .subscribe(System.out::println, System.err::println);
        SystemClock.sleep(10000);
    }

    @Test
    public void retryPredicate() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10)
                .map(i -> 10 / (5 - i))
                .retry(e -> e instanceof ArithmeticException)
                .subscribe(System.out::println);
        SystemClock.sleep(3000);
    }

    @Test
    public void retryPredicate2() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10)
                .map(i -> 10 / (5 - i))
                .retry(3, e -> e instanceof ArithmeticException)
                .subscribe(System.out::println);
        SystemClock.sleep(3000);
    }

    @Test
    public void retryBiPredicate() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10)
                .map(i -> 10 / (5 - i))
                .retry((times, e) -> times < 3 && e instanceof ArithmeticException)
                .subscribe(System.out::println);
        SystemClock.sleep(3000);
    }

    /**
     * 이런 식은 안 됨
     */
    @Test
    public void retryWhenWithFault1() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10)
                .map(i -> 10 / (5 - i))
                .retryWhen(errors -> Observable.timer(1, TimeUnit.SECONDS))
                .subscribe(System.out::println);
        SystemClock.sleep(10000);
    }

    @Test
    public void retryWhenDelayed() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10)
                .map(i -> 10 / (5 - i))
                .retryWhen(errors -> errors.flatMap(e -> Observable.timer(1, TimeUnit.SECONDS)))
                .subscribe(System.out::println);
        SystemClock.sleep(10000);
    }

    @Test
    public void retryWhenWithFault2() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10)
                .map(i -> 10 / (5 - i))
                .retryWhen(errors -> errors.flatMap(e -> Observable.timer(1, TimeUnit.SECONDS)))
                .subscribe(System.out::println);
        SystemClock.sleep(10000);
    }

    private Function<Observable<Throwable>, Observable<?>> backOffDelay(int retry, int delayMilli) {
        return attempts -> attempts.zipWith(Observable.range(1, retry), (n, i) -> i)
                .flatMap(i -> Observable.timer(i * delayMilli, TimeUnit.MILLISECONDS));
    }

    private Function<? super Observable<Throwable>, ? extends Observable<?>> backOffDelay2(int retry, int delayMilli) {
        return errors -> errors.zipWith(Observable.range(1, retry), (e, attempt) -> {
            if (attempt == retry) {
                return Observable.error(e);
            } else {
                return Observable.timer(attempt * delayMilli, TimeUnit.MILLISECONDS);
            }
        }).flatMap(x -> x);
    }

}

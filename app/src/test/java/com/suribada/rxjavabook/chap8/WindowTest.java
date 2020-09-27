package com.suribada.rxjavabook.chap8;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava3.math.MathObservable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WindowTest {

    /**
     * 비교를 위한 테스트
     */
    @Test
    public void buffer() {
        getSales().buffer(4).subscribe(System.out::println);
    }

    @Test
    public void window() {
        Observable.range(1, 11).window(4) // (1)
                .flatMapSingle(obs -> obs.reduce(0, (total, next) -> total + next)) // (2)
                .subscribe(System.out::println);
    }

    @Test
    public void window2() {
        getSales().map(sale -> sale.getPrice()).window(4) // (1)
                .flatMapSingle(price -> price.reduce(0, (total, next) -> total + next)) // (2)
                .subscribe(System.out::println);
    }

    /**
     * Caused by: java.lang.IllegalStateException: Only a single observer allowed.
     * https://stackoverflow.com/questions/30491785/rxjava-java-lang-illegalstateexception-only-one-subscriber-allowed
     * https://stackoverflow.com/questions/42181065/could-someone-understand-the-java-lang-illegalstateexception-only-one-observer
     */
    @Test
    public void window_autoConnect() {
        Observable<Observable<Integer>> priceObservable = getSales()
                .map(sale -> sale.getPrice()).window(4) // (1)
                .publish().autoConnect(3); // (2)
        System.out.println("구간 합계");
        priceObservable.flatMapSingle(price -> // (3) 시작
                price.reduce(0, (total, next) -> total + next))
                .subscribe(System.out::println, System.err::println); // (3) 끝
        System.out.println("구간 최다판매");
        priceObservable.flatMapSingle(price -> // (4) 시작
                price.reduce(0, (max, next) -> (next > max) ? next : max))
                .subscribe(System.out::println, System.err::println); // (4) 끝
        System.out.println("구간 최소판매");
        priceObservable.flatMapSingle(price -> // (5) 시작
                price.reduce(Integer.MAX_VALUE, (min, next) -> (next < min) ? next : min))
                .subscribe(System.out::println, System.err::println); // (5) 끝
    }

    /**
     * 위와 동일한 문제..
     */
    @Test
    public void window_withMathObservable() {
        Observable<Observable<Integer>> priceObservable = getSales().map(sale -> sale.getPrice()).window(4)
                .publish().autoConnect(3);
        System.out.println("구간 합계");
        priceObservable.flatMap(price -> MathObservable.sumInt(price))
                .subscribe(System.out::println);
        System.out.println("구간 최다판매");
        priceObservable.flatMap(price -> MathObservable.max(price))
                .subscribe(System.out::println);
        System.out.println("구간 최소판매");
        priceObservable.flatMap(price -> MathObservable.min(price))
                .subscribe(System.out::println);
    }

    /**
     * 구간마다 sum, max, min이 순차적으로 나온다.
     */
    @Test
    public void window_autoConnect2() {
        Observable<Integer> priceObservable = getSales() // (1) 시작
                .map(sale -> sale.getPrice())
                .publish().autoConnect(3); // (1) 끝
        priceObservable.window(4).flatMapSingle(price -> // (2) 시작
                price.reduce(0, (total, next) -> total + next))
                .subscribe(sum -> System.out.println("sum=" + sum)); // (2) 끝
        priceObservable.window(4).flatMapSingle(price -> // (3) 시작
                price.reduce(0, (max, next) -> (next > max) ? next : max))
                .subscribe(max -> System.out.println("max=" + max)); // (3) 끝
        priceObservable.window(4).flatMapSingle(price -> // (4) 시작
                price.reduce(Integer.MAX_VALUE, (min, next) -> (next < min) ? next : min))
                .subscribe(min -> System.out.println("min=" + min)); // (4) 끝
    }

    private Observable<Sale> getSales() {
        return Observable.fromArray(Sale.create(1000),
                Sale.create(1500),
                Sale.create(2500),
                Sale.create(2000),
                Sale.create(3000),
                Sale.create(3500),
                Sale.create(4500),
                Sale.create(4000),
                Sale.create(5000),
                Sale.create(6500),
                Sale.create(6000),
                Sale.create(5500),
                Sale.create(7000),
                Sale.create(7500),
                Sale.create(8000));
    }

    @Test
    public void window_timed() {
        Observable.interval(0, 10,  TimeUnit.MILLISECONDS) // (1)
                .map(v -> v * 10) // (2)
                .window(100, 50, TimeUnit.MILLISECONDS) // (3)
                .flatMapSingle(obs -> obs.reduce(0L, (total, next) -> total + next)) // (4)
                .subscribe(System.out::println);
        SystemClock.sleep(5000);
    }

    @Test
    public void window_indicator() {
        Observable.interval(1, TimeUnit.SECONDS)
                .take(20)
                .window(Observable.interval(5, TimeUnit.SECONDS), // (1)
                        opening -> Observable.timer(opening + 2, TimeUnit.SECONDS, Schedulers.single())) // (2)
                .flatMapSingle(obs -> obs.reduce(0L, (total, next) -> total + next)) // (4)
                .subscribe(buffer -> System.out.println(Thread.currentThread().getName() + ": " + buffer));
        SystemClock.sleep(20000);
    }
}

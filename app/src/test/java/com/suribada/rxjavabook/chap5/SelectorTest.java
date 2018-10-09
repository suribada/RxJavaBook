package com.suribada.rxjavabook.chap5;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;

/**
 * Created by lia on 2018-10-06.
 */

public class SelectorTest {

    @Test
    public void rangeTest() {
        Observable<Integer> observable = Observable.range(1, 3);
        observable.concatWith(observable).subscribe(System.out::println);
    }

    @Test
    public void basicFunction() throws InterruptedException {
        Observable<Long> observable = Observable.interval(1000, TimeUnit.MILLISECONDS);
        observable.take(5).concatWith(observable.skip(3))
                .doOnNext(value -> System.out.println(System.currentTimeMillis()))
                .subscribe(System.out::println);
        Thread.sleep(15000);
    }

    @Test
    public void basicFunction2() throws InterruptedException {
        Observable<Long> observable1 = Observable.interval(1000, TimeUnit.MILLISECONDS).take(5);
        Observable<Long> observable2 = Observable.interval(1000, TimeUnit.MILLISECONDS).skip(8);
        observable1.concatWith(observable2)
                .doOnNext(value -> System.out.println(System.currentTimeMillis()))
                .subscribe(System.out::println);
        Thread.sleep(15000);
    }

    @Test
    public void basicFunction3() throws InterruptedException {
        Observable<Long> observable = Observable.interval(1000, TimeUnit.MILLISECONDS);
        observable.take(5).mergeWith(observable.skip(8))
                .doOnNext(value -> System.out.println(System.currentTimeMillis()))
                .subscribe(System.out::println);
        Thread.sleep(15000);
    }

    @Test
    public void publishWithSelector() throws InterruptedException {
        Observable<Long> observable = Observable.interval(1000, TimeUnit.MILLISECONDS)
                .publish(obs -> obs.take(5).concatWith(obs.skip(3)));
        observable
                .doOnNext(value -> System.out.println(System.currentTimeMillis()))
                .subscribe(value -> System.out.println("observer1=" + value));
        Thread.sleep(10000);
        observable.subscribe(value -> System.out.println("observer2=" + value));
        Thread.sleep(10000);
    }

    @Test
    public void publishWithSelector2() throws InterruptedException {
        Observable<Long> observable = Observable.interval(1000, TimeUnit.MILLISECONDS)
                .publish(obs -> obs.take(15).concatWith(obs.takeWhile(x -> x < 7)));
        observable
                .doOnNext(value -> System.out.println(System.currentTimeMillis()))
                .subscribe(value -> System.out.println("observer1=" + value));
        Thread.sleep(10000);
        observable.subscribe(value -> System.out.println("observer2=" + value));
        Thread.sleep(10000);
    }

    @Test
    public void testTakeUntilWithPublishedStreamUsingSelector() throws InterruptedException {
        Observable<Long> observable = Observable.interval(1000, TimeUnit.MILLISECONDS);
        observable.publish(obs -> obs.take(7).concatWith(obs.takeUntil(obs.skipWhile(i -> i <= 3))))
                .subscribe(System.out::println);
        Thread.sleep(10000);
    }

    @Test
    public void replayWithSelector() throws InterruptedException {
        Observable<Long> observable = Observable.interval(1000, TimeUnit.MILLISECONDS)
                .replay(obs -> obs.take(5).concatWith(obs.skip(3)));
        observable
                .doOnNext(value -> System.out.println("observer1=" + System.currentTimeMillis()))
                .subscribe(value -> System.out.println("observer1=" + value));
        Thread.sleep(10000);
    }

    @Test
    public void replayWithSelector2() throws InterruptedException {
        Observable<Long> observable = Observable.interval(1000, TimeUnit.MILLISECONDS)
                .replay(obs -> obs.take(5).concatWith(obs.skip(8)));
        observable
                .doOnNext(value -> System.out.println("observer1=" + System.currentTimeMillis()))
                .subscribe(value -> System.out.println("observer1=" + value));
        Thread.sleep(15000);
    }

    @Test
    public void replayWithSelector_repeat() throws InterruptedException {
        Observable<Long> observable = Observable.interval(1000, TimeUnit.MILLISECONDS);
        observable.replay(obs -> obs.take(3).repeat(3), 2)
                .doOnNext(value -> System.out.println("observer1=" + System.currentTimeMillis()))
                .subscribe(value -> System.out.println("observer1=" + value));
        Thread.sleep(20000);
    }
}

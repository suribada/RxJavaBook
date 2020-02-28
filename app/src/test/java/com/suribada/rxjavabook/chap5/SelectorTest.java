package com.suribada.rxjavabook.chap5;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.observables.ConnectableObservable;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

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
        Observable<Long> observable = Observable.interval(1000, TimeUnit.MILLISECONDS).doOnNext(value -> System.out.println("source value=" + value))
                .publish(obs -> obs.take(5).concatWith(obs.skip(3)));
        observable
                .doOnNext(value -> System.out.println(System.currentTimeMillis()))
                .subscribe(value -> System.out.println("observer1=" + value));
        Thread.sleep(10000);
        observable.subscribe(value -> System.out.println("observer2=" + value));
        Thread.sleep(10000);
    }

    @Test
    public void publishWithSelector_finite() throws InterruptedException {
        Observable<Long> observable = Observable.interval(1000, TimeUnit.MILLISECONDS).doOnNext(value -> System.out.println("source value=" + value))
                .publish(obs -> obs.take(5).concatWith(obs.skip(3).take(2)));
        observable
                .doOnNext(value -> System.out.println(System.currentTimeMillis()))
                .subscribe(value -> System.out.println("observer1=" + value));
        Thread.sleep(10000);
    }

    @Test
    public void publishWithSelector_once_in_a_while() throws InterruptedException {
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS)
                .publish(obs -> obs.skip(1).take(1).concatWith(obs.skip(2).take(2)).concatWith(obs.skip(3).take(3)));
        observable
                .doOnNext(value -> System.out.println(System.currentTimeMillis()))
                .subscribe(value -> System.out.println("observer1=" + value));
        Thread.sleep(15000);
    }

    /** 내부에서 PublishSubject를 사용하므로 이 내용은 아니다 */
    @Test
    public void publishWithSelector_internal_wrongAnswer() throws InterruptedException {
        ConnectableObservable<Long> connectableObservable = Observable.interval(1000, TimeUnit.MILLISECONDS).publish();
        connectableObservable.take(5)
                .subscribe(value -> System.out.println("observer1=" + value));
        SystemClock.sleep(8000);
        connectableObservable.skip(3).subscribe(value -> System.out.println("observer2=" + value));
        connectableObservable.connect();
        Thread.sleep(10000);
    }

    @Test
    public void publishWithSelector_internal() throws InterruptedException {
        PublishSubject<Long> publishSubject = PublishSubject.create(); // (1)
        Observable.interval(1, TimeUnit.SECONDS).subscribe(publishSubject); // (2)
        publishSubject.take(5).concatWith(publishSubject.skip(3).take(2))
                .subscribe(System.out::println);
        Thread.sleep(15000);
    }

    @Test
    public void publishWithSelector2() throws InterruptedException {
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS)
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
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS)
            .publish(obs -> obs.take(5).concatWith(obs.takeUntil(obs.skipWhile(i -> i <= 7))));
        observable.subscribe(System.out::println);
        Thread.sleep(10000);
    }

    @Test
    public void replayWithSelector() throws InterruptedException {
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS)
                .replay(obs -> obs.take(5).concatWith(obs.skip(3)));
        observable
                .doOnNext(value -> System.out.println("observer1=" + System.currentTimeMillis()))
                .subscribe(value -> System.out.println("observer1=" + value));
        Thread.sleep(10000);
    }

    @Test
    public void replayWithSelector_internal() {
        ConnectableObservable<Long> connectableObservable
                = Observable.interval(1, TimeUnit.SECONDS).replay(); // (1)
        connectableObservable.take(5).concatWith(connectableObservable.skip(3)) // (2)
                .subscribe(System.out::println);
        connectableObservable.connect(); // (3)
        SystemClock.sleep(10000);
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

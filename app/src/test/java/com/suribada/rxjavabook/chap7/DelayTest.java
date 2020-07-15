package com.suribada.rxjavabook.chap7;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class DelayTest {

    @Test
    public void testDelaySubscription() {
        PublishSubject<Integer> publishSubject = PublishSubject.create();
        Observable.just(1, 2, 3).delaySubscription(publishSubject) // (1)
                .subscribe(System.out::println);
        publishSubject.onNext(0); // (2)
    }

    @Test
    public void testDelaySubscriptionWithOnComplete() {
        PublishSubject<Integer> publishSubject = PublishSubject.create();
        Observable.just(1, 2, 3).delaySubscription(publishSubject)
                .subscribe(System.out::println);
        publishSubject.onComplete(); // (1)
    }

    @Test
    public void testDelaySubscriptionWithOnError() {
        PublishSubject<Integer> publishSubject = PublishSubject.create();
        Observable.just(1, 2, 3).delaySubscription(publishSubject)
                .subscribe(System.out::println, System.err::println);
        publishSubject.onError(new IndexOutOfBoundsException());
    }

    @Test
    public void testDelaySubscription_flatMap() {
        PublishSubject<Integer> publishSubject = PublishSubject.create();
        publishSubject.flatMap(ignored -> Observable.just(1, 2, 3))
                .subscribe(System.out::println, System.err::println);
        publishSubject.onNext(0);
    }

    @Test
    public void testDelaySubscription_flatMap2() {
        PublishSubject<Integer> publishSubject = PublishSubject.create();
        publishSubject.flatMap(value -> Observable.just(1, 2, 3))
                .subscribe(System.out::println, System.err::println);
        publishSubject.onComplete();
    }

    @Test
    public void testInsertAndRetrieve() {
        retrieve().delaySubscription(update())
                .subscribeOn(Schedulers.io())
                .subscribe(System.out::println);
        SystemClock.sleep(2000);
    }

    private Observable<String> update() {
        return Observable.<String>empty().doOnComplete(() -> System.out.println("onComplete"));
    }

    private Observable<String> retrieve() {
        return Observable.create(emitter -> {
                emitter.onNext("A");
                emitter.onNext("B");
                emitter.onComplete();
            });
    }

    @Test
    public void testInsertAndRetrieve_alternative() {
        Observable.concat(update(), retrieve())
                .subscribeOn(Schedulers.io())
                .subscribe(System.out::println);
        SystemClock.sleep(2000);
    }

    @Test
    public void testDelay() {
        Observable.just("Sonata", "Lay", "Equinox", "K5")
                .delay(car -> Observable.timer(car.length(), TimeUnit.SECONDS)) // (1)
                .subscribe(System.out::println);
        SystemClock.sleep(20000);
    }
}

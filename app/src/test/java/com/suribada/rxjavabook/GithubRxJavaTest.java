package com.suribada.rxjavabook;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.observables.ConnectableObservable;
import io.reactivex.rxjava3.observers.TestObserver;

import static org.junit.Assert.assertEquals;

/**
 * Created by lia on 2018-10-06.
 */

public class GithubRxJavaTest {

    @Test
    public void testTakeUntilWithPublishedStream() {
        Observable<Integer> xs = Observable.range(0, Flowable.bufferSize() * 2);
        TestObserver<Integer> to = new TestObserver<Integer>();
        ConnectableObservable<Integer> xsp = xs.publish();
        xsp.takeUntil(xsp.skipWhile(new Predicate<Integer>() {

            @Override
            public boolean test(Integer i) {
                return i <= 3;
            }

        })).subscribe(to);
        xsp.connect();
        System.out.println(to.values());
    }

    @Test
    public void testTakeUntilWithPublishedStreamUsingSelector() {
        final AtomicInteger emitted = new AtomicInteger();
        Observable<Integer> xs = Observable.range(0, Flowable.bufferSize() * 2).doOnNext(new Consumer<Integer>() {

            @Override
            public void accept(Integer t1) {
                emitted.incrementAndGet();
            }

        });
        TestObserver<Integer> to = new TestObserver<Integer>();
        xs.publish(new Function<Observable<Integer>, Observable<Integer>>() {

            @Override
            public Observable<Integer> apply(Observable<Integer> xs) {
                return xs.takeUntil(xs.skipWhile(new Predicate<Integer>() {

                    @Override
                    public boolean test(Integer i) {
                        return i <= 3;
                    }

                }));
            }

        }).subscribe(to);
        //RxJava3에서 제거
        // https://github.com/ReactiveX/RxJava/issues/6153
        //to.awaitTerminalEvent();
        to.assertNoErrors();
        to.assertValues(0, 1, 2, 3);
        assertEquals(5, emitted.get());
        System.out.println(to.values());
    }

}

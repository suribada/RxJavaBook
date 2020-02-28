package com.suribada.rxjavabook.chap6;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.internal.util.Pow2;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ObserveOnTest {

    @Test
    public void testObserveOn_default() {
        Observable.fromArray(1, 2, 0, 4, 5)
                .subscribeOn(Schedulers.computation())
                .map(value -> 10 / value)
                .observeOn(Schedulers.io())
                .subscribe(result -> System.out.println(Thread.currentThread().getName() + ": " + result),
                        e -> e.printStackTrace());

        SystemClock.sleep(1000);
    }

    @Test
    public void testObserveOn_longArray() {
        Observable.fromArray(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 0, 4, 5)
                .subscribeOn(Schedulers.computation())
                .map(value -> 10 / value)
                .observeOn(Schedulers.io())
                .subscribe(result -> System.out.println(Thread.currentThread().getName() + ": " + result),
                        e -> e.printStackTrace());

        SystemClock.sleep(3000);
    }

    @Test
    public void testObserveOn_withErrorDelay() {
        Observable.just(1, 2, 0, 4, 5)
                .subscribeOn(Schedulers.computation())
                .map(value -> 10 / value)
                .observeOn(Schedulers.io(), true)
                .subscribe(result -> System.out.println(Thread.currentThread().getName() + ": " + result),
                        e -> e.printStackTrace());

        SystemClock.sleep(1000);
    }

    @Test
    public void testObserveOn_longArray_withErorDelay() {
        Observable.fromArray(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 0, 4, 5)
                .subscribeOn(Schedulers.computation())
                .map(value -> 10 / value)
                .observeOn(Schedulers.io(), true)
                .subscribe(result -> System.out.println(Thread.currentThread().getName() + ": " + result),
                        e -> e.printStackTrace());

        SystemClock.sleep(3000);
    }

    @Test
    public void testObserveOn_default2() {
        Observable<Integer> obs = Observable.create(emitter -> {
            emitter.onNext(1);
            emitter.onNext(2);
            emitter.onNext(3);
            emitter.onError(new RuntimeException());
        });
        obs.subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io()) // (1)
                .subscribe(result -> System.out.println(Thread.currentThread().getName() + ": " + result),
                        e -> e.printStackTrace());

        SystemClock.sleep(1000);
    }

    @Test
    public void testObserveOn_withDelayError() {
        Observable<Integer> obs = Observable.create(emitter -> {
            emitter.onNext(1);
            emitter.onNext(2);
            emitter.onNext(3);
            emitter.onError(new RuntimeException());
        });
        obs.subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io(), true) // (1)
                .subscribe(result -> System.out.println(Thread.currentThread().getName() + ": " + result),
                        e -> e.printStackTrace());

        SystemClock.sleep(1000);
    }

    @Test
    public void pow2() {
        System.out.println(Pow2.roundToPowerOfTwo(18));
    }

}

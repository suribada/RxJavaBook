package com.suribada.rxjavabook.chap10;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BackPressureTest {

    /**
     * Caused by: io.reactivex.rxjava3.exceptions.MissingBackpressureException: Queue is full?!
     * 	at io.reactivex.rxjava3.internal.operators.flowable.FlowableObserveOn$BaseObserveOnSubscriber.onNext(FlowableObserveOn.java:114)
     * 	at io.reactivex.rxjava3.internal.operators.flowable.FlowableCreate$MissingEmitter.onNext(FlowableCreate.java:374)
     */
    @Test
    public void create_missing() {
        Flowable.<Integer>create(emitter -> {
                for (int i = 0; i < 1_000_000; i++) {
                    if (emitter.isCancelled()) {
                        return;
                    }
                    emitter.onNext(i);
                }
            }, BackpressureStrategy.MISSING)
            .observeOn(Schedulers.io())
            .subscribe(v -> {
                SystemClock.sleep(5);
                System.out.println(v);
            }, Throwable::printStackTrace);
        SystemClock.sleep(5000);
    }

    /**
     * Caused by: io.reactivex.rxjava3.exceptions.MissingBackpressureException: create: could not emit value due to lack of requests
     * 	at io.reactivex.rxjava3.internal.operators.flowable.FlowableCreate$ErrorAsyncEmitter.onOverflow(FlowableCreate.java:445)
     * 	at io.reactivex.rxjava3.internal.operators.flowable.FlowableCreate$NoOverflowBaseAsyncEmitter.onNext(FlowableCreate.java:413)
     */
    @Test
    public void create_error() {
        Flowable.<Integer>create(emitter -> {
                for (int i = 0; i < 1_000_000; i++) {
                    if (emitter.isCancelled()) {
                        return;
                    }
                    emitter.onNext(i);
                }
            }, BackpressureStrategy.ERROR)
            .observeOn(Schedulers.io())
            .subscribe(v -> {
                SystemClock.sleep(5);
                System.out.println(v);
            }, Throwable::printStackTrace);
        SystemClock.sleep(5000);
    }

    @Test
    public void create_buffer() {
        Flowable.<Integer>create(emitter -> {
                for (int i = 0; i < 1_000_000; i++) {
                    if (emitter.isCancelled()) {
                        return;
                    }
                    emitter.onNext(i);
                }
            }, BackpressureStrategy.BUFFER)
            .observeOn(Schedulers.io())
            .subscribe(v -> {
                SystemClock.sleep(5);
                System.out.println(v);
            });
        SystemClock.sleep(5000);
    }

    @Test
    public void create_drop() {
        Flowable.<Integer>create(emitter -> {
                for (int i = 0; i < 1_000_000; i++) {
                    if (emitter.isCancelled()) {
                        return;
                    }
                    SystemClock.sleep(3);
                    emitter.onNext(i);
                }
            }, BackpressureStrategy.DROP)
            .observeOn(Schedulers.io())
            .subscribe(v -> {
                SystemClock.sleep(5);
                System.out.println(v);
            });
        SystemClock.sleep(5000);
    }

    @Test
    public void create_latest() {
        Flowable.<Integer>create(emitter -> {
                for (int i = 0; i < 1_000_000; i++) {
                    if (emitter.isCancelled()) {
                        return;
                    }
                    SystemClock.sleep(3);
                    emitter.onNext(i);
                }
            }, BackpressureStrategy.LATEST)
            .observeOn(Schedulers.io())
            .subscribe(v -> {
                SystemClock.sleep(5);
                System.out.println(v);
            });
        SystemClock.sleep(20000);
    }

    /*
    https://itsallbinary.com/rxjava-basics-with-example-backpressure-drop-error-latest-missing-good-for-beginners/
     */
    @Test
    public void lastest() throws InterruptedException {
        Flowable<Object> flowableAsyncBackp = Flowable.create(emitter -> {

            // Publish 1000 numbers
            for (int i = 0; i < 1000; i++) {
                System.out.println(Thread.currentThread().getName() + " | Publishing = " + i);
                emitter.onNext(i);
                Thread.sleep(10);
            }
            // When all values or emitted, call complete.
            emitter.onComplete();

        }, BackpressureStrategy.LATEST);

        flowableAsyncBackp.subscribeOn(Schedulers.newThread()).observeOn(Schedulers.single()).subscribe(i -> {
            // Process received value.
            System.out.println(Thread.currentThread().getName() + " | Received = " + i);
            // 100 mills delay to simulate slow subscriber
            Thread.sleep(100);
        }, e -> {
            // Process error
            System.err.println(Thread.currentThread().getName() + " | Error = " + e.getMessage());
        });
        /*
         * Notice above -
         *
         * BackpressureStrategy.LATEST - Overwrites values if subscriber can't keep up.
         *
         * subscribeOn & observeOn - Put subscriber & publishers on different threads.
         */

        // Since publisher & subscriber run on different thread than main thread, keep
        // main thread active for 100 seconds.
        Thread.sleep(100000);
    }

    /**
     * fromArray, fromIterable, range, rangeLong은 slowPath(), pastPath()가 있다.
     */
    @Test
    public void noProblem() {
        Flowable.range(1, 1_000_000)
                .observeOn(Schedulers.io())
                .subscribe(v -> {
                    SystemClock.sleep(5);
                    System.out.println(v);
                });
        SystemClock.sleep(5000);
    }

    /**
     * interval, intervalRange는
     * request() 메서드를 처리하지 않는다.
     */
    @Test
    public void problem() {
        Flowable.interval(1, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .subscribe(v -> {
                    SystemClock.sleep(5);
                    System.out.println(v);
                });
        SystemClock.sleep(5000);
    }

    /**
     * 중간에 끼어서 업스트림에 request(Long.MAX_VALUE)를 하고 있다.
     */
    @Test
    public void problem_onBackPressureBuffer() {
        Flowable.interval(1, TimeUnit.MILLISECONDS)
                .onBackpressureBuffer()
                .observeOn(Schedulers.io())
                .subscribe(v -> {
                    SystemClock.sleep(5);
                    System.out.println(v);
                });
        SystemClock.sleep(5000);
    }

    /**
     *  여기서도 중간에 끼어서 업스트림에 request(Long.MAX_VALUE)를 하고 있다.
     *  onNext 받는 곳에서 에러를 내지 않고 조용히 넘어간다.
     */
    @Test
    public void problem_onBackPressureDrop() {
        Flowable.interval(1, TimeUnit.MILLISECONDS)
                .onBackpressureDrop()
                .observeOn(Schedulers.io())
                .subscribe(v -> {
                    SystemClock.sleep(5);
                    System.out.println(v);
                });
        SystemClock.sleep(5000);
    }

    @Test
    public void problem_onBackPressureLatest() {
        Flowable.interval(1, TimeUnit.MILLISECONDS)
                .onBackpressureLatest()
                .observeOn(Schedulers.io())
                .subscribe(v -> {
                    SystemClock.sleep(5);
                    System.out.println(v);
                });
        SystemClock.sleep(5000);
    }
}

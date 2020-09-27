package com.suribada.rxjavabook.chap10;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.rxjava3.core.BackpressureOverflowStrategy;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BackPressureTest {

    /**
     * Caused by: io.reactivex.rxjava3.exceptions.MissingBackpressureException: Queue is full?!
     * at io.reactivex.rxjava3.internal.operators.flowable.FlowableObserveOn$BaseObserveOnSubscriber.onNext(FlowableObserveOn.java:114)
     * at io.reactivex.rxjava3.internal.operators.flowable.FlowableCreate$MissingEmitter.onNext(FlowableCreate.java:374)
     */
    @Test
    public void create_missing() {
        Flowable.<Integer>create(emitter -> {
            for (int i = 0; i < 1_000_000; i++) { // (1) 시작
                if (emitter.isCancelled()) {
                    return;
                }
                emitter.onNext(i);
            } // (1) 끝
        }, BackpressureStrategy.MISSING) // (2)
                .onBackpressureBuffer()
                .observeOn(Schedulers.io()) // (3)
                .subscribe(v -> {
                    SystemClock.sleep(5); // (4) 시작
                    System.out.println("value= " + v); // (4) 끝
                }, Throwable::printStackTrace);
        SystemClock.sleep(5000);
    }

    /**
     * Caused by: io.reactivex.rxjava3.exceptions.MissingBackpressureException: create: could not emit value due to lack of requests
     * at io.reactivex.rxjava3.internal.operators.flowable.FlowableCreate$ErrorAsyncEmitter.onOverflow(FlowableCreate.java:445)
     * at io.reactivex.rxjava3.internal.operators.flowable.FlowableCreate$NoOverflowBaseAsyncEmitter.onNext(FlowableCreate.java:413)
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
                    System.out.println("value=" + v);
                }, Throwable::printStackTrace);
        SystemClock.sleep(5000);
    }

    @Test
    public void create_error_buffer() {
        Flowable.<Integer>create(emitter -> {
            for (int i = 0; i < 1_000_000; i++) {
                if (emitter.isCancelled()) {
                    return;
                }
                emitter.onNext(i);
            }
        }, BackpressureStrategy.ERROR)
                .onBackpressureBuffer()
                .observeOn(Schedulers.io())
                .subscribe(v -> {
                    SystemClock.sleep(5);
                    System.out.println("value=" + v);
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

    @Test
    public void generate() {
        Flowable.<Integer, Integer>generate(() -> 0, (num, emitter) -> {
            if (num >= 1000) {
                emitter.onComplete();
                return num;
            }
            emitter.onNext(num);
            return ++num;
        }).doOnRequest(request -> System.out.println("requested=" + request))
                .observeOn(Schedulers.io())
                .subscribe(v -> {
                    SystemClock.sleep(5);
                    System.out.println(v);
                });
        SystemClock.sleep(10000);
    }

    @Test
    public void generate_Integer() {
        Flowable.<Integer, AtomicInteger>generate(() -> new AtomicInteger(0), // (1)
                (num, emitter) -> {
                    int value = num.getAndIncrement(); // (2)
                    emitter.onNext(value); // (3)
                    if (num.intValue() == 1_000_000) { // (4)
                        emitter.onComplete();
                    } // (4) 끝
                }).doOnRequest(request -> System.out.println("requested=" + request)) // (5)
                .observeOn(Schedulers.io())
                .subscribe(v -> {
                    SystemClock.sleep(5);
                    System.out.println(v);
                });
        SystemClock.sleep(10000);
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
     * PublishProcessor, interval, intervalRange는
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
        Flowable.interval(1, TimeUnit.MILLISECONDS) // (1)
                .onBackpressureBuffer(64, false, false, // (2)
                        () -> System.out.println(System.currentTimeMillis() + ":overflowed")) // (3)
                .observeOn(Schedulers.io())
                .subscribe(v -> {
                    SystemClock.sleep(5); // (4) 시작
                    System.out.println(System.currentTimeMillis() + ":" + v); // (4) 끝
                }, Throwable::printStackTrace);
        SystemClock.sleep(5000);
    }

    @Test
    public void problem_onBackPressureBuffer_nonFusion() {
        Flowable.interval(1, TimeUnit.MILLISECONDS) // (1)
                .onBackpressureBuffer(64, false, false, // (2)
                        () -> System.out.println(System.currentTimeMillis() + ":overflowed")) // (3)
                .hide()
                .observeOn(Schedulers.io())
                .subscribe(v -> {
                    SystemClock.sleep(5); // (4) 시작
                    System.out.println(System.currentTimeMillis() + ":" + v); // (4) 끝
                }, Throwable::printStackTrace);
        SystemClock.sleep(5000);
    }

    @Test
    public void problem_onBackPressureBuffer_withSubscriber() {
        Flowable.interval(1, TimeUnit.MILLISECONDS) // (1)
                .onBackpressureBuffer(64, false, false, // (2)
                        () -> System.out.println("overflowed")) // (3)
                .subscribe(new Subscriber<Long>() {

                    private Subscription subscription;

                    @Override
                    public void onSubscribe(Subscription s) {
                        this.subscription = s;
                        s.request(128);
                    }

                    @Override
                    public void onNext(Long next) {
                        SystemClock.sleep(300); // (4) 시작
                        System.out.println(next);
                        if (next % 128 == 0) {
                            subscription.request(128);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
        SystemClock.sleep(5000);
    }

    @Test
    public void problem_onBackPressureBuffer_withSubscriber2() {
        Flowable.interval(1, TimeUnit.MILLISECONDS) // (1)
                .map(v -> 10 / (5 - v))
                .onBackpressureBuffer(64, false, false, // (2)
                        () -> System.out.println("overflowed")) // (3)
                .subscribe(new Subscriber<Long>() {

                    private Subscription subscription;

                    @Override
                    public void onSubscribe(Subscription s) {
                        this.subscription = s;
                        s.request(128);
                    }

                    @Override
                    public void onNext(Long next) {
                        SystemClock.sleep(5); // (4) 시작
                        System.out.println(next);
                        if (next % 128 == 0) {
                            subscription.request(128);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
        SystemClock.sleep(5000);
    }


    @Test
    public void problem_onBackPressureBuffer_delayError() {
        Flowable.interval(1, TimeUnit.MILLISECONDS) // (1)
                .map(v -> v / (5 - v))
                .onBackpressureBuffer(64, true, false, // (2)
                        () -> System.out.println("overflowed")) // (3)
                .observeOn(Schedulers.io())
                .subscribe(v -> {
                    SystemClock.sleep(5); // (4) 시작
                    System.out.println(v); // (4) 끝
                }, Throwable::printStackTrace);
        SystemClock.sleep(5000);
    }

    @Test
    public void problem_onBackPressureBuffer_dropOldest() {
        Flowable.interval(1, TimeUnit.MILLISECONDS)
                .onBackpressureBuffer(64, () -> System.out.println(System.currentTimeMillis() + ":overflowed"), // (1)
                        BackpressureOverflowStrategy.DROP_OLDEST) // (2)
                .observeOn(Schedulers.io())
                .subscribe(v -> {
                    SystemClock.sleep(5);
                    System.out.println(System.currentTimeMillis() + ": " + v);
                }, Throwable::printStackTrace);
        SystemClock.sleep(5000);
    }

    /**
     * 여기서도 중간에 끼어서 업스트림에 request(Long.MAX_VALUE)를 하고 있다.
     * onNext 받는 곳에서 에러를 내지 않고 조용히 넘어간다.
     */
    @Test
    public void problem_onBackPressureDrop() {
        Flowable.interval(1, TimeUnit.MILLISECONDS)
                .onBackpressureDrop(v -> System.out.println("dropped " + v)) // (1)
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
                .onBackpressureLatest() // (1)
                .observeOn(Schedulers.io())
                .subscribe(v -> {
                    SystemClock.sleep(5);
                    System.out.println(v);
                });
        SystemClock.sleep(5000);
    }
}

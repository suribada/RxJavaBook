package com.suribada.rxjavabook.chap5;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.SafeObserver;
import io.reactivex.observers.SerializedObserver;

/**
 * Created by lia on 2018-10-18.
 */

public class ObserverTest {

    @Test
    public void testDisposableObserver() {
        DisposableObserver<Integer> observer = new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer t) {
                System.out.println(10 / t); // (1)
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Done!");
            }
        };
        Observable.just(1, 2, 3, 0, 1, 2)
                .subscribe(observer);
    }

    @Test
    public void testDisposableObserver_withSafeObserver() {
        DisposableObserver<Integer> observer = new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer t) {
                System.out.println(10 / t); // (1)
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Done!");
            }
        };
        Observable.just(1, 2, 3, 0, 1, 2)
                .subscribe(new SafeObserver<Integer>(observer));
    }

    @Test
    public void testDisposableObserver_withErrorAtOnError() {
        DisposableObserver<Integer> observer = new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer t) {
                System.out.println(t); // (1)
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("error occurred");
                throw new ArithmeticException("arithmetic problem");
            }

            @Override
            public void onComplete() {
                System.out.println("Done!");
            }
        };
        Observable.just(1, 2, 3, 0, 1, 2)
                .map(value -> 10 / value)
                .subscribe(observer);
    }

    @Test
    public void testDisposableObserver_withDelivableErrorAtOnError() {
        DisposableObserver<Integer> observer = new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer t) {
                System.out.println(t); // (1)
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("error occurred");
                throw new IllegalStateException("illegal");
            }

            @Override
            public void onComplete() {
                System.out.println("Done!");
            }
        };
        Observable.just(1, 2, 3, 0, 1, 2)
                .map(value -> 10 / value)
                .subscribe(observer);
    }

    @Test
    public void testDisposableObserver_withErrorAtOnError_wrapWithSafe() {
        DisposableObserver<Integer> observer = new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer t) {
                System.out.println(t); // (1)
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("error occurred");
                throw new ArithmeticException("arithmetic problem");
            }

            @Override
            public void onComplete() {
                System.out.println("Done!");
            }
        };
        Observable.just(1, 2, 3, 0, 1, 2)
                .map(value -> 10 / value)
                .subscribe(new SafeObserver<Integer>(observer));
    }

    @Test
    public void testDisposableObserver2() {
        DisposableObserver<Integer> observer = new DisposableObserver<Integer>() {
            @Override
            public void onStart() {
                System.out.println("Start!");
            }

            @Override
            public void onNext(Integer t) {
                System.out.println(t);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Done!");
            }
        };
        Disposable d =
                Observable.just(1, 2, 3, 0, 1, 2)
                        .map(value -> 10 / value)
                        .subscribeWith(observer);
        d.dispose();
    }

    @Test
    public void testDisposableObserver3() {
        DisposableObserver<Integer> observer = new DisposableObserver<Integer>() {
            @Override
            public void onStart() {
                System.out.println("Start!");
            }

            @Override
            public void onNext(Integer t) {
                System.out.println(t);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Done!");
            }
        };
        Disposable d =
                Observable.merge(Observable.just(1),
                        Observable.error(new IllegalStateException()),
                        Observable.just(3))
                    .subscribeWith(observer);
        d.dispose();
    }

    @Test
    public void testDisposableObserver4() {
        DisposableObserver<Integer> observer = new DisposableObserver<Integer>() {
            @Override
            public void onStart() {
                System.out.println("Start!");
            }

            @Override
            public void onNext(Integer t) {
                System.out.println(t);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Done!");
            }
        };
        Disposable d =
                Observable.<Integer>create(emitter -> {
                    emitter.onNext(1);
                    emitter.onError(new IllegalArgumentException("invalid"));
                    emitter.onNext(3);
                })
                .subscribeWith(observer);
        d.dispose();
    }

    @Test
    public void testDisposableObserver5() {
        DisposableObserver<Integer> observer = new DisposableObserver<Integer>() {
            @Override
            public void onStart() {
                System.out.println("Start!");
            }

            @Override
            public void onNext(Integer t) {
                System.out.println(t);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Done!");
            }
        };
        Observable<Integer> obs = Observable.create(emitter -> {
            new Thread(() -> {
                emitter.onNext(1);
                emitter.onComplete(); // (1)
            }).start();
            new Thread(() -> {
                emitter.onNext(2);
                emitter.onNext(4);
                emitter.onNext(6);
                emitter.onComplete(); // (2)
            }).start();
        });
        obs.subscribe(observer);
        SystemClock.sleep(2000);
    }

    @Test
    public void testDisposableObserver6() {
        DisposableObserver<Integer> observer = new DisposableObserver<Integer>() {
            @Override
            public void onStart() {
                System.out.println("Start!");
            }

            @Override
            public void onNext(Integer t) {
                System.out.println(t);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Done!");
            }
        };
        Observable<Integer> obs = Observable.create(emitter -> {
            new Thread(() -> {
                emitter.onNext(1);
                emitter.onComplete(); // (1)
            }).start();
            new Thread(() -> {
                emitter.onNext(2);
                emitter.onNext(4);
                emitter.onNext(6);
                emitter.onComplete(); // (2)
            }).start();
        });
        obs.subscribe(new SafeObserver<>(observer));
        SystemClock.sleep(2000);
    }

    @Test
    public void testSerializedObserver() {
        Observable obs = Observable.create(emitter -> {
            new Thread(() -> {
                emitter.onNext(1);
                emitter.onNext(3);
                emitter.onNext(5);
                emitter.onNext(7);
                emitter.onNext(9);
            }).start();
            new Thread(() -> {
                emitter.onNext(2);
                emitter.onNext(4);
                emitter.onNext(6);
                emitter.onNext(8);
                emitter.onNext(10);
            }).start();
        });
        DisposableObserver<Integer> observer = new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer t) {
                System.out.println(Thread.currentThread().getName() + ":" + t);
            }

            @Override
            public void onError(Throwable e) {
                System.err.println(e);
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };
        //obs.subscribe(observer);
        obs.subscribe(new SerializedObserver(observer));
        SystemClock.sleep(1000);
    }

}

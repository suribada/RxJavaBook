package com.suribada.rxjavabook.chap7;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;

public class BufferTest {

    @Test
    public void testBuffer() {
        Observable.range(1, 10)
                .buffer(3, 2) // (1)
                .subscribe(System.out::println);
        System.out.println("----");
        Observable.range(1, 10)
                .buffer(3, 3)
                .subscribe(System.out::println);
        System.out.println("----");
        Observable.range(1, 10)
                .buffer(3, 5)
                .subscribe(System.out::println);
    }

    @Test
    public void testScheduler() {
        Observable.interval(40, TimeUnit.MILLISECONDS, Schedulers.io())
                .take(8)
                .buffer(100, TimeUnit.MILLISECONDS)
                .subscribe(value -> System.out.println(Thread.currentThread().getName() + ", " + value));
        SystemClock.sleep(3000);
    }

    @Test
    public void testScheduler2() {
        // buffer 기준 미달이기 때문에 무의미
        Observable.range(1, 10)
                .buffer(500, TimeUnit.MILLISECONDS)
                .subscribe(value -> System.out.println(Thread.currentThread().getName() + ", " + value));
        SystemClock.sleep(1000);
    }

    @Test
    public void testBufferOverlapped() {
        ConnectableObservable<Integer> connectableObservable = getSpeedObservable().publish();
        connectableObservable.subscribe(speed -> System.out.println("speed=" + speed));
        connectableObservable.buffer(2, 1) // (1)
                .filter(list -> list.size() > 1) // (2)
                .map(speeds -> speeds.get(1) - speeds.get(0)) // (3)
                .subscribe(diff -> System.out.println("diff=" + diff));
        Disposable disposable = connectableObservable.connect();
    }

    private Observable<Integer> getSpeedObservable() {
        return Observable.create(emitter -> {
            emitter.onNext(70);
            SystemClock.sleep(100);
            emitter.onNext(80);
            SystemClock.sleep(100);
            emitter.onNext(65);
            SystemClock.sleep(100);
            emitter.onNext(90);
            emitter.onComplete();
        });
    }
}

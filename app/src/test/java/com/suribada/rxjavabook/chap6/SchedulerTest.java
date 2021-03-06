package com.suribada.rxjavabook.chap6;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SchedulerTest {

    @Test
    public void testThread() {
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .subscribe(ignored -> print("first"));
        SystemClock.sleep(1000);
    }

    @Test
    public void testThread_computation() {
        Observable.just(0)
                .subscribeOn(Schedulers.computation())
                .subscribe(ignored -> print("first"));
        SystemClock.sleep(1000);
    }

    @Test
    public void testThread2() {
        Schedulers.io().scheduleDirect(() -> print("first"));
    }


    @Test
    public void testDirect() {
        Scheduler scheduler = Schedulers.io();
        scheduler.scheduleDirect(() -> print(Thread.currentThread().getName() + ":first")); // (1)
        scheduler.scheduleDirect(() -> print(Thread.currentThread().getName() + ":second"), 1, TimeUnit.SECONDS); // (2)
        scheduler.schedulePeriodicallyDirect(() -> print(Thread.currentThread().getName() + ":third"), 0, 1, TimeUnit.SECONDS); // (3)
        SystemClock.sleep(5000);
    }

    private void print(String value) {
        //SystemClock.sleep(1000);
        System.out.println(Thread.currentThread().getName() + ": value=" + value);
    }

    @Test
    public void testWorker() {
        Scheduler.Worker worker = Schedulers.io().createWorker();
        worker.schedule(() -> print("first"));
        worker.schedule(() -> print("second"), 1, TimeUnit.SECONDS);
        worker.schedulePeriodically(() -> print("third"), 0, 1, TimeUnit.SECONDS);
        SystemClock.sleep(5000);
    }

    @Test
    public void testDisposable() {
        Scheduler.Worker worker = Schedulers.io().createWorker();
        Disposable disposable1 = worker.schedule(() -> print("first")); // (1) 시작
        Disposable disposable2 = worker.schedule(() -> print("second"), 1, TimeUnit.SECONDS);
        Disposable disposable3 =  worker.schedulePeriodically(() -> print("third"), 0, 1, TimeUnit.SECONDS); // (1) 끝
        SystemClock.sleep(500);
        disposable1.dispose(); // (2) 시작
        disposable2.dispose();
        disposable3.dispose(); // (2) 끝
    }
}

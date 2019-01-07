package com.suribada.rxjavabook.chap6;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ExecutorSchedulerTest {

    /**
     * 스레드 1개만 사용
     */
    @Test
    public void basic() {
        Observable.range(1, 100)
                .subscribeOn(Schedulers.from(Executors.newScheduledThreadPool(4)))
                .subscribe(value -> System.out.println(Thread.currentThread().getName() + ": value=" + value));
        SystemClock.sleep(5000);
    }

    /**
     * 스레드 1개만 사용
     */
    @Test
    public void observeExecutorScheduler() {
        Observable.range(1, 100)
                .observeOn(Schedulers.from(Executors.newScheduledThreadPool(4)))
                .subscribe(value -> System.out.println(Thread.currentThread().getName() + ": value=" + value));
        SystemClock.sleep(5000);
    }

    @Test
    public void interval_original() {
        Observable.interval(50, TimeUnit.MILLISECONDS)
                .subscribe(value -> System.out.println(Thread.currentThread().getName() + ": value=" + value));
        SystemClock.sleep(5000);
    }

    @Test
    public void interval() {
        Observable.interval(50, TimeUnit.MILLISECONDS,
                Schedulers.from(Executors.newScheduledThreadPool(4)))
                .subscribe(value -> System.out.println(Thread.currentThread().getName() + ": value=" + value));
        SystemClock.sleep(5000);
    }
}

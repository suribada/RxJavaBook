package com.suribada.rxjavabook.chap6;

import android.util.Log;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.internal.schedulers.ScheduledDirectPeriodicTask;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThreadTest {

    @Test
    public void testDefault() {
        for (int i = 0; i < 5; i++) {
            final int k = i;
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + ": value="  + k);
            }).start();
        }
        SystemClock.sleep(2000);
    }

    @Test
    public void testDefaultWithRx() {
        Observable.create(emitter -> {
                for (int i = 0; i < 5; i++) {
                    final int k = i;
                    new Thread(() -> {
                        emitter.onNext(k);
                    }).start();
                }
            }).subscribe(value -> System.out.println(Thread.currentThread().getName() + ": value=" + value));
        SystemClock.sleep(2000);
    }

    @Test
    public void testDefault_Sequential() {
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + ": value=" + i);
            }
        }).start();
        SystemClock.sleep(2000);
    }

    @Test
    public void testObservable() {
        Observable.range(0, 5)
                .subscribeOn(Schedulers.computation()) // (1)
                .subscribe(value -> System.out.println(Thread.currentThread().getName() + ": value=" + value));
        SystemClock.sleep(2000);
    }

    @Test
    public void testThreadPool() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10,
                500, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(5));
        for (int i = 0; i < 15; i++) {
            final int k = i;
            executor.execute(() -> System.out.println(Thread.currentThread().getName() + ": value="  + k));
        };
        SystemClock.sleep(2000);
    }

    @Test
    public void testThreadPool_Sequential() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 15; i++) {
            final int k = i;
            executor.execute(() -> System.out.println(Thread.currentThread().getName() + ": value="  + k));
        };
        SystemClock.sleep(2000);
    }

    @Test
    public void testObservable_withProducerConsumer() {
        Observable.<Order>create(emitter -> {
            emitter.onNext(new Order("라면", 2));
            emitter.onNext(new Order("떡볶이", 3));
            emitter.onNext(new Order("김밥", 4));
        }).doOnNext(ignored -> System.out.println("observable " + Thread.currentThread().getName()))
            .subscribeOn(Schedulers.io()) // (1)
            .observeOn(Schedulers.io()) // (2)
            .subscribe(order -> {
                System.out.println(Thread.currentThread().getName() + ": order=" + order.name);
                makeFood(order);
            });
        SystemClock.sleep(10000);
    }

    @Test
    public void testObservable_withProducerConsumer2() {
        Scheduler scheduler = Schedulers.io();
        Observable.<Order>create(emitter -> {
            emitter.onNext(new Order("라면", 2));
            emitter.onNext(new Order("떡볶이", 3));
            emitter.onNext(new Order("김밥", 4));
        }).doOnNext(ignored -> System.out.println("observable " + Thread.currentThread().getName()))
                .subscribeOn(scheduler) // (1)
                .observeOn(scheduler) // (2)
                .subscribe(order -> {
                    System.out.println(Thread.currentThread().getName() + ": order=" + order.name);
                    makeFood(order);
                });
        SystemClock.sleep(10000);
    }

    private void makeFood(Order order) {
        SystemClock.sleep(1000);
    }

    private class Order {
        String name;
        Order(String name, int count) {
            this.name = name;
        }
    }

    @Test
    public void testThreadNested() {
        new Thread(() -> {
            new Thread(() -> {
                new Thread(() -> {
                    System.out.println("thread=" + Thread.currentThread().getName()); // (1)
                }, "thread1").start();
            }, "thread2").start();
        }, "thread3").start();
        SystemClock.sleep(500);
    }
}

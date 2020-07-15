package com.suribada.rxjavabook.chap7;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;

public class CompletionStageTest {

    @Test
    public void firstStage() {
        Observable.empty().firstStage(null)
                .thenAccept(value -> System.out.println("value=" + value));
    }

    @Test
    public void firstOrErrorStage() {
        Observable.empty().firstOrErrorStage()
                .exceptionally(e -> -1)
                .thenAccept(value -> System.out.println("value=" + value));
    }

    @Test
    public void firstOrErrorStage2() {
        Observable.just(1, 2, 3).firstOrErrorStage()
                .thenAccept(value -> System.out.println("value=" + value));
    }

    @Test
    public void singleStage() {
        Observable.empty().singleStage(null)
                .thenAccept(value -> System.out.println("value=" + value));
    }

    @Test
    public void singleStage2() {
        Observable.just(1).singleStage(null)
                .thenAccept(value -> System.out.println("value=" + value));
    }

    @Test
    public void singleStage3() {
        Observable.just(1, 2).singleStage(null)
                //.exceptionally(e -> -1)
                .thenAccept(value -> System.out.println("value=" + value))
                .whenComplete((value, e) -> {
                    System.out.println("value=" + value);
                    System.err.println(e);
                });
    }

    @Test
    public void execute() {
        CompletionStage<Void> startStage = CompletableFuture.runAsync(() -> logStart("RxJavaSample"));
        CompletionStage<Void> endStage = CompletableFuture.runAsync(() -> logEnd("RxJavaSample"));

        Observable obs = Observable.interval(1, TimeUnit.SECONDS)
                .take(10);
        obs.publish().autoConnect(3);
        obs.firstStage(null).runAfterEitherAsync(startStage, () -> System.out.println("started"));
        obs.subscribe(System.out::println);
        obs.lastStage(null).runAfterEitherAsync(endStage, () -> System.out.println("ended"));
        SystemClock.sleep(12000);
    }

    private void logStart(String tag) {
        System.out.println(tag + ":start=" + System.currentTimeMillis()
                + ", thread=" + Thread.currentThread().getName());
    }

    private void logEnd(String tag) {
        System.out.println(tag + ":end=" + System.currentTimeMillis() + ", thread=" + Thread.currentThread().getName());
    }
}

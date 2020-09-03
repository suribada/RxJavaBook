package com.suribada.rxjavabook.chap7;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

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

    /**
     * startStage와 endStage는 병렬 실행
     */
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

    @Test
    public void sendGiftToFirstPerson() {
        CompletionStage<Gift> giftCompletionStage = CompletableFuture.supplyAsync(() -> { // (1)
            System.out.println("gift thread=" + Thread.currentThread().getName()); // (2)
            return new Gift();
        }); // (1) 끝

        getPersons().firstStage(new Person("노재춘")) // (3)
                .thenAcceptBothAsync(giftCompletionStage, (person, gift) -> { // (4) 시작
                    System.out.println("combined thread=" + Thread.currentThread().getName()); // (5)
                    sendGift(person, gift);
                }); // (4) 끝
        SystemClock.sleep(2000);
    }

    private Observable<Person> getPersons() {
        return Observable.just(new Person("노재춘"), new Person("김성수"), new Person("강사룡"))
                .subscribeOn(Schedulers.io()) // (6)
                .doOnNext(person -> System.out.println("person thread=" + Thread.currentThread().getName())); // (7)
    }

    private void sendGift(Person person, Gift gift) {
        System.out.println("sendGift");
    }

    class Person {
        String name;

        Person(String name) {
            this.name = name;
        }

    }

    class Gift {

    }
}

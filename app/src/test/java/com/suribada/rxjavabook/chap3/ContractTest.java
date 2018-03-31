package com.suribada.rxjavabook.chap3;

import com.thoughtworks.xstream.mapper.Mapper;

import org.junit.Test;

import io.reactivex.Observable;

/**
 * Created by Noh.Jaechun on 2018. 3. 29..
 */

public class ContractTest {

    @Test(expected = NullPointerException.class)
    public void nullEvent() {
        Observable.just("Hello", null, "RxJava")
                .subscribe(System.out::println);
    }

    @Test
    public void nullEventOnCreate() {
        Observable.create(emitter -> {
            emitter.onNext("Hello");
            emitter.onNext(null);
            emitter.onNext("RxJava");
        }).subscribe(System.out::println,
                System.err::println);
    }

    @Test
    public void nullEventPossible() {
        Observable.just(1, 2, -1, 1, 2)
                .map(dollar -> getCurrentPrice(dollar))
                .subscribe(System.out::println,
                        System.err::println);
    }

    private String getCurrentPrice(int dollar) {
        if (dollar < 0) {
            return null;
        }
        return (dollar * 1000) + " won";
    }

    @Test
    public void nullEventPossible2() {
        Observable.just(1, 2, -1, 1, 2)
                .map(dollar -> getCurrentPrice(dollar))
                .onErrorReturnItem("0 won")
                .subscribe(System.out::println,
                        System.err::println,
                        () -> System.out.println("onComplete")
                );
    }

    @Test
    public void nullEventPossible3() {
        Observable.just(1, 2, -1, 1, 2)
                .map(dollar -> getCurrentPrice3(dollar))
                .subscribe(System.out::println,
                        System.err::println);
    }

    private String getCurrentPrice3(int dollar) {
        if (dollar < 0) {
            throw new IllegalArgumentException("dollar should be bigger than 0");
        }
        return (dollar * 1000) + " won";
    }

    @Test
    public void nullEventPossible5() {
        Observable.just(1, 2, -1, 1, 2)
                .flatMap(dollar -> getCurrentPrice5(dollar)
                        .onErrorReturnItem("0 won")) // (1)
                .subscribe(System.out::println,
                        System.err::println);
    }

    private Observable getCurrentPrice5(int dollar) {
        if (dollar < 0) {
            return Observable.error(
                    new IllegalArgumentException("dollar should be bigger than 0")); // (2)
        }
        return Observable.just((dollar * 1000) + " won"); // (3)
    }




}

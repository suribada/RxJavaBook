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

}

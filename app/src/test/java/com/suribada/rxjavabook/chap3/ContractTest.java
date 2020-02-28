package com.suribada.rxjavabook.chap3;

import org.json.JSONObject;
import org.junit.Test;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;

import static com.facebook.stetho.inspector.network.PrettyPrinterDisplayType.JSON;

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
    public void errorEventPossible() {
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

    private String getCurrentPrice3(int dollar) {
        if (dollar < 0) { // (1) 시작
            throw new IllegalArgumentException("dollar should be bigger than 0");
        } // (1) 끝
        return (dollar * 1000) + " won";
    }

    @Test
    public void testOnErrorReturnItem() {
        Observable.just(1, 2, -1, 1, 2)
                .map(dollar -> getCurrentPrice3(dollar))
                .onErrorReturnItem("0 won") // (2)
                .subscribe(System.out::println,
                        System.err::println,
                        () -> System.out.println("onComplete"));
    }

    private Observable<String> getCurrentPrice4(int dollar) {
        if (dollar < 0) {
            return Observable.error( // (1) 시작
                    new IllegalArgumentException("dollar should be bigger than 0")); // (1) 끝
        }
        return Observable.just((dollar * 1000) + " won"); // (2)
    }

    @Test
    public void testOnErrorReturnItemContinued() {
        Observable.just(1, 2, -1, 1, 2)
                .flatMap(dollar -> getCurrentPrice4(dollar)
                        .onErrorReturnItem("0 won")) // (3)
                .subscribe(System.out::println,
                        System.err::println,
                        () -> System.out.println("onComplete"));
    }

    @Test(expected = NullPointerException.class)
    public void nullEvent_Maybe() {
        Maybe.just(null)
                .subscribe(System.out::println,
                        System.err::println);
    }

    @Test
    public void maybeCreate() {
        Maybe.create(emitter -> {
            emitter.onSuccess(null);
        }).subscribe(System.out::println,
                System.err::println);
    }

    @Test
    public void nullEventPossible5() {
        Observable.just(1, 2, -1, 1, 2)
                .flatMapMaybe(dollar -> getCurrentPrice5(dollar))
                .subscribe(System.out::println,
                        System.err::println);
    }

    private Maybe<String> getCurrentPrice5(int dollar) {
        if (dollar < 0) {
            return Maybe.just(null); // (2)
        }
        return Maybe.just((dollar * 1000) + " won"); // (3)
    }

}

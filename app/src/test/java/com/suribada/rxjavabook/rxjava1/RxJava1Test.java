package com.suribada.rxjavabook.rxjava1;

import org.junit.Test;

import io.reactivex.Observable;

/**
 * Created by lia on 2018-03-03.
 */
public class RxJava1Test {

    private int divideBy(int val) throws Exception {
        try {
            return 10 / val;
        } catch (ArithmeticException e) {
            throw new Exception("Divide by Zero");
        }
    }

    /*
    @Test
    public void throwErrorNotCompile() {
        rx.Observable.just(1, 2, 3, 4, 0)
                .map(val -> divideBy(val))
                .subscribe(System.out::println);
    }
    */

    @Test
    public void throwError() {
        rx.Observable.just(1, 2, 3, 4, 0, 5)
                .map(val -> {
                    try {
                        return divideBy(val);
                    } catch (Exception e) {
                        return -1;
                    }
                })
                .subscribe(System.out::println);
    }

    @Test
    public void throwError2() {
        rx.Observable.just(1, 2, 3, 4, 0, 5)
                .map(val -> {
                    try {
                        return divideBy(val);
                    } catch (Exception e) {
                        return -1;
                    }
                })
                .subscribe(val -> {
                    if (val == -1) {
                        System.out.println("나누기 문제 발생");
                    } else {
                        System.out.println(val);
                    }
                });
    }

    @Test
    public void throwError2_2() {
        rx.Observable.just(1, 2, 3, 4, 0, 5)
                .map(val -> {
                    try {
                        return divideBy(val);
                    } catch (Exception e) {
                        throw new RuntimeException(e); // (1)
                    }
                })
                .subscribe(System.out::println,
                        e -> {
                            System.out.println("나누기 문제 발생: " + e.getMessage());
                        });
    }

    @Test
    public void throwError3() {
        rx.Observable.just(1, 2, 3, 4, 0,5)
                .flatMap(val -> { // (1)
                    try {
                        return rx.Observable.just(divideBy(val)); // (2)
                    } catch (Exception e) {
                        return rx.Observable.error(e); // (3)
                    }
                })
                .subscribe(System.out::println,
                        e -> {
                            System.out.println("나누기 문제 발생: " + e.getMessage());
                        });
    }

    @Test
    public void throwErrorRxJava2() {
        Observable.just(1, 2, 3, 4, 0,5)
                .map(val -> divideBy(val))
                .subscribe(System.out::println,
                        e -> {
                            System.out.println("나누기 문제 발생: " + e.getMessage());
                        });
    }

}

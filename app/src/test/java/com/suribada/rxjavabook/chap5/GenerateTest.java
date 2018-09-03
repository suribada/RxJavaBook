package com.suribada.rxjavabook.chap5;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import io.reactivex.Observable;

/**
 * Created by Noh.Jaechun on 2018. 8. 20..
 */
public class GenerateTest {

    /**
     * java.lang.IllegalStateException: onNext already called in this generate turn
     */
    @Test
    public void generate1() {
        Observable.<Integer>generate(emitter -> {
            emitter.onNext(1);
            emitter.onNext(2); // 예외로 전달
        }).subscribe(System.out::println,
            System.err::println);
    }

    @Test
    public void generate1_onError() {
        Observable.<Integer>generate(emitter -> {
            emitter.onError(new Exception("test"));
            emitter.onError(new Exception("test"));
        }).subscribe(System.out::println,
                System.err::println);

    }

    @Test
    public void generate1_onComplete() {
        Observable.<Integer>generate(emitter -> {
            emitter.onComplete();
            emitter.onComplete();
        }).subscribe(System.out::println,
                System.err::println,
                () -> System.out.println("onComplete"));
    }

    @Test
    public void generate1_more() {
        Observable.<Integer>generate(emitter -> {
            emitter.onNext(1);
            emitter.onComplete();
        }).subscribe(System.out::println,
                System.err::println,
                () -> System.out.println("onComplete"));
    }

    /**
     * 'java.lang.Exception: error'로 끝
     */
    @Test
    public void generate1_more2() {
        Observable.<Integer>generate(emitter -> {
            emitter.onNext(1);
            emitter.onError(new Exception("error"));
            emitter.onComplete();
        }).subscribe(System.out::println,
                System.err::println,
                () -> System.out.println("onComplete"));
    }

    @Test
    public void rangeNumbers() {
        Observable.<Integer, Integer>generate(() -> 1, // (1)
            (num, emitter) -> { // (2)
                emitter.onNext(num); // (3)
                return ++num; // (4)
            }
        ).take(20) // (5)
        .subscribe(System.out::println);
    }

    @Test
    public void rangeStringNumbers() {
        Observable.<String, Integer>generate(() -> 1, // (1)
            (num, emitter) -> {
                emitter.onNext("Number=" + num); // (2)
                return ++num;
            }
        ).take(20)
        .subscribe(System.out::println);
    }

    @Test
    public void rangeNumbersFloat1() {
        Observable.range(1, 20).map(value -> (float) value / 10)
            .take(20)
            .subscribe(System.out::println);
    }

    @Test
    public void rangeNumbersFloat2() {
        Observable.range(1, 20).map(value -> BigDecimal.valueOf(value, 1))
        .take(20)
        .subscribe(System.out::println);
    }

    @Test
    public void pointNumbersFloat() {
        Observable.generate(() -> 0.1f, (num, emitter) -> {
            emitter.onNext(num);
            num += 0.1f;
            return num;
        }).take(20)
        .subscribe(System.out::println);
    }

    @Test
    public void pointNumbers() {
        Observable.generate(() -> BigDecimal.valueOf(0.1), (num, emitter) -> {
            emitter.onNext(num);
            return num.add(BigDecimal.valueOf(0.1));
        }).take(20)
        .subscribe(System.out::println);
    }

    @Test
    public void printFactorial() {
        long currentTime = System.nanoTime();
        Observable.range(1, 20) // (1)
            .map(this::factorial) // (2)
            .subscribe(System.out::println);
        System.out.println("elapsed=" + (System.nanoTime() - currentTime));
    }

    private long factorial(int n) { // (3) 시작
        if (n == 0) {
            return 1L;
        }
        return n * factorial(n - 1);
    } // (3) 끝

    @Test
    public void factorial() {
        long currentTime = System.nanoTime();
        Observable.generate(Base::new, (base, emitter) -> {
            emitter.onNext(base.f);
            base.prepareNext();
            return base;
        }).take(20)
        .subscribe(System.out::println);
        System.out.println("elapsed=" + (System.nanoTime() - currentTime));
    }

    @Test
    public void printFactorial2() {
        long currentTime = System.nanoTime();
        Observable.generate(Base::new, (base, emitter) -> {
    //            if (base.n == 20) {
    //                emitter.onComplete();
    //            }
                emitter.onNext(base.f);
                /*
                base.n++;
                base.f *= (base.n + 1);
                */
                base.prepareNext();
                return base;
            }).take(20)
            .subscribe(System.out::println);

        System.out.println("elapsed=" + (System.nanoTime() - currentTime));
    }

    private class Base {
        int n = 0;
        long f = 1L;

        void prepareNext() { // (2)
            n++;
            f *= (n + 1);
        } // (2) 끝
    }

    @Test
    public void printFactorial3() {
        long currentTime = System.nanoTime();
        Observable.create(emitter -> {
                int n = 0;
                long f = 1L;
                while (!emitter.isDisposed()) {
                    emitter.onNext(f);
                    n++;
                    f *= (n + 1);
                }
            }).take(20)
            .subscribe(System.out::println);
        System.out.println("elapsed=" + (System.nanoTime() - currentTime));
    }



}

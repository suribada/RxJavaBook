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
            emitter.onNext(2);
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
    public void rangeNumbersFloat1() {
        Observable.range(1, 20)
            .map(value -> (float) value / 10)
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

    public class Base {
        int n = 0;
        long f = 1L;

        void prepareNext() {
            n++;
            f *= (n + 1);
        }
    }

    @Test
    public void printFactorial() {
        long currentTime = System.nanoTime();
        Observable.range(1, 20)
            .map(this::factorial)
            .subscribe(System.out::println);
        System.out.println("elapsed=" + (System.nanoTime() - currentTime));
    }

    private long factorial(int n) {
        if (n == 0) {
            return 1L;
        }
        return n * factorial(n - 1);
    }

}

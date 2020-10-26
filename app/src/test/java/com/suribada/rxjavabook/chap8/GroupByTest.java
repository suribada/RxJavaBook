package com.suribada.rxjavabook.chap8;

import androidx.core.util.Pair;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.internal.functions.Functions;
import io.reactivex.rxjava3.observables.GroupedObservable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GroupByTest {

    @Test
    public void groupBy() {
        Observable<Student> obs = Observable.just(
                Student.create("노재춘", 61),
                Student.create("강사룡", 85),
                Student.create("이효근", 72),
                Student.create("하동현", 68),
                Student.create("권태환", 78),
                Student.create("이창신", 91),
                Student.create("최태용", 80),
                Student.create("최희탁", 77));
        obs.groupBy(student -> {
            if (student.getScore() >= 90) { // (1) 시작
                return "A";
            }
            if (student.getScore() >= 80) {
                return "B";
            }
            if (student.getScore() >= 70) {
                return "C";
            }
            if (student.getScore() >= 60) {
                return "D";
            }
            return "F"; // (1) 끝
        }, Student::getName) // (2)
                .flatMapSingle(grouped -> grouped.toList()
                        .map(list -> Pair.create(grouped.getKey(), list))) // (3)
                .subscribe(System.out::println);
    }

    /**
     * From RxJava Test
     */
    @Test
    public void keySelectorAndDelayError() {
        Observable.just(1).concatWith(Observable.<Integer>error(new IllegalArgumentException()))
                .groupBy(Functions.identity(), true)
                .flatMap(g -> g)
                .subscribe(System.out::println, System.err::println);
    }

    @Test
    public void keySelectorAndDelayError_delayErrorFalse() {
        Observable.just(1).concatWith(Observable.<Integer>error(new IllegalArgumentException()))
                .groupBy(Functions.identity())
                .flatMap(g -> g)
                .subscribe(System.out::println, System.err::println);
    }

    /**
     * From RxJava Test
     */
    @Test
    public void keyAndValueSelectorAndDelayError() {
        Observable.just(1).concatWith(Observable.error(new IllegalArgumentException()))
                .groupBy(Functions.identity(), Functions.identity(), true)
                .flatMap(g -> g)
                .subscribe(System.out::println, System.err::println);
    }

    @Test
    public void keyAndValueSelectorAndDelayError_delayErrorFalse() {
        Observable.just(1).concatWith(Observable.error(new IllegalArgumentException()))
                .groupBy(Functions.identity(), Functions.identity())
                .flatMap(g -> g)
                .subscribe(System.out::println, System.err::println);
    }

    @Test
    public void delayError_noThread() {
        Observable.range(1, 10).map(v -> 10 / (4 - v))
                .groupBy(v -> v, true)
                .flatMap(g -> g)
                .subscribe(System.out::println, System.err::println);
    }

    @Test
    public void delayError_false_noThread() {
        Observable.range(1, 10).map(v -> 10 / (4 - v))
                .groupBy(v -> v, false)
                .flatMap(g -> g)
                .subscribe(System.out::println, System.err::println);
    }

    @Test
    public void delayError_thread() {
        Observable.interval(1, TimeUnit.SECONDS).map(v -> 10 / (4 - v))
                .groupBy(v -> v, true)
                .flatMap(g -> g)
                .subscribe(System.out::println, System.err::println);
        SystemClock.sleep(10000);
    }

    @Test
    public void delayError_false_thread() {
        Observable.interval(1, TimeUnit.SECONDS).map(v -> 10 / (4 - v))
                .groupBy(v -> v, false)
                .flatMap(g -> g)
                .subscribe(System.out::println, System.err::println);
        SystemClock.sleep(10000);
    }

    @Test
    public void delayError_false_thread_merge() {
        Observable.merge(Observable.interval(1, TimeUnit.MILLISECONDS).take(7),
                Observable.interval(1, TimeUnit.MILLISECONDS).take(10))
                .map(v -> 10 / (5 - v))
                .groupBy(v -> v % 2, false)
                .flatMapSingle(g -> g.onErrorComplete().toList().map(list -> Pair.create(g.getKey(), list)))
                .subscribe(System.out::println, System.err::println);
        SystemClock.sleep(3000);
    }

    /**
     * 재현이 쉽지 않다.
     *
     * onErrorComplete가 없으면 문제가 있다.
     * 어차피 다운스트림에 onError를 보내는데 group 안에서 먼저 onError를 보냈기 때문
     *
     * java.lang.ArithmeticException: / by zero
     * io.reactivex.rxjava3.exceptions.UndeliverableException: The exception could not be delivered to the consumer because it has already canceled/disposed the flow or the exception has nowhere to go to begin with.
     */
    @Test
    public void delayError_true_thread_merge() {
        Observable.merge(Observable.interval(2, TimeUnit.MILLISECONDS).take(7),
                Observable.interval(3, TimeUnit.MILLISECONDS).take(8),
                Observable.interval(4, TimeUnit.MILLISECONDS).take(9))
                .map(v -> 10 / (5 - v))
                .groupBy(v -> v % 2, true)
                .flatMapSingle(g -> g.onErrorComplete().toList().map(list -> Pair.create(g.getKey(), list)))
                .subscribe(System.out::println, System.err::println);
        SystemClock.sleep(3000);
    }

    @Test
    public void delayError_true_thread_merge_concatWith() {
        Observable.mergeDelayError(Observable.interval(1, TimeUnit.MILLISECONDS).take(2).doOnNext(v -> System.out.println("out1=" + v)),
                Observable.interval(1, TimeUnit.MILLISECONDS).take(3).doOnNext(v -> System.out.println("out2=" + v)),
                Observable.interval(1, TimeUnit.MILLISECONDS).take(4).concatWith(Observable.error(new IllegalArgumentException()))
                        .doOnNext(v -> System.out.println("out3=" + v)))
                //.map(v -> 10 / (5 - v))
                .doOnNext(v -> System.out.println("out4=" + v))
                .groupBy(v -> v % 4, true)
                .flatMapSingle(g -> g.onErrorComplete().toList().map(list -> Pair.create(g.getKey(), list)))
                .subscribe(System.out::println, System.err::println);
        SystemClock.sleep(3000);
    }

}

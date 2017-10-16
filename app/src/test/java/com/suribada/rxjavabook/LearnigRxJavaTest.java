package com.suribada.rxjavabook;

import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;

import static org.junit.Assert.assertTrue;

/**
 * Created by lia on 2017-09-26.
 */

public class LearnigRxJavaTest {

    @Test
    public void testIterable() {
        Observable<String> source =
                Observable.just("Alpha", "Beta", "Gamma", "Delta",
                        "Zeta");
        Iterable<String> allWithLengthFive = source.filter(s ->
                s.length() == 5)
                .blockingIterable();
        for (String s: allWithLengthFive) {
            assertTrue(s.length() == 5);
        }
    }

    @Test
    public void testBlockingNext() {
        Observable<Long> source =
                Observable.interval(1, TimeUnit.MICROSECONDS)
                        .take(1000);
        Iterable<Long> iterable = source.blockingNext();
        for (Long i: iterable) {
            System.out.println(i);
        }
    }

    @Test
    public void testBlockingLatest() {
        Observable<Long> source =
                Observable.interval(1, TimeUnit.MICROSECONDS)
                        .take(1000);

        Iterable<Long> iterable = source.blockingLatest();

        for (Long i: iterable) {
            System.out.println(i);
        }
    }

    @Test
    public void testBlockingMostRecent() {
        Observable<Long> source =
                Observable.interval(10, TimeUnit.MILLISECONDS)
                        .take(5);
        Iterable<Long> iterable = source.blockingMostRecent(-1L);
        for (Long i: iterable) {
            System.out.println(i);
        }
    }

    @Test
    public void testConnectable() {
        ConnectableObservable<Integer> threeRandoms =
                Observable.range(1, 3)
                        .map(i -> randomInt()).publish();

//Observer 1 - print each random integer
        threeRandoms.subscribe(i -> System.out.println("Observer 1: " + i));

//Observer 2 - sum the random integers, then print
        threeRandoms.reduce(0, (total, next) -> total + next)
                .subscribe(i -> System.out.println("Observer 2: " + i));

        threeRandoms.connect();

        // onComplete가 불렸기 때문에 여기는 불리지 않는다.
        threeRandoms.subscribe(i -> System.out.println("Observer3: " + i));
    }

    public static int randomInt() {
        return ThreadLocalRandom.current().nextInt(100000);
    }
}

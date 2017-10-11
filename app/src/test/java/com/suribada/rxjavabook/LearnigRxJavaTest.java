package com.suribada.rxjavabook;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

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
}

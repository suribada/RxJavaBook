package com.suribada.rxjavabook.chap5;

import org.junit.Test;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Created by Noh.Jaechun on 2018. 8. 15..
 */
public class DeferTest {

    @Test
    public void testNotDefer() {
        int start = 3;
        int count = 5;
        Observable<Integer> obs = Observable.range(start, count); // (1)
        obs.subscribe(System.out::println); // (2)
        start = 11;
        count = 7;
        obs.subscribe(System.out::println); // (3)
    }

    int start = 3;
    int count = 5;

    @Test
    public void testDefer() {
//        int start = 3;
//        int count = 5;
        Observable<Integer> obs = Observable.defer(() -> Observable.range(start, count)); // 컴파일 에러
        obs.subscribe(System.out::println);
        start = 11;
        count = 7;
        obs.subscribe(System.out::println);
    }

    @Test
    public void testDefer2() {
        int start = 3;
        int count = 5;
        Observable<Integer> innerObs = Observable.range(start, count);
        Observable<Integer> obs = Observable.defer(() -> innerObs);
        obs.subscribe(System.out::println);
        start = 11;
        count = 7;
        obs.subscribe(System.out::println);
    }

    @Test
    public void testDefer3() {
        Range range = new Range(3, 5);
        Observable<Integer> obs = Observable.defer(() -> Observable.range(range.start, range.count));
        obs.subscribe(System.out::println);
        range.start = 11;
        range.count = 7;
        obs.subscribe(System.out::println);
    }

    class Range {
        int start;
        int count;

        public Range(int start, int count) {
            this.start = start;
            this.count = count;
        }
    }

}

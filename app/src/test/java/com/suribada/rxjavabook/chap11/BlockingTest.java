package com.suribada.rxjavabook.chap11;

import com.suribada.rxjavabook.SystemClock;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import io.reactivex.rxjava3.core.Observable;

public class BlockingTest {

    @BeforeClass
    public static void beforeClass() {
        System.out.println("beforeClass");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("afterClass");
    }

    @Test
    public void elapsedSeconds() {
        Observable<String> obs = Observable.interval(5, TimeUnit.SECONDS).take(12)
                .map(v -> (v + 1) * 5 + "초 경과");
        obs.subscribe(System.out::println, e -> {}, () -> System.out.println("1분 경과"));
    }

    /**
     * 일반적으로 작성하는..
     */
    @Test
    public void elapsedSeconds2() {
        Observable<String> obs = Observable.interval(5, TimeUnit.SECONDS).take(12)
                .map(v -> (v + 1) * 5 + "초 경과");
        obs.subscribe(System.out::println, e -> {}, () -> System.out.println("1분 경과")); // (1)
        SystemClock.sleep(65000); // (2)
    }

    @Test
    public void elapsedSeconds3() {
        Observable<String> obs = Observable.interval(5, TimeUnit.SECONDS).take(12)
                .map(v -> (v + 1) * 5 + "초 경과");
        obs.blockingSubscribe(System.out::println, e -> {}, () -> System.out.println("1분 경과")); // (1)
    }

    @Test
    public void blockingFirst() {
        Observable<String> obs = Observable.interval(5, TimeUnit.SECONDS).take(12)
                .map(v -> (v + 1) * 5 + "초 경과");
        String first = obs.blockingFirst(); // (1)
        System.out.println(first);
    }

    @Test
    public void blockingLast() {
        Observable<String> obs = Observable.interval(5, TimeUnit.SECONDS).take(12)
                .map(v -> (v + 1) * 5 + "초 경과");
        String last = obs.blockingLast(); // (1)
        System.out.println(last);
    }

    @Test
    public void blockingSingle() {
        Observable<String> obs = Observable.interval(5, TimeUnit.SECONDS).take(12)
                .map(v -> (v + 1) * 5 + "초 경과");
        String single = obs.blockingSingle(); // (1)
        System.out.println(single);
    }

    @Test
    public void blockingIterable() {
        Observable<String> obs = Observable.interval(5, TimeUnit.SECONDS).take(12)
                .map(v -> (v + 1) * 5 + "초 경과");
        Iterable<String> iterable = obs.blockingIterable(); // (1)
        for (String each : iterable) { // (2) 시작
            System.out.println(each);
        } // (2) 끝
    }

    /**
     * blockingLatest와 비교하기 위해서 생산 속도를 빨리한 버전
     */
    @Test
    public void blockingIterable_fastProducer() {
        Observable<String> fastObs = Observable.interval(1, TimeUnit.SECONDS).take(60)
                .map(v -> (v + 1) + "초 경과");
        Iterable<String> iterable = fastObs.blockingIterable(); // (1)
        for (String each : iterable) { // (2) 시작
            SystemClock.sleep(2000);
            System.out.println(each);
        } // (2) 끝
    }

    /**
     * 생산 속도가 느리기 때문에 여기서는 영향이 없다.
     */
    @Test
    public void blockingLatest() {
        Observable<String> obs = Observable.interval(5, TimeUnit.SECONDS).take(12)
                .map(v -> (v + 1) * 5 + "초 경과");
        Iterable<String> iterable = obs.blockingLatest(); // (1)
        for (String each : iterable) { // (2) 시작
            System.out.println(each);
        } // (2) 끝
    }

    @Test
    public void blockingLatest_fastProducer() {
        Observable<String> obs = Observable.interval(1, TimeUnit.SECONDS).take(60)
                .map(v -> (v + 1) + "초 경과");
        Iterable<String> iterable = obs.blockingLatest(); // (1)
        for (String each : iterable) { // (2) 시작
            SystemClock.sleep(2000);
            System.out.println(each);
        } // (2) 끝
    }

    @Test
    public void blockingMostRecent() {
        Observable<String> obs = Observable.interval(5, TimeUnit.SECONDS).take(12)
                .map(v -> (v + 1) * 5 + "초 경과");
        Iterable<String> iterable = obs.blockingMostRecent("시작"); // (1)
        for (String each : iterable) { // (2) 시작
            System.out.println(each);
        } // (2) 끝
    }

    @Test
    public void blockingNext() {
        System.out.println(System.currentTimeMillis());
        Observable<String> obs = Observable.interval(5, TimeUnit.SECONDS).take(12)
                .map(v -> (v + 1) * 5 + "초 경과");
        Iterable<String> iterable = obs.blockingNext(); // (1)
        for (String each : iterable) { // (2) 시작
            System.out.println(System.currentTimeMillis() + ": " + each);
        } // (2) 끝
    }

    @Test
    public void blockingNext_fastProducer() {
        System.out.println(System.currentTimeMillis());
        Observable<String> fastObs = Observable.interval(1, TimeUnit.SECONDS).take(60)
                .map(v -> (v + 1) + "초 경과");
        Iterable<String> iterable = fastObs.blockingNext(); // (1)
        for (String each : iterable) { // (2) 시작
            SystemClock.sleep(2000);
            System.out.println(System.currentTimeMillis() + ": " + each);
        } // (2) 끝
    }

    /**
     * Learning RxJava
     */
    @Test
    public void testBlockingNext() {
        Observable<Long> source = Observable.interval(1, TimeUnit.MICROSECONDS).take(1000);
        Iterable<Long> iterable = source.blockingNext();
        for (Long i : iterable) {
            System.out.println(i);
        }
    }

    @Test
    public void blockingStream() {
        Observable<String> obs = Observable.interval(5, TimeUnit.SECONDS).take(12)
                .map(v -> (v + 1) * 5 + "초 경과");
        obs.blockingStream()// (1)
                .forEach(System.out::println); // (2)
    }

    @Test
    public void blockingForEach() {
        Observable<String> obs = Observable.interval(5, TimeUnit.SECONDS).take(12)
                .map(v -> (v + 1) * 5 + "초 경과");
        obs.blockingForEach(System.out::println); // (1)
    }
}

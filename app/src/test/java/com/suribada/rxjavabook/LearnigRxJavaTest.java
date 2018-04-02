package com.suribada.rxjavabook;

import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;

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

        threeRandoms.subscribe(i -> System.out.println("Observer 1: " + i),
                System.err::println,
                () -> System.out.println("onComplete"));

        threeRandoms.reduce(0, (total, next) -> total + next)
                .subscribe(i -> System.out.println("Observer 2: " + i),
                    System.err::println);

        threeRandoms.connect();

        // onComplete가 불렸기 때문에 여기는 불리지 않는다.
        threeRandoms.subscribe(i -> System.out.println("Observer3: " + i));
    }

    public static int randomInt() {
        return ThreadLocalRandom.current().nextInt(100000);
    }

    @Test
    public void schedulerTest() {
        //Declare TestScheduler
        TestScheduler testScheduler = new TestScheduler();

        //Declare TestObserver
        TestObserver<Long> testObserver = new TestObserver<>();

        //Declare Observable emitting every 1 minute
        Observable<Long> minuteTicker =
                Observable.interval(1, TimeUnit.MINUTES,
                        testScheduler);

        //Subscribe to TestObserver
        minuteTicker.subscribe(testObserver);

        //Fast forward by 30 seconds
        testScheduler.advanceTimeBy(30, TimeUnit.SECONDS);

        //Assert no emissions have occurred yet
        testObserver.assertValueCount(1);

        //Fast forward to 70 seconds after subscription
        testScheduler.advanceTimeTo(70, TimeUnit.SECONDS);

        //Assert the first emission has occurred
        testObserver.assertValueCount(1);

        //Fast Forward to 90 minutes after subscription
        testScheduler.advanceTimeTo(90, TimeUnit.MINUTES);

        //Assert 90 emissions have occurred
        testObserver.assertValueCount(90);
    }

}

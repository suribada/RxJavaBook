package com.suribada.rxjavabook.chap11;

import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.TestObserver;

import static org.junit.Assert.assertTrue;

public class BasicTest {

    @Rule
    public RxSchedulerRule schedulerRule = new RxSchedulerRule();

    @Test
    public void test() {
        assertTrue(9 > 5);
    }

    @Test
    public void testObserver() {
        TestObserver<Integer> testObserver = new TestObserver<>(); // (1)
        Observable.range(1, 5).subscribe(testObserver); // (2)
        testObserver.assertValues(1, 2, 3, 4, 5); // (3) 시작
        testObserver.assertComplete(); // (3) 끝
    }

    @Test
    public void testObserver2() {
        TestObserver<Integer> testObserver = Observable.range(1, 5).test(); // (1)
        testObserver.assertValues(1, 2, 3, 4, 5);
        testObserver.assertComplete();
    }

    @Test
    public void testObserver3() {
        Observable.range(1, 5).test()
                .assertValues(1, 2, 3, 4, 5)
                .assertComplete();
    }
}

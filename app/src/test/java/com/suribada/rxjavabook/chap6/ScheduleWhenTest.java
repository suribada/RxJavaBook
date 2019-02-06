package com.suribada.rxjavabook.chap6;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.internal.schedulers.ComputationScheduler;
import io.reactivex.schedulers.Schedulers;

import static java.util.concurrent.TimeUnit.SECONDS;

public class ScheduleWhenTest {

    @Test
    public void testNormal() {
        Observable.range(1, 10).flatMap(ignored -> Observable.timer(1, SECONDS))
                .subscribe(value -> System.out.println("thread=" + Thread.currentThread().getName() + "," + System.currentTimeMillis()));
        SystemClock.sleep(5000);
    }

    /**
     * 각각 스케줄러 지정하면 의미 없다
     */
    @Test
    public void testFirst() {
        Observable.range(1, 10).flatMap(ignored -> Observable.timer(1, SECONDS, maxConcurrentScheduler()))
                .subscribe(value -> System.out.println("thread=" + Thread.currentThread().getName() + "," + System.currentTimeMillis()));
        SystemClock.sleep(5000);
    }

    @Test
    public void testFirst_withFlowable() {
        Flowable.range(1, 10).flatMap(ignored -> Flowable.timer(1, SECONDS, maxConcurrentScheduler()))
                .subscribe(value -> System.out.println("thread=" + Thread.currentThread().getName() + "," + System.currentTimeMillis()));
        SystemClock.sleep(5000);
    }

    /**
     * 1개의 Scheduler만 있어야 한다.
     */
    @Test
    public void testFirst_withOnlyScheduler() {
        Scheduler scheduler = maxConcurrentScheduler();
        Observable.range(1, 10).flatMap(ignored -> Observable.timer(1, SECONDS, scheduler))
                .subscribe(value -> System.out.println("thread=" + Thread.currentThread().getName() + "," + System.currentTimeMillis()));
        SystemClock.sleep(5000);
    }

    private Scheduler maxConcurrentScheduler() {
        return Schedulers.computation().when(workerActions -> {
            Flowable<Completable> workers = workerActions.map(actions -> Completable.concat(actions));
            return Completable.merge(workers, 2);
        });
    }

    private Scheduler throttleScheduler() {
        return Schedulers.computation().when(workerActions -> {
            Flowable<Completable> workers = workerActions.map(actions -> Completable.concat(actions));
            return Completable.concat(workers.map(worker -> worker.delay(1, SECONDS)));
        });
    }

    @Test
    public void test2nd() {
        Observable.range(1, 10).flatMap(ignored -> Observable.timer(1, SECONDS, getLimitScheduler()))
                .subscribe(value -> System.out.println("thread=" + Thread.currentThread().getName() + "," + System.currentTimeMillis()));
        SystemClock.sleep(5000);
    }

    private Scheduler getLimitScheduler() {
        return Schedulers.computation().when(workers -> {
            // use merge max concurrent to limit the number of concurrent
            // callbacks two at a time
            return Completable.merge(Flowable.merge(workers), 2);
        });
    }

    @Test
    public void twoInterval() {
        Scheduler scheduler = maxConcurrentScheduler();
        Observable.interval(5, TimeUnit.SECONDS, scheduler)
                .subscribe(value -> System.out.println(System.currentTimeMillis() +", first="  +  value));
        Observable.interval(8, TimeUnit.SECONDS, scheduler)
                .subscribe(value -> System.out.println(System.currentTimeMillis() +", second=" + value));
        SystemClock.sleep(60000);
    }

    /**
     * 끝이 나야만 다음 Observable을 시작한다. 따라서 세 번째 Observable은 동작하지 않는다.
     */
    @Test
    public void threeInterval() {
        Scheduler scheduler = maxConcurrentScheduler();
        Observable.interval(5, TimeUnit.SECONDS, scheduler)
                .subscribe(value -> System.out.println(System.currentTimeMillis() +", first="  +  value));
        Observable.interval(8, TimeUnit.SECONDS, scheduler)
                .subscribe(value -> System.out.println(System.currentTimeMillis() +", second=" + value));
        Observable.interval(10, TimeUnit.SECONDS, scheduler)
                .subscribe(value -> System.out.println(System.currentTimeMillis() +", third=" + value));
        SystemClock.sleep(60000);
    }

    @Test
    public void threeInterval2() {
        Scheduler scheduler = maxConcurrentScheduler();
        Observable.interval(5, TimeUnit.SECONDS, scheduler).take(3)
                .subscribe(value -> System.out.println(System.currentTimeMillis() +", first="  +  value));
        Observable.interval(8, TimeUnit.SECONDS, scheduler)
                .subscribe(value -> System.out.println(System.currentTimeMillis() +", second=" + value));
        Observable.interval(10, TimeUnit.SECONDS, scheduler)
                .subscribe(value -> System.out.println(System.currentTimeMillis() +", third=" + value));
        SystemClock.sleep(60000);
    }

}

package com.suribada.rxjavabook.chap10;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.internal.functions.Functions;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ParallelTest {

    //@Test
    public void reduce() {
        final long start = System.nanoTime();
        Flowable.range(1, 10000)
                .reduce((total, next) -> total + next) // (1)
                .subscribe(v -> System.out.println("simple elapsed="
                        + (System.nanoTime() - start) + ", sum=" + v));
    }

    /**
     * 위의 것보다 시간이 많이 소요된다.parallel이 좋은 경우는 따져봐야 한다.
     */
    //@Test
    public void parallel_reduce() {
        final long start = System.nanoTime();
        Flowable.range(1, 10000).parallel() // (1)
                .reduce((total, next) -> total + next) // (2)
                .subscribe(v -> System.out.println("parallel elapsed="
                        + (System.nanoTime() - start) + ", sum=" + v));
    }

    /**
     * 첫 밴째 것보다 속도가 나아진다.
     */
    //@Test
    public void parallel_reduce_runOn() {
        final long start = System.nanoTime();
        Flowable.range(1, 10000).parallel()
                .runOn(Schedulers.computation()) // (1)
                .reduce((total, next) -> total + next)
                .subscribe(v -> System.out.println("parallel_runOn elapsed="
                        + (System.nanoTime() - start) + ", sum=" + v));
        SystemClock.sleep(3000);
    }

    @Test
    public void parallel_multiThread() {
        final long start = System.nanoTime();
        Flowable.range(1, 1000).parallel()
                .runOn(Schedulers.io()) // (1)
                .flatMap(v -> complexOperation(v)) // (2)
                .sequential() // (3)
                .subscribe(System.out::println, Functions.emptyConsumer(),
                        () -> System.out.println("parallel elapsed=" + (System.nanoTime() - start)));
        SystemClock.sleep(10000);
    }

    private Flowable<String> complexOperation(Integer v) {
        SystemClock.sleep(20);
        return Flowable.just("value=" + v + 100);
    }

    @Test
    public void parallel_multiThread_flatMap() {
        final long start = System.nanoTime();
        Flowable.range(1, 1000)
                .flatMap(v -> complexOperation(v).subscribeOn(Schedulers.io()), 4) // (1)
                .subscribe(System.out::println, Functions.emptyConsumer(),
                        () -> System.out.println("flatMap elapsed=" + (System.nanoTime() - start)));
        SystemClock.sleep(10000);
    }
}
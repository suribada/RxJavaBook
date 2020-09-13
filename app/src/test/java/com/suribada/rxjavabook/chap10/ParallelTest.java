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
        Flowable.range(1, 1000).reduce((total, next) -> total + next)
                .subscribe(v -> System.out.println("reduce simple elapsed=" + (System.nanoTime() - start) + ", value=" + v));
    }

    /**
     * 위의 것보다 시간이 많이 소요된다.parallel이 좋은 경우는 따져봐야 한다.
     */
    //@Test
    public void parallel_reduce() {
        final long start = System.nanoTime();
        Flowable.range(1, 1000).parallel().reduce((total, next) -> total + next)
                .subscribe(v -> System.out.println("parallel_reduce elapsed=" + (System.nanoTime() - start) + ", value=" + v));
    }

    /**
     * 첫 밴쩌 것보다 속도가 나아진다.
     */
    //@Test
    public void parallel_reduce_runOn() {
        final long start = System.nanoTime();
        Flowable.range(1, 1000).parallel().runOn(Schedulers.computation()).reduce((total, next) -> total + next)
                .subscribe(v -> System.out.println("parallel_reduce_runOn elapsed=" + (System.nanoTime() - start) + ", value=" + v));
        SystemClock.sleep(3000);
    }

    @Test
    public void parallel_multiThread() {
        final long start = System.nanoTime();
        Flowable.range(1, 1000).parallel().runOn(Schedulers.io())
                .flatMap(v -> complextOperation(v))
                .sequential()
                .subscribe(System.out::println, Functions.emptyConsumer(), () -> System.out.println("parallel elapsed=" + (System.nanoTime() - start)));
        SystemClock.sleep(10000);
    }

    private Flowable<String> complextOperation(Integer v) {
        SystemClock.sleep(20);
        return Flowable.just("value=" + v + 100);
    }

    @Test
    public void parallel_multiThread_flatMap() {
        final long start = System.nanoTime();
        Flowable.range(1, 1000)
                .flatMap(v -> complextOperation(v).subscribeOn(Schedulers.io()), 4)
                .subscribe(System.out::println, Functions.emptyConsumer(), () -> System.out.println("flatMap elapsed=" + (System.nanoTime() - start)));
        SystemClock.sleep(10000);
    }
}
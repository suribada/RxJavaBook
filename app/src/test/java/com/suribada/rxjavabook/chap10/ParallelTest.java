package com.suribada.rxjavabook.chap10;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.stream.Collector;

import io.reactivex.internal.functions.Functions;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ParallelTest {

    @Test
    public void reduce() {
        final long start = System.nanoTime();
        Flowable.range(1, 1000).reduce((total, next) -> total + next)
                .subscribe(v -> System.out.println("reduce elapsed=" + (System.nanoTime() - start) + ", value=" + v));
    }

    /**
     * 위의 것보다 시간이 많이 소요된다. parallel이 좋은 경우는 따져봐야 한다.
     */
    @Test
    public void parallel_reduce() {
        final long start = System.nanoTime();
        Flowable.range(1, 1000).parallel().reduce((total, next) -> total + next)
                .subscribe(v -> System.out.println("parallel_reduce elapsed=" + (System.nanoTime() - start) + ", value=" + v));
    }

    @Test
    public void parallel_multiThread() {
        final long start = System.nanoTime();
        Flowable.range(1, 1000).parallel().runOn(Schedulers.io())
                .map(v -> complextOperation(v))
                .sequential()
                .subscribe(System.out::println, Functions.emptyConsumer(), () -> );
        SystemClock.sleep(5000);
    }

    private Integer complextOperation(Integer v) {
        SystemClock.sleep(20);
        return v + 100;
    }
}

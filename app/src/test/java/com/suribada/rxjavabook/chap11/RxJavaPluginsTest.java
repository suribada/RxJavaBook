package com.suribada.rxjavabook.chap11;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RxJavaPluginsTest {

    @Test
    public void scheduler() {
        RxJavaPlugins.setInitComputationSchedulerHandler(schedulerSupplier -> {
            System.out.println("setInitComputationSchedulerHandler");
            return schedulerSupplier.get();
        });
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> {
            System.out.println("setComputationSchedulerHandler");
            return scheduler;
        });
        Schedulers.computation().scheduleDirect(() -> System.out.println("작업"));
        Schedulers.computation().scheduleDirect(() -> System.out.println("작업2"));
        SystemClock.sleep(2000);
        RxJavaPlugins.reset();
        Schedulers.computation().scheduleDirect(() -> System.out.println("작업3"));
        Schedulers.computation().scheduleDirect(() -> System.out.println("작업4"));
        SystemClock.sleep(2000);
        RxJavaPlugins.setInitComputationSchedulerHandler(schedulerSupplier -> {
            System.out.println("setInitComputationSchedulerHandler2");
            return schedulerSupplier.get();
        });
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> {
            System.out.println("setComputationSchedulerHandler2");
            return scheduler;
        });
        Schedulers.computation().scheduleDirect(() -> System.out.println("작업5"));
        Schedulers.computation().scheduleDirect(() -> System.out.println("작업6"));
        SystemClock.sleep(2000);
    }

}

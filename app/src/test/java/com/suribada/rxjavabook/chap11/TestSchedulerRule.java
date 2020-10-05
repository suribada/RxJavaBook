package com.suribada.rxjavabook.chap11;

import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.schedulers.TestScheduler;

public class TestSchedulerRule implements TestRule {

    private TestScheduler testScheduler = new TestScheduler();

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                RxJavaPlugins.setIoSchedulerHandler(scheduler -> testScheduler);
                RxJavaPlugins.setComputationSchedulerHandler(scheduler -> testScheduler);
                try {
                    base.evaluate();
                } catch (Throwable e) {
                    throw e;
                } finally {
                    RxJavaPlugins.reset();
                }
            }

        };
    }

    public TestScheduler getTestScheduler() {
        return testScheduler;
    }

}

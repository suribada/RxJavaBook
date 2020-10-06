package com.suribada.rxjavabook.chap11;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.TestScheduler;

public class TestSchedulerRule implements TestRule { // (1)

    private final TestScheduler testScheduler = new TestScheduler(); // (2)

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                RxJavaPlugins.setIoSchedulerHandler(scheduler -> testScheduler); // (3) 시작
                RxJavaPlugins.setComputationSchedulerHandler(scheduler -> testScheduler);
                RxJavaPlugins.setNewThreadSchedulerHandler(scheduler -> testScheduler);
                RxJavaPlugins.setSingleSchedulerHandler(scheduler -> testScheduler); // (3) 끝
                try {
                    base.evaluate(); // (4)
                } catch (Throwable e) {
                    throw e;
                } finally {
                    RxJavaPlugins.reset(); // (5)
                }
            }

        };
    }

    public TestScheduler getTestScheduler() { // (6) 시작
        return testScheduler;
    } // (6) 끝

}

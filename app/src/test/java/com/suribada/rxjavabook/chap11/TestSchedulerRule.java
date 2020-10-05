package com.suribada.rxjavabook.chap11;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RxSchedulerRule implements TestRule {

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
                RxJavaPlugins.setComputationSchedulerHandler(scheduler -> Schedulers.trampoline());
                RxJavaPlugins.setNewThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
                RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());

                try {
                    base.evaluate();
                } catch (Throwable e) {
                    throw e;
                } finally {
                    RxJavaPlugins.reset();
                    RxAndroidPlugins.reset();
                }
            }

        };
    }

}

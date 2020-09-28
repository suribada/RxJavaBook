package com.suribada.rxjavabook.chap11;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.TestScheduler;

public class WeatherRepositoryPeriodicTest {

    @BeforeClass
    public void beforeClass() {
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> new TestScheduler());
    }

    @AfterClass
    public void afterClass() {
        RxJavaPlugins.reset();
    }

}

package com.suribada.rxjavabook.chap11;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.TestScheduler;

public class TestSchedulerTest {

    @Test
    public void testScheduler() {
        TestScheduler testScheduler = new TestScheduler();
        Scheduler.Worker worker = testScheduler.createWorker();

        worker.schedule(() -> {
            System.out.println("immediate run");
        });
        worker.schedule(() -> System.out.println("delayed"), 3, TimeUnit.SECONDS);
        worker.schedulePeriodically(() -> System.out.println("repeat delayed"), 0, 2, TimeUnit.SECONDS);

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        System.out.println("now=" + testScheduler.now(TimeUnit.SECONDS));

        SystemClock.sleep(2000);
        System.out.println("now2=" + testScheduler.now(TimeUnit.SECONDS));

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        System.out.println("now3=" + testScheduler.now(TimeUnit.SECONDS));

        testScheduler.advanceTimeTo(8, TimeUnit.SECONDS);
        System.out.println("now4=" + testScheduler.now(TimeUnit.SECONDS));
    }
}

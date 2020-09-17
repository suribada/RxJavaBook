package com.suribada.rxjavabook.chap10;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Flowable;

public class SubscriberTest {

    @Test
    public void subscribe() {
        Flowable.range(1, 25)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(10); // (1)
                    }

                    @Override
                    public void onNext(Integer next) {
                        System.out.println("next=" + next); // (2)
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.err.println(t); // (3)
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete"); // (4)
                    }
                });
    }

    @Test
    public void subscribe_multiple() {
        Flowable.range(1, 25)
                .subscribe(new Subscriber<Integer>() {

                    private Subscription subscription;
                    private int processed;

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription = s; // (1)
                        s.request(10);
                    }

                    @Override
                    public void onNext(Integer next) {
                        System.out.println("next=" + next);
                        if (++processed % 10 == 0) { // (2) 시작
                            subscription.request(10);
                        } // (2) 끝
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.err.println(t);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
    }

    @Test
    public void subscribe_interval() {
        Flowable.interval(10, TimeUnit.MILLISECONDS).take(25)
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(10); // (1)
                    }

                    @Override
                    public void onNext(Long next) {
                        System.out.println("next=" + next); // (2)
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.err.println(t); // (3)
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete"); // (4)
                    }
                });
        SystemClock.sleep(3000);
    }
}

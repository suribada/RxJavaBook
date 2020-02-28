package com.suribada.rxjavabook.chap8;

import androidx.annotation.NonNull;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;
import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.functions.Function;

public class RxTest {

    @Test
    public void retryTest() {
        Completable.create(emitter -> {
            System.out.println("executed time=" + System.currentTimeMillis());
            emitter.onError(new Exception("problem occurred"));
        }).retryWhen(backOffDelay(3, 100))
                .doOnComplete(() -> System.out.println("doOnComplete"))
                .doOnError(e -> System.out.println("doOnError"))
        .subscribe(
                () -> System.out.println("success"),
                e -> System.err.println(e)
        );
        SystemClock.sleep(1000);
    }

    @NonNull
    private Function<? super Flowable<Throwable>, ? extends Publisher<Object>> backOffDelay(int retry, int delayMilli) {
        return attempts -> attempts.zipWith(Flowable.range(1, retry), (n, i) -> i)
                .flatMap(i -> Flowable.timer(i * delayMilli, TimeUnit.MILLISECONDS));
    }

    @Test
    public void retryTest2() {
        Completable.create(emitter -> {
            System.out.println("executed time=" + System.currentTimeMillis());
            emitter.onError(new Exception("problem occurred"));
        }).retryWhen(backOffDelay2(3, 100))
                .doOnComplete(() -> System.out.println("doOnComplete"))
                .doOnError(e -> System.out.println("doOnError"))
                .subscribe(
                        () -> System.out.println("success"),
                        e -> System.err.println(e)
                );
        SystemClock.sleep(1000);
    }

    @NonNull
    private Function<? super Flowable<Throwable>, ? extends Publisher<Object>> backOffDelay2(int retry, int delayMilli) {
        return errors -> errors.zipWith(Flowable.range(1, retry), (e, attempt) -> {
            if (attempt == retry) {
                return Flowable.error(e);
            } else {
                return Flowable.timer(attempt * delayMilli, TimeUnit.MILLISECONDS);
            }
        }).flatMap(x -> x);
    }

}

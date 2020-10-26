package com.suribada.rxjavabook.chap2;

import static com.suribada.rxjavabook.SystemClock.*;

import org.junit.Test;

import java.util.Random;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Predicate;


public class FunctionalTest {

    @Test
    public void notLambda() {
        Observable.<Integer>create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Throwable {
                Random random = new Random();
                for (int i = 0; i < 4; i++) {
                    sleep(1000);
                    emitter.onNext(random.nextInt(10));
                }
                emitter.onComplete();
            }
        })
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer value) throws Throwable {
                        return 1000 / value;
                    }
                })
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer value) throws Throwable {
                        return value < 400;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                               @Override
                               public void accept(Integer v) throws Throwable {
                                   System.out.println("onNext=" + v);
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable e) throws Throwable {
                                   System.err.println("onError=" + e.getMessage());
                               }
                           }, new Action() {
                               @Override
                               public void run() throws Throwable {
                                   System.out.println("onComplete");
                               }
                           }
                );
    }

    @Test
    public void lambda() {
        Observable.<Integer>create(emitter -> { // (1) 시작
            Random random = new Random();
            for (int i = 0; i < 4; i++) {
                sleep(1000);
                emitter.onNext(random.nextInt(10));
            }
            emitter.onComplete();
        }) // (1)
                .map(value -> 1000 / value) // (2)
                .filter(value -> value < 400) // (3)
                .subscribe(v -> System.out.println("onNext=" + v), // (4)
                        e -> System.err.println("onError=" + e.getMessage()), // (5)
                        () -> System.out.println("onComplete") // (6)
                );
    }
}

package com.suribada.rxjavabook.chap5;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.Single;

public class CreateTest {

    @Test
    public void testGenerate() {
        Observable.<Observable<Integer>, Integer>generate(() -> 10, (startNo, emmiter) -> {
            int toNo = startNo + 10;
            if (toNo < 100) {
                emmiter.onNext(Observable.range(startNo, 10));
            } else {
                emmiter.onComplete();
            }
            return toNo;
        }).flatMap(obs -> obs).subscribe(System.out::println);
    }

    @Test
    public void testInnerCreate() {
        Observable.just("noh", "jae", "chun")
                .flatMap(name -> Observable.<String>create(emitter -> {
                    emitter.onNext("char=" + name.charAt(0));
                }).concatMap(value -> Observable.range(0, value.length())))
                        .subscribe(System.out::println);
    }

    @Test
    public void testSerialize() {
        Observable obs = Observable.create(emitter -> {
            Emitter serializeEmitter = emitter.serialize(); // (1)
            new Thread(() -> {
                serializeEmitter.onNext(1);
                serializeEmitter.onNext(3);
                serializeEmitter.onNext(5);
                serializeEmitter.onNext(7);
                serializeEmitter.onNext(9);
                // serializeEmitter.onComplete(); // (2)
            }).start();
            new Thread(() -> {
                serializeEmitter.onNext(2);
                serializeEmitter.onNext(4);
                serializeEmitter.onNext(6);
                serializeEmitter.onNext(8);
                serializeEmitter.onNext(10);
                // serializeEmitter.onComplete(); // (3)
            }).start();
        });
        obs.subscribe(value -> System.out.println(Thread.currentThread().getName() + ":" + value),
                System.err::println,
                () -> System.out.println("onComplete"));
        SystemClock.sleep(2000);
    }

    @Test
    public void testSerialize2() {
        Observable obs = Observable.create(emitter -> {
            for (int i = 0; i < 1000; i++) {
                new Thread(() -> {
                    for (int j = 0; j < 10; j++) {
                        emitter.onNext(Thread.currentThread().getName() + ", value=" + j);
                    }
                }).start();
            }
        });
        obs.subscribe(System.out::println);
        SystemClock.sleep(20000);
    }



}

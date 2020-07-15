package com.suribada.rxjavabook.chap7;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;

public class CacheTest {

    @Test
    public void cache() {
        Observable obs = Observable.interval(100, TimeUnit.MILLISECONDS) // (1) 시작
                .take(40)
                .cacheWithInitialCapacity(40); // (1) 끝

        Disposable disposable1 = obs.subscribe(value -> System.out.println("1st observer=" + value)); // (2)
        SystemClock.sleep(500);
        System.out.println("------ 2nd start"); // (3)
        Disposable disposable2 = obs.subscribe(value -> System.out.println("2nd observer=" + value)); // (4)
        SystemClock.sleep(500);
        System.out.println("------ 3rd start"); // (5)
        Disposable disposable3 = obs.subscribe(value -> System.out.println("3rd observer=" + value)); // (6)
        SystemClock.sleep(500);
        disposable1.dispose(); // (7) 시작
        disposable2.dispose();
        disposable3.dispose(); // (7) 끝
        SystemClock.sleep(500);
        System.out.println("------ 4th start"); // (8)
        Disposable disposable4 = obs.subscribe(value -> System.out.println("4th observer=" + value)); // (9)

        SystemClock.sleep(20000);
    }
}

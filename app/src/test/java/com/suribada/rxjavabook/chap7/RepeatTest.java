package com.suribada.rxjavabook.chap7;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;

public class RepeatTest {

    @Test
    public void testRepeat() {
        Observable.just("무", "궁", "화", "꽃", "이", "피", "었", "습", "니", "다")
                .repeat(3)
                .subscribe(System.out::println);
    }

    @Test
    public void repeatUntil() {
        long start = System.currentTimeMillis();
        Observable.just("무", "궁", "화", "꽃", "이", "피", "었", "습", "니", "다")
                .repeatUntil(() -> System.currentTimeMillis() - start > 5000) // (1)
                .subscribe(System.out::println);
        SystemClock.sleep(10000);
    }

    @Test
    public void repeatWhen() {
        Observable.just("무", "궁", "화", "꽃", "이", "피", "었", "습", "니", "다")
                .repeatWhen(obs -> obs.delay(2, TimeUnit.SECONDS)) // (1)
                .subscribe(System.out::println);
        SystemClock.sleep(10000);
    }

    @Test
    public void repeatWhen2() {
        Observable.just("무", "궁", "화", "꽃", "이", "피", "었", "습", "니", "다")
                .repeatWhen(obs -> obs.flatMap(ignored -> Observable.timer(2, TimeUnit.SECONDS))) // (1)
                .subscribe(System.out::println);
        SystemClock.sleep(10000);
    }

    /**
     * 3회 추가 반복된다. onComplete 이벤트도 delay되기 때문
     */
    @Test
    public void repeatWhenTake() {
        Observable.just("무", "궁", "화", "꽃", "이", "피", "었", "습", "니", "다")
                .repeatWhen(obs -> obs.take(3)
                        .doOnComplete(() -> System.out.println("doOnComplete"))
                        .delay(2, TimeUnit.SECONDS) // (1)
                        .doOnComplete(() -> System.out.println("doOnComplete2")))
                .subscribe(System.out::println);
        SystemClock.sleep(10000);
    }

    @Test
    public void repeatWhenTake2() {
        Observable.just("무", "궁", "화", "꽃", "이", "피", "었", "습", "니", "다")
                .doOnDispose(() -> System.out.println("upstream dispose"))
                .repeatWhen(obs -> obs.take(3)
                        .doOnComplete(() -> System.out.println("doOnComplete"))
                        .flatMap(ignored -> Observable.timer(2, TimeUnit.SECONDS))
                        .doOnComplete(() -> System.out.println("doOnComplete2"))
                ) // (1)
                .subscribe(System.out::println);
        SystemClock.sleep(10000);
    }

    @Test
    public void repeatWhenTake_withThread() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10)
                .doOnSubscribe(disposable -> System.out.println("upstream subscribe"))
                .doOnDispose(() -> System.out.println("upstream dispose"))
                .repeatWhen(obs -> obs.take(3)
                        .doOnComplete(() -> System.out.println("doOnComplete"))
                        .delay(1, TimeUnit.SECONDS) // (1)
                        .doOnComplete(() -> System.out.println("doOnComplete2")))
                .subscribe(System.out::println);
        SystemClock.sleep(30000);
    }

}

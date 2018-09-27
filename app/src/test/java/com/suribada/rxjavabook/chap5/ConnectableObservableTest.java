package com.suribada.rxjavabook.chap5;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava2.math.MathObservable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observables.ConnectableObservable;

/**
 * Created by Noh.Jaechun on 2018. 9. 11..
 */
public class ConnectableObservableTest {

    @Test
    public void testConnect() {
        ConnectableObservable<Integer> obs = Observable.range(1, 10)
            .map(this::getScore)
            .doOnNext(value -> System.out.println("next=" + value))
            .publish(); // (1)
        obs.to(MathObservable::sumInt)
            .doOnSubscribe(disposable -> System.out.println("observer1 subscribed"))
            .subscribe(sum -> System.out.println("sum=" + sum)); // (2)
        obs.to(MathObservable::averageDouble)
            .doOnSubscribe(disposable -> System.out.println("observer2 subscribed2"))
            .subscribe(average -> System.out.println("average=" + average)); // (3)
        obs.connect(); // (4)
        obs.to(MathObservable::min)
            .doOnSubscribe(disposable -> System.out.println("observer3 subscribed"))
            .subscribe(min -> System.out.println("min=" + min)); // (5)
        obs.to(MathObservable::max)
            .doOnSubscribe(disposable -> System.out.println("observer4 subscribed"))
            .subscribe(min -> System.out.println("max=" + min)); // (6)
    }

    private Random random = new Random();

    private int getScore(long value) {
        return random.nextInt(101);
    }

    @Test
    public void testConnect_thread() {
        ConnectableObservable<Integer> obs = Observable.interval(1, TimeUnit.SECONDS)
            .take(10)
            .map(this::getScore)
            .doOnNext(value -> System.out.println("next=" + value))
            .publish();
        obs.to(MathObservable::sumInt)
            .doOnSubscribe(disposable -> System.out.println("observer1 subscribed"))
            .subscribe(sum -> System.out.println("sum=" + sum));
        obs.to(MathObservable::averageDouble)
            .doOnSubscribe(disposable -> System.out.println("observer2 subscribed"))
            .subscribe(average -> System.out.println("average=" + average));
        obs.connect(); // (1)
        SystemClock.sleep(3000); // (2)
        obs.to(MathObservable::sumInt)
            .doOnSubscribe(disposable -> System.out.println("observer3 subscribed"))
            .subscribe(min -> System.out.println("sum2=" + min));
        obs.to(MathObservable::averageDouble)
            .doOnSubscribe(disposable -> System.out.println("observer4 subscribed"))
            .subscribe(min -> System.out.println("average2=" + min));
        SystemClock.sleep(10000);
    }

    @Test
    public void testConnect_thread_and_dispose() {
        ConnectableObservable<Integer> obs = Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> System.out.println("obs disposed"))
                .map(this::getScore)
                .doOnDispose(() -> System.out.println("obs mapped disposed")) // (1)
                .doOnNext(value -> System.out.println("next=" + value))
                .publish();
        obs.doOnSubscribe(disposable -> System.out.println("observer1 subscribed"))
                .doOnDispose(() -> System.out.println("observer1 disposed")) // (2)
                .map(score -> "score: " + score)
                .subscribe(value -> System.out.println("observer1 " + value));
        obs.doOnSubscribe(disposable -> System.out.println("observer2 subscribed"))
                .doOnDispose(() -> System.out.println("observer2 disposed")) // (3)
                .map(score -> "score: " + (10 + score))
                .subscribe(value -> System.out.println("observer2 " + value));
        Disposable disposable = obs.connect(); // (4)
        SystemClock.sleep(3000); // (5)
        disposable.dispose(); // (6)
        obs.subscribe(score -> System.out.println("observer3 score: " + score)); // (7)
        SystemClock.sleep(10000);
    }

    @Test
    public void testConnect_thread_and_dispose_separate() {
        ConnectableObservable<Integer> obs = Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> System.out.println("obs disposed"))
                .map(this::getScore)
                .doOnDispose(() -> System.out.println("obs mapped disposed"))
                .doOnNext(value -> System.out.println("next=" + value))
                .publish();
        Disposable disposable1
            = obs.doOnSubscribe(disposable -> System.out.println("observer1 subscribed")) // (1)
                .doOnDispose(() -> System.out.println("observer1 disposed")) // (2)
                .map(score -> "score: " + score)
                .subscribe(value -> System.out.println("observer1 " + value));
        Disposable disposable2
            = obs.doOnSubscribe(disposable -> System.out.println("observer2 subscribed")) // (3)
                .doOnDispose(() -> System.out.println("observer2 disposed")) // (4)
                .map(score -> "score: " + (10 + score))
                .subscribe(value -> System.out.println("observer2 " + value));
        obs.connect();
        SystemClock.sleep(3000);
        disposable1.dispose(); // (5)
        SystemClock.sleep(2000);
        disposable2.dispose(); // (6)
        obs.subscribe(score -> System.out.println("observer3 score: " + score)); // (7)
        SystemClock.sleep(10000);

    }

    @Test
    public void testConnect_thread_withDisposeFunction() {
        ConnectableObservable<Integer> obs = Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> System.out.println("obs disposed"))
                .map(this::getScore)
                .doOnNext(value -> System.out.println("next=" + value))
                .publish();
        obs.doOnSubscribe(disposable -> System.out.println("observer1 subscribed"))
                .doOnDispose(() -> System.out.println("observer1 disposed"))
                .map(score -> "score: " + score)
                .subscribe(value -> System.out.println("observer1 " + value));
        obs.doOnSubscribe(disposable -> System.out.println("observer2 subscribed"))
                .doOnDispose(() -> System.out.println("observer2 disposed"))
                .map(score -> 10 + score)
                .subscribe(addedScore -> System.out.println("observer2 score=" + addedScore));
        obs.connect(disposable -> {
            SystemClock.sleep(3000);
            disposable.dispose(); // (1)
        });
        SystemClock.sleep(10000); // (2)
    }

    @Test
    public void testConnect_thread_withSeparateDisposable_withLambda() {
        ConnectableObservable<Integer> obs = Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> System.out.println("obs disposed"))
                .map(this::getScore)
                .doOnNext(value -> System.out.println("next=" + value))
                .publish();
        obs.doOnSubscribe(disposable -> System.out.println("observer1 subscribed"))
                .map(score -> "score: " + score)
                .subscribe(value -> System.out.println("observer1 " + value));
        obs.doOnSubscribe(disposable -> System.out.println("observer2 subscribed"))
                .map(score -> 10 + score)
                .subscribe(addedScore -> System.out.println("observer2 score=" + addedScore));
        obs.connect(disposable -> {
            Calendar calendar = Calendar.getInstance();
            if (calendar.get(Calendar.HOUR_OF_DAY) < 12) { // (1) 시작
                disposable.dispose();
            }
        });
        SystemClock.sleep(3000);
    }

    @Test
    public void testConnect_thread_withSeparateDisposable() {
        ConnectableObservable<Integer> obs = Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> System.out.println("obs disposed"))
                .map(this::getScore)
                .doOnNext(value -> System.out.println("next=" + value))
                .publish();
        obs.doOnSubscribe(disposable -> System.out.println("observer1 subscribed"))
                .map(score -> "score: " + score)
                .subscribe(value -> System.out.println("observer1 " + value));
        obs.doOnSubscribe(disposable -> System.out.println("observer2 subscribed"))
                .map(score -> 10 + score)
                .subscribe(addedScore -> System.out.println("observer2 score=" + addedScore));
        ConnectConsumer connectConsumer = new ConnectConsumer();
        obs.connect(connectConsumer); // (1)
        SystemClock.sleep(3000);
        connectConsumer.disposable.dispose(); // (2)
        SystemClock.sleep(10000);
    }

    private class ConnectConsumer implements Consumer<Disposable> {
        public Disposable disposable;

        @Override
        public void accept(Disposable disposable) throws Exception {
            this.disposable = disposable;
            Calendar calendar = Calendar.getInstance();
            if (calendar.get(Calendar.HOUR_OF_DAY) < 12) { // (1) 시작
                disposable.dispose();
            } // (1) 끝
        }
    }

    @Test
    public void testAutoConnect() {
        Observable<Integer> obs = Observable.range(1, 10)
                .map(this::getScore)
                .doOnNext(value -> System.out.println("next=" + value))
                .publish()
                .autoConnect(2); // (1)
        obs.to(MathObservable::sumInt)
                .doOnSubscribe(disposable -> System.out.println("observer1 subscribed"))
                .subscribe(sum -> System.out.println("sum=" + sum));
        obs.to(MathObservable::averageDouble)
                .doOnSubscribe(disposable -> System.out.println("observer2 subscribed2"))
                .subscribe(average -> System.out.println("average=" + average));
        obs.to(MathObservable::min)
                .doOnSubscribe(disposable -> System.out.println("observer3 subscribed"))
                .subscribe(min -> System.out.println("min=" + min));
        obs.to(MathObservable::max)
                .doOnSubscribe(disposable -> System.out.println("observer4 subscribed"))
                .subscribe(min -> System.out.println("max=" + min));
    }

    @Test
    public void testAutoConnect_thread() {
        Observable<Integer> obs = Observable.interval(1, TimeUnit.SECONDS)
                .take(10)
                .map(this::getScore)
                .doOnNext(value -> System.out.println("next=" + value))
                .publish()
                .autoConnect(2);
        obs.to(MathObservable::sumInt)
                .doOnSubscribe(disposable -> System.out.println("observer1 subscribed"))
                .subscribe(sum -> System.out.println("sum=" + sum));
        obs.to(MathObservable::averageDouble)
                .doOnSubscribe(disposable -> System.out.println("observer2 subscribed"))
                .subscribe(average -> System.out.println("average=" + average));
        SystemClock.sleep(3000);
        obs.to(MathObservable::sumInt)
                .doOnSubscribe(disposable -> System.out.println("observer3 subscribed"))
                .subscribe(min -> System.out.println("sum2=" + min));
        obs.to(MathObservable::averageDouble)
                .doOnSubscribe(disposable -> System.out.println("observer4 subscribed"))
                .subscribe(min -> System.out.println("average2=" + min));
        SystemClock.sleep(10000);
    }
}

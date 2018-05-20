package com.suribada.rxjavabook.another;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;

import com.suribada.rxjavabook.R;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.BackpressureOverflowStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;
import io.reactivex.subjects.UnicastSubject;

/**
 * Learing RxJava Code Test
 *
 * Created by lia on 2017-09-28.
 */
public class LearningRxJavaActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.many_buttons);
    }

    public void onClickButton1(View view) {
        Observable.range(1, 999_999_999)
                .map(MyItem::new)
                .observeOn(Schedulers.io())
                .subscribe(myItem -> {
                    SystemClock.sleep(50);
                    System.out.println("Received MyItem " +
                            myItem.id);
                });

    }

    public void onClickButton2(View view) {
        Flowable.range(1, 999_999_999)
                .map(MyItem::new)
                .observeOn(Schedulers.io())
                .subscribe(myItem -> {
                    SystemClock.sleep(50);
                    System.out.println("Received MyItem " +
                            myItem.id);
                });
    }

    static final class MyItem {
        final int id;

        MyItem(int id) {
            this.id = id;
            // System.out.println("Constructing MyItem " + id);
        }
    }

    public void onClickButton3(View view) {
        Flowable.interval(1, TimeUnit.MILLISECONDS)
                .doOnNext(s -> System.out.println("Source pushed "
                        + s))
                .observeOn(Schedulers.io())
                .map(i -> intenseCalculation(i))
                .subscribe(s -> System.out.println("Subscriber received " + s),
                        Throwable::printStackTrace,
                        () -> System.out.println("Done!")
                );
    }


    public static <T> T intenseCalculation(T value) {
//sleep up to 200 milliseconds
        SystemClock.sleep(ThreadLocalRandom.current().nextInt(200));
        return value;
    }

    private Action completeAction = () -> System.out.println("onComplete");

    public void onClickButton4(View view) {

        ConnectableObservable<Integer> threeRandoms =
                Observable.range(1, 3).map(i ->
                        randomInt()).publish();

        threeRandoms.subscribe(i -> System.out.println("Observer 1: " + i), e -> {}, completeAction);
        threeRandoms.subscribe(i -> System.out.println("Observer 2: " + i), e -> {}, completeAction);

        threeRandoms.connect();

        threeRandoms.subscribe(i -> System.out.println("Observer 3: " + i), e -> {}, completeAction);
    }

    public static int randomInt() {
        return ThreadLocalRandom.current().nextInt(100000);
    }

    public void onClickButton5(View view) {
        Observable<Long> seconds =
                Observable.interval(300, TimeUnit.MILLISECONDS)
                        .map(l -> (l + 1) * 300) // map to elapsed milliseconds
                        .replay(1, TimeUnit.SECONDS)
                        .autoConnect();
//Observer 1
        seconds.subscribe(l -> System.out.println("Observer 1: " +
                l));
        SystemClock.sleep(2000);
//Observer 2
        seconds.subscribe(l -> System.out.println("Observer 2: " +
                l));
    }

    public void onClickButton6(View view) {
        Subject<String> subject =
                UnicastSubject.create();
        Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(l -> ((l + 1) * 300) + " milliseconds")
                .subscribe(subject);
        SystemClock.sleep(2000);
        subject.subscribe(s -> System.out.println("Observer 1: " +
                s));
        SystemClock.sleep(2000);
        subject.subscribe(s -> System.out.println("Observer 2: " +
                s));
    }

    public void onClickButton7(View view) {
        Observable<String> items = Observable.just("Alpha", "Beta",
                "Gamma", "Delta", "Epsilon",
                "Zeta", "Eta", "Theta", "Iota");
//delay each String to emulate an intense calculation
        Observable<String> processStrings = items.concatMap(s ->
                Observable.just(s)
                        .delay(randomSleepTime(),
                                TimeUnit.MILLISECONDS)
        );
//run processStrings every 5 seconds, and kill eachprevious instance to start next
        Observable.interval(5, TimeUnit.SECONDS)
                .switchMap(i ->
                        processStrings
                                .doOnDispose(() ->
                                        System.out.println("Disposing! Starting next"))
                ).subscribe(System.out::println);
    }

    public static int randomSleepTime() {
        return ThreadLocalRandom.current().nextInt(2000);
    }

    public void onClickButton8(View view) {
        Observable.range(1, 10)
                .flatMap(i -> Observable.just(i)
                        .subscribeOn(Schedulers.computation())
                        .map(i2 -> intenseCalculationMore(i2))
                )
                .subscribe(i -> System.out.println("Received " + i +
                        " "
                        + new Date() + " on thread "
                        + Thread.currentThread().getName()));
    }

    public static <T> T intenseCalculationMore(T value) {
        SystemClock.sleep(ThreadLocalRandom.current().nextInt(3000));
        return value;
    }

    public void onClickButton9(View view) {
        int coreCount = Runtime.getRuntime().availableProcessors();
        System.out.println("core=" + coreCount);
        AtomicInteger assigner = new AtomicInteger(0);
        Observable.range(1, 10)
                .groupBy(i -> assigner.incrementAndGet() %
                        coreCount)
                .flatMap(grp -> grp.observeOn(Schedulers.io())
                        .map(i2 -> intenseCalculationMore(i2))
                )
                .subscribe(i -> System.out.println("Received " + i +
                        " "
                        + new Date() + " on thread "
                        + Thread.currentThread().getName()));
    }

    public void onClickButton10(View view) {
        Disposable d = Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> System.out.println("Disposing on thread "
                        + Thread.currentThread().getName()))
                .subscribe(i -> System.out.println("Received " +
                        i + " at " + Thread.currentThread().getName()));
        SystemClock.sleep(3000);
        d.dispose();
    }

    public void onClickButton11(View view) {
        Observable.interval(1, TimeUnit.SECONDS)
            .doOnDispose(() -> System.out.println("Disposing on thread "
                    + Thread.currentThread().getName()))
             .take(3)
            .subscribe(i -> System.out.println("Received " +
                    i + " at " + Thread.currentThread().getName()));
    }

    public void onClickButton12(View view) {
        Disposable d = Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> System.out.println("Disposing on thread"
                        + Thread.currentThread().getName()))
                .unsubscribeOn(Schedulers.io())
                .subscribe(i -> System.out.println("Received " +
                        i));
        SystemClock.sleep(3000);
        d.dispose();
    }

    public void onClickButton13(View view) {
        Observable<String> source1 = Observable.interval(100,
                TimeUnit.MILLISECONDS)
                .map(i -> (i + 1) * 100) // map to elapsed time
                .map(i -> "SOURCE 1: " + i)
                .take(10);
        Observable<String> source2 = Observable.interval(300,
                TimeUnit.MILLISECONDS)
                .map(i -> (i + 1) * 300) // map to elapsed time
                .map(i -> "SOURCE 2: " + i)
                .take(3);
        Observable<String> source3 = Observable.interval(2000,
                TimeUnit.MILLISECONDS)
                .map(i -> (i + 1) * 2000) // map to elapsed time
                .map(i -> "SOURCE 3: " + i)
                .take(2);
        Observable.concat(source1, source2, source3)
                .doOnDispose(() -> System.out.println("onDispose"))
                .throttleLast(1, TimeUnit.SECONDS)
                //.throttleWithTimeout(1, TimeUnit.SECONDS)
                .subscribe(System.out::println);
    }

    public void onClickButton14(View view) {
        Flowable.range(1, 1000)
                .doOnNext(s -> System.out.println("Source pushed "
                        + s))
                .observeOn(Schedulers.io())
                .map(i -> intenseCalculation(i))
                //.doOnNext(s -> System.out.println("map Source pushed " + s))
                .subscribe(new Subscriber<Integer>() {
                    Subscription subscription;
                    AtomicInteger count = new AtomicInteger(0);

                    @Override
                    public void onSubscribe(Subscription
                                                    subscription) {
                        this.subscription = subscription;
                        System.out.println("Requesting 40 items!");
                        subscription.request(40);
                    }

                    @Override
                    public void onNext(Integer s) {
                        SystemClock.sleep(50);
                        System.out.println("Subscriber received " +
                                s);
                        if (count.incrementAndGet() % 20 == 0 &&
                                count.get() >= 40) {
                            System.out.println("Requesting 20 more !");
                            subscription.request(20);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("Done!");
                    }
                });
    }

    public void onClickButton15(View view) {
        Flowable.interval(1, TimeUnit.MILLISECONDS)
                .onBackpressureBuffer(10,
                        () -> System.out.println("overflow!"),
                        BackpressureOverflowStrategy.DROP_LATEST)
                .observeOn(Schedulers.io())
                .subscribe(i -> {
                    SystemClock.sleep(5);
                    System.out.println(i);
                });
    }

    public void onClickButton16(View view) {
        randomGenerator(1, 10000)
                .subscribeOn(Schedulers.computation())
                .doOnNext(i -> System.out.println("Emitting " +
                        i))
                .observeOn(Schedulers.io())
                .subscribe(i -> {
                    SystemClock.sleep(50);
                    System.out.println("Received " + i);
                });
        }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    static Flowable<Integer> randomGenerator(int min, int max) {
        return Flowable.generate(emitter ->
                emitter.onNext(ThreadLocalRandom.current().nextInt(min, max))
        );
    }

    public void onClickButton17(View view) {
        rangeReverse(100, -100)
        .subscribeOn(Schedulers.computation())
        .doOnNext(i -> System.out.println("Emitting " +
                i))
        .observeOn(Schedulers.io())
        .subscribe(i -> {
            SystemClock.sleep(50);
            System.out.println("Received " + i);
        });
    }

    static Flowable<Integer> rangeReverse(int upperBound, int
            lowerBound) {
        return Flowable.generate(() -> new
                        AtomicInteger(upperBound + 1),
                (state, emitter) -> {
                    int current = state.decrementAndGet();
                    emitter.onNext(current);
                    if (current == lowerBound)
                        emitter.onComplete();
                }
        );
    }

}

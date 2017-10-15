package com.suribada.rxjavabook.another;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;

import com.suribada.rxjavabook.R;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
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
        setContentView(R.layout.learning_rxjava);
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



}

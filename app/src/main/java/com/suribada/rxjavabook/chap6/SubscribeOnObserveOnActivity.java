package com.suribada.rxjavabook.chap6;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.suribada.rxjavabook.R;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Noh.Jaechun on 2018-04-12.
 */

public class SubscribeOnObserveOnActivity extends Activity {

    private TextView title, progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nine_buttons);
        title = findViewById(R.id.title);
        progress = findViewById(R.id.progress);
    }

    public void onClickButton1(View view) {
        Observable.just("Android", "iOS", "Bada")
                .doOnNext(this::log) // (1)
                .subscribeOn(Schedulers.io()) // (2)
                .doOnNext(this::log) // (3)
                .filter(value -> value.length() > 3) // (4)
                .doOnNext(this::log) // (5)
                .subscribeOn(Schedulers.computation()) // (6)
                .doOnNext(this::log) // (7)
                .map(os -> "OS:" + os) // (8)
                .doOnNext(this::log) // (9)
                .subscribeOn(AndroidSchedulers.mainThread()) // (10)
                .subscribe(this::log); // (11)
    }

    public void onClickButton2(View view) {
        Observable.just("Android", "iOS", "Bada")
                .doOnNext(this::log) // (1)
                .observeOn(Schedulers.io()) // (2)
                .doOnNext(this::log) // (3)
                .filter(value -> value.length() > 3) // (4)
                .doOnNext(this::log) // (5)
                .observeOn(Schedulers.computation()) // (6)
                .doOnNext(this::log) // (7)
                .map(os -> "OS:" + os) // (8)
                .doOnNext(this::log) // (9)
                .observeOn(AndroidSchedulers.mainThread()) // (10)
                .subscribe(this::log); // (11)
    }

    public void onClickButton3(View view) {
        Observable.just(1, 2, 4, 7, 812, 1004, 14)
                .subscribeOn(Schedulers.io())
                .filter(x -> x <= 1000)
                .observeOn(Schedulers.computation()) // (1)
                .map(x -> findNearPrime(x)) // (2)
                .map(prime -> "prime=" + prime)
                .observeOn(AndroidSchedulers.mainThread()) // (3)
                .subscribe(title::setText);
    }

    public void onClickButton4(View view) {
        showPrimeNumbers();
    }

    public void onClickButton5(View view) {
        Observable<Integer> obs = Observable.zip(Observable.interval(1, TimeUnit.SECONDS), // (1) 시작
                Observable.just(1, 2, 4, 7, 812, 1004, 14),
                (first, second) -> second); // (1) 끝
        obs.filter(x -> x <= 1000)
                .observeOn(AndroidSchedulers.mainThread()) // (2)
                .doOnNext(value -> progress.setText("progressing=" + value))
                .observeOn(Schedulers.computation())// (3)
                .map(x -> findNearPrime(x))
                .map(prime -> "prime=" + prime)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(title::setText);
    }

    private void showPrimeNumbers() {
        Observable.just(1, 2, 4, 7, 812, 1004, 14)
                .subscribeOn(Schedulers.io())
                .filter(x -> x <= 1000)
                .flatMap(x -> findNearPrimeObservable(x).subscribeOn(Schedulers.computation())) // (2)
                .map(prime -> "prime=" + prime)
                .observeOn(AndroidSchedulers.mainThread()) // (3)
                .subscribe(title::setText);
    }

    private void log(String value) {
        System.out.println(Thread.currentThread().getName() + ": value=" + value);
    }

    private int findNearPrime(Integer x) {
        SystemClock.sleep(2000);
        return 997 + x; // for example
    }

    private Observable<Integer> findNearPrimeObservable(Integer x) {
        return Observable.fromCallable(() -> findNearPrime(x));
    }

    public void onClickButton6(View view) {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    SystemClock.sleep(1000); // (1)
                    title.setText("value=" + value);
                });
    }

    private long complexLogic(long value) {
        SystemClock.sleep(500);
        return value + 1;
    }

    public void onClickButton7(View view) {
        Observable.interval(1, TimeUnit.SECONDS)
                .take(10)
                .map(value -> value + 1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    title.setText("value=" + value);
                });
    }

    public void onClickButton8(View view) {
        Observable<Long> obs = Observable.interval(1, TimeUnit.SECONDS).take(10);
        obs.observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    long next = complexLogic(value); // (1)
                    title.setText("value=" + next);
                });

    }

    public void onClickButton9(View view) {
        Observable<Long> obs = Observable.interval(1, TimeUnit.SECONDS).take(10);
        obs.observeOn(Schedulers.computation()) // (1)
                .map(this::complexLogic) // (2)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    title.setText("value=" + value);
                });
    }

    public void saveDb() {
        Observable<Long> obs = Observable.interval(1, TimeUnit.SECONDS).take(10);
        obs.observeOn(Schedulers.io())
                .doOnNext(this::saveDb)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    title.setText("value=" + value);
                });
    }

    private void saveDb(Long value) {
    }

}
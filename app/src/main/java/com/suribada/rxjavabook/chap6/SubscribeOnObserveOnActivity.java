package com.suribada.rxjavabook.chap6;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.suribada.rxjavabook.R;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Noh.Jaechun on 2018-04-12.
 */

public class SubscribeOnObserveOnActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_two_buttons);
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

    private void log(String value) {
        System.out.println("thread=" + Thread.currentThread().getName() + ", value=" + value);
    }

}
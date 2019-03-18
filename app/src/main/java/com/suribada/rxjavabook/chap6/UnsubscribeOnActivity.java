package com.suribada.rxjavabook.chap6;

import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;

import com.suribada.rxjavabook.R;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UnsubscribeOnActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_three_buttons);
    }

    public void onClickButton1(View view) {
        Disposable disposable = Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(this::log)
                .subscribe(System.out::println);
        SystemClock.sleep(5000);
        disposable.dispose();
    }

    public void onClickButton2(View view) {
        Disposable disposable = Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> log())
                .subscribe(System.out::println);
        SystemClock.sleep(5000);
        new Thread(() -> disposable.dispose(), "disposeThread").start();
        SystemClock.sleep(3000);
    }

    private void log() {
        System.out.println(Thread.currentThread().getName());
    }

    public void onClickButton3(View view) {
        Disposable disposable = Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(this::log)
                .unsubscribeOn(Schedulers.io())
                .subscribe(System.out::println);
        SystemClock.sleep(5000);
        disposable.dispose();
    }
}

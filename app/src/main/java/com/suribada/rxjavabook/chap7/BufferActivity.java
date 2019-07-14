package com.suribada.rxjavabook.chap7;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.suribada.rxjavabook.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class BufferActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.four_buttons);
        startInterval();
    }

    private Subject<Integer> boundarySubject = PublishSubject.create(); // (1)

    void startInterval() {
        Observable.interval(1, TimeUnit.SECONDS)
                .take(20)
                .buffer(boundarySubject) // (2)
                .subscribe(buffer -> System.out.println(Thread.currentThread().getName() + ": " + buffer));
    }

    private void clickBoundary() {
        boundarySubject.onNext(0); // (3)
    }

    public void onClickButton1(View view) {
        clickBoundary();
    }

    public void onClickButton2(View view) {
        Observable.interval(1, TimeUnit.SECONDS)
                .take(20)
                .buffer(() -> Observable.timer(5, TimeUnit.SECONDS, Schedulers.single())) // (1)
                .subscribe(buffer -> System.out.println(Thread.currentThread().getName() + ": " + buffer));
    }

    public void onClickButton3(View view) {
        Observable.interval(1, TimeUnit.SECONDS)
                .take(20)
                .buffer(Observable.interval(5, TimeUnit.SECONDS), // (1)
                        value -> Observable.timer(value + 2, TimeUnit.SECONDS, Schedulers.single())) // (2)
                .subscribe(buffer -> System.out.println(Thread.currentThread().getName() + ": " + buffer));
    }

    public void onClickButton4(View view) {
        Observable.interval(1, TimeUnit.SECONDS)
                .take(20)
                .buffer(Observable.interval(0, 2, TimeUnit.SECONDS), // (1)
                        value -> Observable.timer(value + 2, TimeUnit.SECONDS, Schedulers.single())) // (2)
                .subscribe(buffer -> System.out.println(Thread.currentThread().getName() + ": " + buffer));
    }
}

package com.suribada.rxjavabook.chap5;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.suribada.rxjavabook.R;

import hu.akarnokd.rxjava2.debug.RxJavaAssemblyTracking;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.SafeObserver;

/**
 * Created by Noh.Jaechun on 2018. 11. 1..
 */
public class SafeObserverActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_two_buttons);
    }

    public void onClickButton1(View view) {
        RxJavaAssemblyTracking.enable();
        DisposableObserver<Integer> observer = new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer t) {
                System.out.println(t);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("error occurred");
                throw new ArithmeticException("arithmetic problem"); // (1)
            }

            @Override
            public void onComplete() {
                System.out.println("Done!");
            }
        };
        Observable.just(1, 2, 3, 0, 1, 2)
                .map(value -> 10 / value) // (2)
                .subscribe(observer);
        RxJavaAssemblyTracking.disable();
    }

    public void onClickButton2(View view) {
        DisposableObserver<Integer> observer = new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer t) {
                System.out.println(t); // (1)
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("error occurred");
                throw new ArithmeticException("arithmetic problem");
            }

            @Override
            public void onComplete() {
                System.out.println("Done!");
            }
        };
        Observable.just(1, 2, 3, 0, 1, 2)
                .map(value -> 10 / value)
                .subscribe(new SafeObserver<Integer>(observer));
    }
}

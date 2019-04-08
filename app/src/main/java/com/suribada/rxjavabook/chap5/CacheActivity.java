package com.suribada.rxjavabook.chap5;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.davidmoten.rx2.observable.CachedObservable;
import com.suribada.rxjavabook.R;

import io.reactivex.Observable;

public class CacheActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_two_buttons);
    }

    int start = 1;
    int count = 5;

    public void onClickButton1(View view) {
        start = 1;
        count = 5;
        Observable<Integer> obs = Observable.defer(() -> Observable.range(start, count))
                .doOnNext(System.out::println)
                .cache();
        obs.subscribe(value -> System.out.println("value=" + value));
        obs.subscribe(value -> System.out.println("value=" + value));
        start = 6;
        count = 10;
        obs.subscribe(value -> System.out.println("value=" + value));
    }

    public void onClickButton2(View view) {
        start = 1;
        count = 5;
        CachedObservable<Integer> obs = new CachedObservable(Observable.defer(() -> Observable.range(start, count))
                .doOnNext(System.out::println));
        obs.subscribe(value -> System.out.println("value=" + value));
        obs.subscribe(value -> System.out.println("value=" + value));
        start = 6;
        count = 10;
        obs.reset();
        obs.subscribe(value -> System.out.println("value=" + value));
    }

}

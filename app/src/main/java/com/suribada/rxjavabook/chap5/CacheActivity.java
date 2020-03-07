package com.suribada.rxjavabook.chap5;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

//TODO
//import com.github.davidmoten.rx2.observable.CachedObservable;
import com.suribada.rxjavabook.R;

import io.reactivex.rxjava3.core.Observable;

public class CacheActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_three_buttons);
    }

    int start = 1;
    int count = 5;

    public void onClickButton1(View view) {
        start = 1;
        count = 5;
        Observable<Integer> obs = Observable.defer(() -> Observable.range(start, count)) // (1)
                .doOnNext(value -> System.out.println("doOnNext=" + value));
        obs.subscribe(value -> System.out.println("value=" + value)); // (3)
        obs.subscribe(value -> System.out.println("value=" + value)); // (4)
        start = 6;
        count = 10;
        obs.subscribe(value -> System.out.println("value=" + value)); // (5)
    }

    public void onClickButton2(View view) {
        start = 1;
        count = 5;
        Observable<Integer> obs = Observable.defer(() -> Observable.range(start, count)) // (1)
                .doOnNext(value -> System.out.println("doOnNext=" + value)) // (2)
                .cache(); // (3)
        obs.subscribe(value -> System.out.println("value=" + value)); // (4)
        obs.subscribe(value -> System.out.println("value=" + value)); // (5)
        start = 6;
        count = 10;
        obs.subscribe(value -> System.out.println("value=" + value)); // (6)
    }

    public void onClickButton3(View view) {
        start = 1;
        count = 5;
        /* TODO
        CachedObservable<Integer> obs = new CachedObservable(Observable.defer(() -> Observable.range(start, count))
                .doOnNext(value -> System.out.println("doOnNext=" + value)));
        obs.subscribe(value -> System.out.println("value=" + value));
        obs.subscribe(value -> System.out.println("value=" + value));
        start = 6;
        count = 10;
        obs.reset();
        obs.subscribe(value -> System.out.println("value=" + value));
         */
    }

}

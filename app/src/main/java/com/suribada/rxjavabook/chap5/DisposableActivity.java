package com.suribada.rxjavabook.chap5;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.suribada.rxjavabook.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class DisposableActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.four_buttons);
    }

    private Disposable disposable;

    private void startInterval() {
        disposable = Observable.interval(1, TimeUnit.SECONDS)
                .filter(x -> x % 3 == 0)
                .map(value -> value * 1000  + " won")
                .subscribe(System.out::println);  // (1)
    }

    private void stopInterval() {
        disposable.dispose(); // (2)
    }

    public void onClickButton1(View view) {
        startInterval();
    }

    public void onClickButton2(View view) {
        stopInterval();
    }

    public void onClickButton3(View view) {
        Disposable disposable = Observable.fromArray(1, 2, 3, 4)
                .map(value -> value * 1000)
                .subscribe(System.out::println,
                        Throwable::printStackTrace,
                        () -> System.out.println("onComplete"));
        // ...
        disposable.dispose(); // (1)
    }

}

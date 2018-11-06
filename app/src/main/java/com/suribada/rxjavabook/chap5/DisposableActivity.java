package com.suribada.rxjavabook.chap5;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.suribada.rxjavabook.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class DisposableActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_two_buttons);
        findViewById(R.id.button1).setOnClickListener(view -> {
            startInterval();
        });
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

    public void onClickButton2(View view) {
        startInterval();
    }

}

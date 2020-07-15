package com.suribada.rxjavabook.chap7;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.suribada.rxjavabook.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class DoActionActivity extends Activity {

    private ProgressBar progressBar;
    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_two_buttons);
        progressBar = findViewById(R.id.progress);
    }

    public void onClickButton1(View view) {
        disposables.add(Observable.interval(1, TimeUnit.SECONDS) // (1)
                .take(10) // (2)
                .observeOn(AndroidSchedulers.mainThread()) // (3)
                .doOnSubscribe(disposable -> progressBar.setVisibility(View.VISIBLE)) // (4)
                .doFinally(() -> progressBar.setVisibility(View.GONE)) // (5)
                .subscribe(System.out::println));
    }

    public void onClickButton2(View view) {
        disposables.dispose();
    }

    @Override
    protected void onDestroy() {
        disposables.dispose();
        super.onDestroy();
    }
}

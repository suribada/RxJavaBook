package com.suribada.rxjavabook.hot;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.suribada.rxjavabook.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;

public class ColdObservableActivity extends Activity {

    private Disposable disposable1, disposable2;

    private TextView title;

    private Observable<String> repeatObservable = Observable.interval(1, TimeUnit.SECONDS)
            .map(value -> "current=" + value).observeOn(AndroidSchedulers.mainThread());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_two_buttons);
        title = findViewById(R.id.title);
    }

    public void onClickButton1(View view) {
        if (disposable1 != null && !disposable1.isDisposed()) {
            disposable1.dispose();
        } else {
            disposable1 = repeatObservable.subscribe(this::showTitle);
        }
    }

    private void showTitle(String input) {
        title.setText(input);
    }

    public void onClickButton2(View view) {
        if (disposable2 != null && !disposable2.isDisposed()) {
            disposable2.dispose();
        } else {
            disposable2 = repeatObservable.subscribe(this::showTitle);
        }
    }

}

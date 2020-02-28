package com.suribada.rxjavabook.chap5;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.suribada.rxjavabook.R;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;

public class RepeatDisposeActivity extends Activity {

    private static final int DELAY_TIME = 30;

    private Disposable disposable;

    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);
        title = findViewById(R.id.title);
        disposable = Observable.interval(DELAY_TIME, DELAY_TIME, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(next -> {
                    title.setText("currentDate=" + new Date());
                });
    }

    public void onClickButton(View view) {
        disposable.dispose();
    }

}

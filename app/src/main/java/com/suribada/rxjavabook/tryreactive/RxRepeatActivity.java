package com.suribada.rxjavabook.tryreactive;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.suribada.rxjavabook.R;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class RxRepeatActivity extends Activity {

    private static final int DELAY_TIME = 1000;

    private Disposable disposable;

    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);
        title = (TextView) findViewById(R.id.title);
        disposable = Observable.interval(DELAY_TIME, DELAY_TIME, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(next -> {
                    title.setText("currentDate=" + new Date());
                });
    }

    @Override
    protected void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }

}

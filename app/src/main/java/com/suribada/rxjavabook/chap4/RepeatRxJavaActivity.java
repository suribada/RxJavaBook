package com.suribada.rxjavabook.chap4;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.suribada.rxjavabook.R;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * Created by Noh.Jaechun on 2018. 4. 2..
 */

public class RepeatRxJavaActivity extends Activity {

    private static final String TAG = "RepeatRxJavaActivity";

    private TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);
        title = (TextView) findViewById(R.id.title);
    }

    private static final int DELAY_TIME = 2000;
    private Disposable disposable;

    public void onClickButton(View view) {
        disposable = Observable.interval(0, DELAY_TIME, TimeUnit.MILLISECONDS) // (1)
                .observeOn(AndroidSchedulers.mainThread()) // (2)
                .subscribe(ignored -> title.setText("current=" + new Date()));
    }

    @Override
    protected void onDestroy() {
        if (disposable != null) {
            disposable.dispose(); // (3)
        }
        super.onDestroy();
    }

}

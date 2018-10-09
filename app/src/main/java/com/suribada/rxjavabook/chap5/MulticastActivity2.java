package com.suribada.rxjavabook.chap5;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.CheckBox;
import android.widget.TextView;

import com.suribada.rxjavabook.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;

/**
 * Created by Noh.Jaechun on 2018. 9. 9..
 */
public class MulticastActivity2 extends Activity {

    private Observable<Long> obsInterval
            = Observable.interval(0,1, TimeUnit.SECONDS).publish().autoConnect();
    private Disposable check1Disposable, check2Disposable;
    private TextView title1, title2;
    private CheckBox check1, check2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interval_change);
        title1 = findViewById(R.id.title1);
        title2 = findViewById(R.id.title2);
        check1 = findViewById(R.id.check1);
        check2 = findViewById(R.id.check2);
        check1.setOnClickListener(view -> {
            if (check1.isChecked()) {
                check1Disposable = obsInterval.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::showMinutes);
            } else {
                if (check1Disposable != null && !check1Disposable.isDisposed()) {
                    check1Disposable.dispose();
                }
            }
        });
        check2.setOnClickListener(view -> {
            if (check2.isChecked()) {
                check2Disposable = obsInterval.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::showSeconds);
            } else {
                if (check2Disposable != null && !check2Disposable.isDisposed()) {
                    check2Disposable.dispose();
                }
            }
        });
    }

    private void showMinutes(long seconds) {
        title1.setText("minutes=" + seconds / 60);
    }

    private void showSeconds(long seconds) {
        title2.setText("seconds=" + seconds);
    }

    @Override
    protected void onDestroy() {
        if (check1Disposable != null && !check1Disposable.isDisposed()) {
            check1Disposable.dispose();
        }
        if (check2Disposable != null && !check2Disposable.isDisposed()) {
            check2Disposable.dispose();
        }
        super.onDestroy();
    }

}

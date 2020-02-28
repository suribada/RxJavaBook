package com.suribada.rxjavabook.another;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jakewharton.rxbinding3.widget.RxCompoundButton;
import com.suribada.rxjavabook.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
/**
 * Learning RxJava에서 안드로이드 버전으로 변경
 *
 * Created by lia on 2017-10-22.
 */
public class SwitchMapActivity extends Activity {

    private TextView title;
    private ToggleButton toggleButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.switch_map);
        title = (TextView) findViewById(R.id.title);
        toggleButton = (ToggleButton) findViewById(R.id.toggle);
        RxCompoundButton.checkedChanges(toggleButton).switchMap(selected -> {
                if (selected) {
                    return Observable.interval(1, TimeUnit.SECONDS);
                } else {
                    return Observable.just(-1L);
                    //return Observable.empty();
                }

            })
            //.map(value -> "elapsed=" + value)
                .doOnNext(value -> {
                    System.out.println("next=" + value);
                })
                .doOnComplete(() -> {
                    System.out.println("onComplete");
                })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    // title::setText,
                    value -> {
                        if (value > -1)
                            title.setText("elapsed=" + value);
                        else
                            title.setText("reset");
                    },
                   e -> {},
                    () -> {
                        // empty 전달된 경우
                        // title.setText("reset");
                    }
            );
    }
}

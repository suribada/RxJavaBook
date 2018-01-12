package com.suribada.rxjavabook.chap7;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxAbsListView;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.jakewharton.rxbinding2.widget.RxCheckedTextView;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.suribada.rxjavabook.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * from Learnig RxJava
 *
 * Created by lia on 2018-01-04.
 */
public class StopWatchActivity extends Activity {

    @BindView(R.id.time)
    TextView time;

    @BindView(R.id.start)
    ToggleButton start;

    @BindView(R.id.init)
    Button init;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stop_watch);
        ButterKnife.bind(this);
        time.setText(String.valueOf(0));
        RxCompoundButton.checkedChanges(start).switchMap(checked -> {
                if (checked) {
                    return Observable.interval(100, TimeUnit.MILLISECONDS);
                } else {
                    //return Observable.empty();
                    return Observable.just(0);
                }
            }).observeOn(AndroidSchedulers.mainThread())
            .subscribe(value -> {
                time.setText(String.valueOf(value));
            });
    }

}

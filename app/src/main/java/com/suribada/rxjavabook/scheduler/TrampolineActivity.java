package com.suribada.rxjavabook.scheduler;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.suribada.rxjavabook.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by lia on 2017-10-19.
 */

public class TrampolineActivity extends Activity {

    private TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_two_buttons);
        title = (TextView) findViewById(R.id.title);
    }

    // crash
    public void onClickButton1(View view) {
        Observable.interval(1, TimeUnit.SECONDS).observeOn(Schedulers.trampoline())
                .subscribe(value -> title.setText("val=" + value + ", Thread=" + Thread.currentThread().getName()));
    }

    /**
     * Trampoline은 스레드 안에서  Looper와 같이 큐 구조를 만든 것이다.
     * @param view
     */
    public void onClickButton2(View view) {
        Observable.interval(1, TimeUnit.SECONDS).observeOn(Schedulers.trampoline())
                .map(value -> String.valueOf(value))
                .subscribe(this::printInThread);
    }

    private void printInThread(String input) {
        SystemClock.sleep(2000);
        System.out.println(input + "Thread " + Thread.currentThread().getName());
    }
}

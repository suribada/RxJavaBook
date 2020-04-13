package com.suribada.rxjavabook.chap6;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.suribada.rxjavabook.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;


/**
 * Created by lia on 2017-10-19.
 */
public class TrampolineActivity extends Activity {

    private TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.six_buttons);
        title = findViewById(R.id.title);
    }

    public void onClickButton1(View view) {
        Observable.range(1, 3)
                .subscribeOn(Schedulers.trampoline()) // (1)
                .subscribe(value -> {
                    System.out.println("Thread: " + Thread.currentThread().getName()); // (2)
                    title.setText("val=" + value); // (3)
                });

    }
    /**
     * UI 스레드를 변경하기 때문에 크래시 발생
    */
    public void onClickButton2(View view) {
        Observable.interval(1, TimeUnit.SECONDS) // (1)
                .observeOn(Schedulers.trampoline()) // (2)
                .subscribe(value -> {
                    System.out.println("Thread: " + Thread.currentThread().getName());
                    title.setText("val=" + value);
                });
    }

    /**
     * Trampoline은 스레드 안에서  Looper와 같이 큐 구조를 만든 것이다.
     * @param view
     */
    public void onClickButton3(View view) {
        Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(Schedulers.trampoline())
                .map(value -> String.valueOf(value))
                .subscribe(this::printInThread);
    }

    private void printInThread(String input) {
        SystemClock.sleep(2000);
        System.out.println(input + "Thread " + Thread.currentThread().getName());
    }

    /**
     * 시스템 출력은 되지만 UI 출력은 되지 않는다.
     */
    public void onClickButton4(View view) {
        Observable.interval(1, TimeUnit.SECONDS, Schedulers.trampoline()) // (1)
                .subscribe(value -> {
                    System.out.println("Thread: " + Thread.currentThread().getName());
                    title.setText("val=" + value);
                });
    }

    /**
     * 시스템 출력도 UI 출력도 안 된다.
     * @param view
     */
    public void onClickButton5(View view) {
        Observable.interval(1, TimeUnit.SECONDS, Schedulers.trampoline())
                .doOnNext(value -> System.out.println("onNext: " + value))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    System.out.println("Thread: " + Thread.currentThread().getName());
                    title.setText("val=" + value);
                });
    }

    public void onClickButton6(View view) {
        Observable.interval(1, TimeUnit.SECONDS, Schedulers.trampoline())
                .doOnNext(value -> System.out.println("onNext: " + value))
                .observeOn(Schedulers.io())
                .subscribe(value -> {
                    System.out.println("Thread: " + Thread.currentThread().getName());
                });
    }

}

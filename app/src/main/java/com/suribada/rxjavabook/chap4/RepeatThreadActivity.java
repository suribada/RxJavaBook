package com.suribada.rxjavabook.chap4;


import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.suribada.rxjavabook.R;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Created by lia on 2018-06-04.
 */
public class RepeatThreadActivity extends Activity {

    private boolean cancelled;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.four_buttons);
        ((Button) findViewById(R.id.button4)).setText("취소");
    }

    private int value = 0;

    public void onClickButton1(View view) {
        new Thread(() -> {
            while (!cancelled) {
                callApi();
                System.out.println(String.valueOf(++value));
                SystemClock.sleep(10_000); // (1)
            }
        }).start();
    }

    private void callApi() {
    }

    private ScheduledFuture future;

    public void onClickButton2(View view) {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        future = executor.scheduleAtFixedRate(() -> {
            System.out.println(String.valueOf(++value));
        }, 0, 10, TimeUnit.SECONDS);
    }

    private Disposable disposable;

    public void onClickButton3(View view) {
        disposable = Observable.interval(0, 10, TimeUnit.SECONDS)
                .subscribe(ignored -> {
                    System.out.println(String.valueOf(++value));
                });
    }

    public void onClickButton4(View view) {
        cancelled = true;
        if (future != null) {
            future.cancel(true);
        }
        if (disposable != null) {
            disposable.dispose();
        }
    }

    private List<String> getEmails() {
        return Arrays.asList("suribada@gmail.com", "endofhope@naver.com");
    }

    private void sendMail(String email) {
        System.out.println("start mail");
        SystemClock.sleep(1000);
        System.out.println("end mail");
    }
}

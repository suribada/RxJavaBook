package com.suribada.rxjavabook.chap6;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.suribada.rxjavabook.R;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThreadActivity extends Activity {

    private static final String TAG = "suribada";

    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.five_buttons);
        titleTextView = findViewById(R.id.title);
    }

    public void onClickButton1(View view) {
        int count = 5;
        for (int i = 0; i < count; i++) {
            final int k = i;
            new Thread(() -> {
//                System.out.println("" + k + "번째: "
//                        + Thread.currentThread().getPriority());
                // sleep을 쓰면 개수가 적을 때는 순서가 맞기도 한다.
                //SystemClock.sleep(1000);
                System.out.println(Thread.currentThread().getName() + ": value="  + k);
            }).start();
        }
    }

    /**
     * CPU도 여러 개인데, priority는 큰 영향을 주지는 않는다.
     *
     */
    public void onClickButton2(View view) {
        for (int i = 0; i < 10; i++) {
            final int k = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("" + k + "번째: "
                            + Thread.currentThread().getPriority());
                    SystemClock.sleep(1000);
                    System.out.println(k);
                }
            });
            thread.setPriority(10 - i);
            thread.start();

        }
    }


    public void onClickButton3(View view) {
        System.out.println("cpu count=" + Runtime.getRuntime().availableProcessors());
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10,
                500, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(5));
        for (int i = 0; i < 15; i++) {
            final int k = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(1000);
                    Log.d(TAG, Thread.currentThread().getName() + ", value=" + k);
                }
            });
        };

    }

    public void onClickButton4(View view) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 50; i++) {
            final int k = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(1000);
                    Log.d(TAG, "value=" + k);
                }
            });
        };

    }

    public void onClickButton5(View view) {
        Observable.range(0, 5)
                .subscribeOn(Schedulers.computation()) // (1)
                .doOnNext(value -> System.out.println("observable " + Thread.currentThread().getName() // (2) 시작
                        + ": value=" + value)) // (2) 끝
                .observeOn(AndroidSchedulers.mainThread()) // (3)
                .subscribe(value -> System.out.println("observer " + Thread.currentThread().getName()
                        + ": value=" + value));
    }

    public void onClickButton5_2(View view) {
        Observable.range(0, 5)
                .subscribeOn(Schedulers.computation()) // (1)
                .doOnNext(value -> System.out.println("observable " + Thread.currentThread().getName() // (2) 시작
                        + ": value=" + value)) // (2) 끝
                .observeOn(AndroidSchedulers.mainThread()) // (3)
                .subscribe(value -> titleTextView.setText("value=" + value));
    }

}

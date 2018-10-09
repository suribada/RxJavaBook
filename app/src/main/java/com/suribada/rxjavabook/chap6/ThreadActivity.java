package com.suribada.rxjavabook.chap6;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import com.suribada.rxjavabook.R;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadActivity extends Activity {

    private static final String TAG = "suribada";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.four_buttons);
    }

    public void onClickButton1(View view) {
        for (int i = 0; i < 10; i++) {
            final int k = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("" + k + "번째: "
                            + Thread.currentThread().getPriority());
                    SystemClock.sleep(1000);
                    System.out.println(k);
                }
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

}

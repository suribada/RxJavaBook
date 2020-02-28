package com.suribada.rxjavabook.chap2;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.suribada.rxjavabook.R;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Noh.Jaechun on 2018. 3. 26..
 */
public class ImmutableActivity extends Activity {

    private static final String TAG = "ImmutableActivity";
    private static final int ITEM_COUNT = 1_000_000;

    private int cpuCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_two_buttons);
        cpuCount = Runtime.getRuntime().availableProcessors();
        Log.d(TAG, "cpu=" + Runtime.getRuntime().availableProcessors());
        long start = System.currentTimeMillis();
        long result = 0L;
        for (int i = 0;  i < cpuCount; i++) {
            for (int j = 0; j < ITEM_COUNT; j++) {
                result += ITEM_COUNT * i + j;
            }
        }
        Log.d(TAG, "expected result=" + result + ", timeElapse=" + (System.currentTimeMillis() - start));
    }

    public void onClickButton1(View view) {
        long start = System.currentTimeMillis();
        MutableValue mutableCount = new MutableValue();
        CountDownLatch countDownLatch = new CountDownLatch(cpuCount);
        for (int i = 0; i < cpuCount; i++) {
            final int i2 = i;
            new Thread(() -> {
                for (int j = 0; j < ITEM_COUNT ; j++) {
                    mutableCount.add(i2 * ITEM_COUNT + j);
                }
                countDownLatch.countDown();

            }).start();
        }
        try {
            countDownLatch.await();
            Log.d(TAG, "mutable sum=" + mutableCount.getSum()
                    + ", timeElapse=" + (System.currentTimeMillis() - start));
        } catch (InterruptedException e) {
            Log.d(TAG, "interrupted", e);
        }
    }

    public void onClickButtonWithProblem(View view) {
        long start = System.currentTimeMillis();
        Calculator calculator = new Calculator();
        CountDownLatch countDownLatch = new CountDownLatch(cpuCount);
        for (int i = 0; i < cpuCount; i++) {
            final int i2 = i;
            new Thread(() -> {
                for (int j = 0; j < ITEM_COUNT; j++) {
                    calculator.add(i2 * ITEM_COUNT + j);
                }
                countDownLatch.countDown();
            }).start();

        }
        try {
            countDownLatch.await();
            Log.d(TAG, "immutable sum=" + calculator.getSum()
                    + ", timeElapse=" + (System.currentTimeMillis() - start));
        } catch (InterruptedException e) {
            Log.d(TAG, "interrupted", e);
        }
    }

}

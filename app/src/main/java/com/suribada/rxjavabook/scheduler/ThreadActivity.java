package com.suribada.rxjavabook.scheduler;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

import com.suribada.rxjavabook.R;

public class ThreadActivity extends Activity {

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
     * priority는 큰 영향을 주지는 않는다.
     *
     * @param view
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



}

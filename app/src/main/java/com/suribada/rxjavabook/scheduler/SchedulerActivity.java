package com.suribada.rxjavabook.scheduler;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;

import com.suribada.rxjavabook.R;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lia on 2017-10-17.
 */

public class SchedulerActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_three_buttons);
    }

    public void onClickButton1(View view) {
        Scheduler scheduler = Schedulers.io();
        scheduler.scheduleDirect(() -> {
            printInThread("first");
        });
        scheduler.scheduleDirect(() -> {
            printInThread("second");
        });
    }

    public void onClickButton2(View view) {
        Scheduler scheduler = Schedulers.io();
        Scheduler.Worker worker = scheduler.createWorker();
        worker.schedule(() -> {
            printInThread("third");
        });
        worker.schedule(() -> {
            printInThread("fourth");
        });
    }


    private void printInThread(String input) {
        SystemClock.sleep(2000);
        System.out.println(input + "Thread " + Thread.currentThread().getName());
    }

    public void onClickButton3(View view) {
        Scheduler scheduler = Schedulers.trampoline();
        Scheduler.Worker worker = scheduler.createWorker();
        worker.schedule(() -> {
            printInThread("trampoline first");
        });
        worker.schedule(() -> {
            printInThread("trampoline second");
        });
    }

}

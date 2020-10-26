package com.suribada.rxjavabook.chap1;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.suribada.rxjavabook.R;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Predicate;

/**
 * Created by lia on 2018-03-01.
 */
public class FunctionalActivity extends Activity {

    private static final String TAG = "FunctionalActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_two_buttons);
    }

    public void onClickButton1(View view) {
        perfLambdaTest(10_000_000);
        perfAnonyousTest(10_000_000);
    }

    public void onClickButton2(View view) {
        perfLambdaTest(5);
        perfAnonyousTest(5);
    }

    private void perfLambdaTest(int count) {
        long start = System.currentTimeMillis();
        AtomicInteger value = new AtomicInteger(0);
        for (int i = 0; i < count; i++) {
            new Runnable() {
                @Override
                public void run() {
                    System.out.println(this);
                    //System.out.println(value.getAndIncrement());
                }
            }.run();
        }
        System.out.println("this=" + this);
        System.out.println("elapsed 1=" + (System.currentTimeMillis() - start));
    }

    public void perfAnonyousTest(int count) {
        long start = System.currentTimeMillis();
        AtomicInteger value = new AtomicInteger(0);
        for (int i = 0; i < count; i++) {
            ((Runnable) () -> {
                System.out.println(this);
                //System.out.println(value.getAndIncrement())
            }).run();
        }
        System.out.println("this=" + this);
        System.out.println("elapsed 2=" + (System.currentTimeMillis() - start));

    }



}

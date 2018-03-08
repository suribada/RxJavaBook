package com.suribada.rxjavabook.chap1;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.suribada.rxjavabook.R;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by lia on 2018-03-01.
 */
public class FunctionalActivity extends Activity {

    private static final String TAG = "FunctionalActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.four_buttons);
    }

    public void onClickButton1(View view) {
        Observable.<Integer>create(emitter -> { // (1)
            Random random = new Random();
            for (int i = 0; i < 4; i++) {
                SystemClock.sleep(1000);
                emitter.onNext( random.nextInt(10));
            }
            emitter.onComplete();
         })
        .map(value -> 1000 / value) // (2)
        .filter(value -> value < 400) // (3)
        .subscribe(
                 next -> { // (4)
                     Log.d(TAG, "onNext=" + next);
                     Toast.makeText(this, "값=" + next, Toast.LENGTH_LONG).show();
                 },
                 e -> { // (5)
                     Log.d(TAG, "onError", e);
                     Toast.makeText(this, "에러가 발생했습니다.", Toast.LENGTH_LONG).show();
                 },
                () -> { // (6)
                    Log.d(TAG, "onComplete");
                    Toast.makeText(this, "완료되었습니다.", Toast.LENGTH_LONG).show();
                }
         );
    }

    public void onClickButton2(View view) {
        Observable.<Integer>create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Random random = new Random();
                for (int i = 0; i < 4; i++) {
                    SystemClock.sleep(1000);
                    emitter.onNext(random.nextInt(10));
                }
                emitter.onComplete();
            }
        })
        .map(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer value) throws Exception {
                return 1000 / value;
            }
        })
        .filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer value) throws Exception {
                return value < 400;
            }
        })
        .subscribe(new Consumer<Integer>() {
                       @Override
                       public void accept(Integer next) throws Exception {
                           Log.d(TAG, "onNext=" + next);
                           Toast.makeText(FunctionalActivity.this, "값=" + next, Toast.LENGTH_LONG).show();
                       }
                   }, new Consumer<Throwable>() {
                       @Override
                       public void accept(Throwable e) throws Exception {
                           Log.d(TAG, "onError", e);
                           Toast.makeText(FunctionalActivity.this, "에러가 발생했습니다.", Toast.LENGTH_LONG).show();
                       }
                   }, new Action() {
                       @Override
                       public void run() throws Exception {
                           Log.d(TAG, "onComplete");
                           Toast.makeText(FunctionalActivity.this, "완료되었습니다.", Toast.LENGTH_LONG).show();
                       }
                   }
        );
    }

    public void onClickButton3(View view) {
        perfLambdaTest(10_000_000);
        perfAnonyousTest(10_000_000);
    }

    public void onClickButton4(View view) {
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

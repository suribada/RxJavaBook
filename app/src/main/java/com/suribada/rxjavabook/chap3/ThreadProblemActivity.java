package com.suribada.rxjavabook.chap3;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;

import com.suribada.rxjavabook.R;

import io.reactivex.Observable;

/**
 * Created by Noh.Jaechun on 2018. 3. 29..
 */
public class ThreadProblemActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.four_buttons);
    }

    public void onClickButton1(View view) {
        Observable obs = Observable.create(emitter -> {
                new Thread(() -> {
                    emitter.onNext(1);
                    emitter.onNext(3);
                    emitter.onNext(5);
                    emitter.onNext(7);
                    emitter.onNext(9);
                    // emitter.onComplete(); // (1)
                }).start();
                new Thread(() -> {
                    emitter.onNext(2);
                    emitter.onNext(4);
                    emitter.onNext(6);
                    emitter.onNext(8);
                    emitter.onNext(10);
                    // emitter.onComplete(); // (2)
                }).start();
        });
        obs.subscribe(System.out::println,
                System.err::println,
                () -> System.out.println("onCompleted"));
    }

    /**
     * 스레드 문제 발생 시험 테스트
     */
    public void onClickButton2(View view) {
        Observable obs = Observable.create(emitter -> {
            for (int i = 0; i < 1000; i++) {
                new Thread(() -> {
                    for (int j = 0; j < 100; j++) {
                        emitter.onNext(Thread.currentThread().getName() + ", value=" + j);
                    }
                }).start();
            }
        });
        obs.subscribe(System.out::println);
    }

    /**
     * 둘 다 onComplete를 해야만 한다.
     */
    public void onClickButton3(View view) {
        Observable obs1 = Observable.create(emitter -> {
            new Thread(() -> {
                emitter.onNext(1);
                emitter.onNext(3);
                emitter.onNext(5);
                emitter.onNext(7);
                emitter.onNext(9);
                emitter.onComplete();
            }).start();
        });
        Observable obs2 = Observable.create(emitter -> {
            new Thread(() -> {
                emitter.onNext(2);
                emitter.onNext(4);
                emitter.onNext(6);
                emitter.onNext(8);
                emitter.onNext(10);
                emitter.onComplete();
            }).start();
        });
        Observable.merge(obs1, obs2).subscribe(System.out::println,
                System.err::println,
                () -> System.out.println("onCompleted")
        );
    }

    public void onClickButton4(View view) {
        Observable obs1 = Observable.create(emitter -> {
            new Thread(() -> {
                emitter.onNext(1);
                emitter.onNext(3);
                emitter.onNext(5);
                emitter.onNext(7);
                emitter.onNext(9);
            }).start();
        });
        Observable obs2 = Observable.create(emitter -> {
            new Thread(() -> {
                emitter.onNext(2);
                emitter.onNext(4);
                emitter.onNext(6);
                emitter.onNext(8);
                emitter.onNext(10);
            }).start();
        });
        Observable.merge(obs1, obs2).subscribe(System.out::println,
                System.err::println,
                () -> System.out.println("onCompleted")
        );
    }

}

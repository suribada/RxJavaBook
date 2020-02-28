package com.suribada.rxjavabook.chap3;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import android.view.View;

import com.suribada.rxjavabook.R;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Created by Noh.Jaechun on 2018. 3. 29..
 */
public class ThreadProblemActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.five_buttons);
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
        obs.subscribe(value -> System.out.println(Thread.currentThread().getName() + ":" + value),
                System.err::println,
                () -> System.out.println("onComplete"));
    }

    /**
     * 스레드 문제 발생 시험 테스트
     */
    public void onClickButton2(View view) {
        Observable obs = Observable.create(emitter -> {
            for (int i = 0; i < 1000; i++) {
                new Thread(() -> {
                    for (int j = 0; j < 10; j++) {
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
                emitter.onComplete(); // (1)
            }).start();
        });
        Observable obs2 = Observable.create(emitter -> {
            new Thread(() -> {
                emitter.onNext(2);
                emitter.onNext(4);
                emitter.onNext(6);
                emitter.onNext(8);
                emitter.onNext(10);
                emitter.onComplete(); // (2)
            }).start();
        });
        Observable.merge(obs1, obs2).subscribe(value ->
                        System.out.println(Thread.currentThread().getName() + ":" + value),
                System.err::println,
                () -> System.out.println("onComplete")
        );
    }

    /**
     * obs3까지 추가해본 것
     */
    public void onClickButton4(View view) {
        Observable obs1 = Observable.create(emitter -> {
            new Thread(() -> {
                emitter.onNext(1);
                emitter.onNext(3);
                emitter.onNext(5);
                emitter.onNext(7);
                emitter.onNext(9);
                emitter.onComplete(); // (1)
            }).start();
        });
        Observable obs2 = Observable.create(emitter -> {
            new Thread(() -> {
                emitter.onNext(2);
                emitter.onNext(4);
                emitter.onNext(6);
                emitter.onNext(8);
                emitter.onNext(10);
                emitter.onComplete(); // (2)
            }).start();
        });
        Observable obs3 = Observable.create(emitter -> {
            new Thread(() -> {
                emitter.onNext(11);
                emitter.onNext(12);
                emitter.onNext(13);
                emitter.onNext(14);
                emitter.onNext(15);
                emitter.onComplete(); // (3)
            }).start();
        });
        Observable.merge(obs1, obs2, obs3).subscribe(value ->
                        System.out.println(Thread.currentThread().getName() + ":" + value),
                System.err::println,
                () -> System.out.println("onCompleted")
        );
    }

    public void onClickButton5(View view) {
        Observable obs1 = Observable.create(emitter -> {
            emitter.onNext(1);
            emitter.onNext(3);
            emitter.onNext(5);
            emitter.onNext(7);
            emitter.onNext(9);
            emitter.onComplete();
        }).subscribeOn(Schedulers.computation()); // (1)
        Observable obs2 = Observable.create(emitter -> {
            emitter.onNext(2);
            emitter.onNext(4);
            emitter.onNext(6);
            emitter.onNext(8);
            emitter.onNext(10);
            emitter.onComplete();
        }).subscribeOn(Schedulers.computation()); // (2)
        Observable.merge(obs1, obs2).subscribe(value ->
                        System.out.println(Thread.currentThread().getName() + ":" + value),
                System.err::println,
                () -> System.out.println("onComplete")
        );
    }

}

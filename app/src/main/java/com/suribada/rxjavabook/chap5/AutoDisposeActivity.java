package com.suribada.rxjavabook.chap5;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import com.suribada.rxjavabook.R;

import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava3.debug.RxJavaAssemblyTracking;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.SafeObserver;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;

public class AutoDisposeActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.four_buttons);
    }

    // crash
    public void onClickButton1(View view) {
        RxJavaPlugins.reset();
        Observable.interval(1, TimeUnit.SECONDS)
                .doOnNext(value -> System.out.println("next=" + value))
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Long next) {
                        System.out.println("value=" + 10 / (next - 4)); // (1)
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void onClickButton2(View view) {
        RxJavaAssemblyTracking.enable();
        RxJavaPlugins.setErrorHandler(Throwable::printStackTrace);
        Observable.interval(1, TimeUnit.SECONDS)
                .doOnNext(value -> System.out.println("next=" + value))
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Long next) {
                        System.out.println("value=" + 10 / (next - 4)); // (1)
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
        //RxJavaPlugins.reset(); // interval 연산자가 스레드 동작이므로 여기서 하면 에러 핸들러가 리셋됨
    }

    public void onClickButton3(View view) {
        RxJavaPlugins.reset();
        Observable.interval(1, TimeUnit.SECONDS)
                .doOnNext(value -> System.out.println("next=" + value))
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Long next) {
                        try {
                            System.out.println("value=" + 10 / (next - 4));
                        } catch (Throwable e) {
                            onError(e);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void onClickButton4(View view) {
        RxJavaPlugins.reset();
        Observable.interval(1, TimeUnit.SECONDS)
                .doOnNext(value -> System.out.println("next=" + value))
                .subscribe(new SafeObserver(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Long next) {
                        System.out.println("value=" + 10 / (next - 4));
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
        /*
        Observable.interval(1, TimeUnit.SECONDS)
                .doOnNext(value -> System.out.println("next=" + value))
                .subscribe(next -> System.out.println("value=" + 10 / (next - 4)),
                        Throwable::printStackTrace);
                        */
    }
}

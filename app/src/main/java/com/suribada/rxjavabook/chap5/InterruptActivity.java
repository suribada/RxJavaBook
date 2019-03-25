package com.suribada.rxjavabook.chap5;

import java.util.concurrent.FutureTask;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.suribada.rxjavabook.R;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * wait나 sleep에 내부에 있는 경우는 Observable로 감싸는 것 주의 필요
 */
public class InterruptActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_three_buttons);
    }

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private FutureTask<String> task = new FutureTask<>(() -> {
        Thread.sleep(30000);
        return "jintae";
    });

    public void onClickButton1(View view) {
        compositeDisposable.add(Observable.<String>create(emitter -> {
           try {
               String next = task.get();
               emitter.onNext(next);
               emitter.onComplete();
           } catch (Throwable e) {
               if (!emitter.isDisposed()) {
                   emitter.onError(e);
               }
           }
        }).subscribeOn(Schedulers.io())
                .subscribe(System.out::println));
    }

    public void onClickButton2(View view) {
        compositeDisposable.add(Observable.fromFuture(task).subscribeOn(Schedulers.io())
                .subscribe(System.out::println));
    }

    public void onClickButton3(View view) {
        compositeDisposable.clear();
    }

}

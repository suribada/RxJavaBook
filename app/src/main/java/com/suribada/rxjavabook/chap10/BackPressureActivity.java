package com.suribada.rxjavabook.chap10;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

import androidx.annotation.Nullable;

import com.suribada.rxjavabook.R;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BackPressureActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_two_buttons);
    }

    public void onClickButton1(View view) {
        Observable.range(1, 1_000_000) // (1) 시작
                .map(MenuOrder::new) // (1) 끝
                .observeOn(Schedulers.io()) // (2)
                .subscribe(menuOrder -> { // (3) 시작
                    SystemClock.sleep(3000);
                    System.out.println(menuOrder); // (3) 끝
                });
    }

    public void onClickButton2(View view) {
        Flowable.range(1, 1_000_000) // (1) 시작
                .map(MenuOrder::new) // (1) 끝
                .observeOn(Schedulers.io()) // (2)
                .subscribe(menuOrder -> {
                    SystemClock.sleep(3000);
                    System.out.println(menuOrder);
                });
    }
}

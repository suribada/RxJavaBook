package com.suribada.rxjavabook.chap5;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.suribada.rxjavabook.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * Created by Noh.Jaechun on 2018. 9. 9..
 */
public class MulticastActivity extends Activity {

    private Observable<Long> obsInterval = Observable.interval(5, TimeUnit.SECONDS);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_title_and_two_button);
    }
}

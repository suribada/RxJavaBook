package com.suribada.rxjavabook.chap6;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.suribada.rxjavabook.R;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class WrongSubscribeOnActivity extends Activity {

    private Subject<String> subject = PublishSubject.create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.four_buttons);
        //subject.
    }
}

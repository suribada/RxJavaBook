package com.suribada.rxjavabook.chap5;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.suribada.rxjavabook.R;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


/**
 * Created by Noh.Jaechun on 2018. 9. 2..
 */
public class ResumeActivity2 extends Activity {

    private Subject<Integer> resumeSubject = PublishSubject.create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);
        resumeSubject.skip(1)
            .subscribe(ignored -> showResumeMessage());
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeSubject.onNext(0);
    }

    private void showResumeMessage() {
        Toast.makeText(this, "Activity resumed!", Toast.LENGTH_LONG).show();
    }

}

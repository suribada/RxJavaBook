package com.suribada.rxjavabook.chap5;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.Toast;

import com.suribada.rxjavabook.R;

import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;


/**
 * Created by Noh.Jaechun on 2018. 9. 2..
 */
public class ResumeActivity2 extends Activity {

    private Subject<Integer> resumeSubject = PublishSubject.create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);
        resumeSubject.skip(1) // (1)
            .subscribe(ignored -> showResumeMessage());
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeSubject.onNext(0); // (2)
    }

    private void showResumeMessage() {
        Toast.makeText(this, "Activity resumed!", Toast.LENGTH_LONG).show();
    }

}

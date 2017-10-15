package com.suribada.rxjavabook.subject;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.suribada.rxjavabook.R;

import java.util.Random;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Jaechun.Noh on 2017-10-14.
 */
public class AutoDisposeActivity extends Activity {

    private TextView title;

    private Subject<Integer> subject = PublishSubject.create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_two_buttons);
        title = (TextView) findViewById(R.id.title);
        subject.map(divider -> 100 / divider).subscribe(divided -> {
            title.setText("divided result=" + divided);
        }, e -> Toast.makeText(AutoDisposeActivity.this, "exception="
                + e.toString(), Toast.LENGTH_LONG).show()
        , () -> Toast.makeText(AutoDisposeActivity.this, "onComplete",
                        Toast.LENGTH_LONG).show());
    }

    private Random random = new Random();
    private int[] values = new int[] {2, 3, 4, 5};

    public void onClickButton1(View view) {
        subject.onNext(values[random.nextInt(4)] );
    }

    public void onClickButton2(View view) {
        subject.onNext(0);
    }

}



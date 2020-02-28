package com.suribada.rxjavabook.chap7;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.suribada.rxjavabook.R;

import java.util.concurrent.TimeUnit;

/**
 * Created by Noh.Jaechun on 2019. 7. 15..
 */
public class ThrottleFirstActivity extends Activity {

    private int i;
    private Button button;
    private TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);
        button = findViewById(R.id.button);
        title = findViewById(R.id.title);

        RxView.clicks(button)
                .throttleFirst(3, TimeUnit.SECONDS) // (1)
                .scan(0, (clickCount, click) -> clickCount + 1) // (2)
                .subscribe(clickCount -> {
                    title.setText(Thread.currentThread().getName() + ":" + clickCount); // (3)
                });
    }


}

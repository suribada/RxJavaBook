package com.suribada.rxjavabook.leaktest;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.suribada.rxjavabook.R;

public class LeakActivity3 extends Activity {

    private TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);
        title = findViewById(R.id.title);
    }

    void changeTitle(String value) {
        title.setText(value);
    }

    public void onClickButton(View view) {
        new LeakAsyncTask(this).execute();
    }

}

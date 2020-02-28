package com.suribada.rxjavabook.chap5;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.Toast;

import com.suribada.rxjavabook.R;

/**
 * Created by Noh.Jaechun on 2018. 9. 2..
 */
public class ResumeActivity extends Activity {

    private boolean firstResumed = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (firstResumed) {
            firstResumed = false;
        } else {
            showResumeMessage();
        }
    }

    private void showResumeMessage() {
        Toast.makeText(this, "Activity resumed!", Toast.LENGTH_LONG).show();
    }

}

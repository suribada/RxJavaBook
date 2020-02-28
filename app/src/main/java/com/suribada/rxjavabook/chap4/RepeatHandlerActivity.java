package com.suribada.rxjavabook.chap4;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;

import android.view.View;
import android.widget.TextView;

import com.suribada.rxjavabook.R;

import java.util.Date;

/**
 * Created by Noh.Jaechun on 2018. 4. 2..
 */

public class RepeatHandlerActivity extends Activity {

    private static final String TAG = "RepeatHandlerActivity";

    private TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);
    }

    private Handler handler = new Handler();
    private static final int DELAY_TIME = 2000;

    private Runnable updateTimeTask = new Runnable() {
        @Override
        public void run() {
            title.setText("current=" + new Date());
            handler.postDelayed(this, DELAY_TIME); // (1)
        }
    };

    public void onClickButton(View view) {
        handler.post(updateTimeTask); // (2)
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null); // (3)
        super.onDestroy();
    }

}

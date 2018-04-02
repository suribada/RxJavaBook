package com.suribada.rxjavabook.chap4;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.suribada.rxjavabook.R;

import java.util.Date;

/**
 * Created by Noh.Jaechun on 2018. 4. 2..
 */

public class RepeatRxJavaActivity extends Activity {

    private static final String TAG = "RepeatHandlerActivity";

    private TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_two_buttons);
    }

    private Handler handler = new Handler();

    private static final int DELAY_TIME = 2000;
    private Runnable updateTimeTask = new Runnable() {

        @Override
        public void run() {
            Log.d(TAG, "currentDate=" + new Date()); // 현재 시간을 로그에 출력한다.
            handler.postDelayed(this, DELAY_TIME); // Runnable 자기 자신을 MesssageQueue에 넣는다.
        }

    };

    public void onClickButton1(View view) {
        handler.post(updateTimeTask);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

}

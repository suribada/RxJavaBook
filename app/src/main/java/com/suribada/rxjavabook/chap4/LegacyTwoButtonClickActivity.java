package com.suribada.rxjavabook.chap4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.suribada.rxjavabook.R;
import com.suribada.rxjavabook.chap1.ObserverActivity;


/**
 * Created by Noh.Jaechun on 2018. 3. 29..
 */
public class LegacyTwoButtonClickActivity extends Activity {

    private Button button1, button2, button3, button4, button5;
    private boolean[] clicks = new boolean[5];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_button_click);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button1.setOnClickListener(v -> {
            if (!clicks[0]) { // (1)
                clicks[0] = true;
                checkTwoButtonClicked(); // (2)
            }

        });
        button2.setOnClickListener(v -> {
            if (!clicks[1]) {
                clicks[1] = true;
                checkTwoButtonClicked();
            }

        });
        button3.setOnClickListener(v -> {
            if (!clicks[2]) {
                clicks[2] = true;
                checkTwoButtonClicked();
            }

        });
        button4.setOnClickListener(v -> {
            if (!clicks[3]) {
                clicks[3] = true;
                checkTwoButtonClicked();
            }

        });
        button5.setOnClickListener(v -> {
            if (!clicks[4]) {
                clicks[4] = true;
                checkTwoButtonClicked();
            }

        });
    }

    private void checkTwoButtonClicked() {
        int count = 0;
        for (int i = 0; i < 5; i++) {
            if (clicks[i]) { // (3) 시작
                count++;
            } // (3) 끝
            if (count == 2) {
                startActivity(new Intent(this, ObserverActivity.class));
                finish();
            }
        }
    }
}

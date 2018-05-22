package com.suribada.rxjavabook.chap4;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.suribada.rxjavabook.R;

/**
 * Created by Noh.Jaechun on 2018. 5. 21..
 */
public class CallTaxiActivity extends Activity {

    private int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.four_buttons);
    }

    /**
     * 2번 버튼을 클릭하고 여러 번 클릭해도 문제 없다. enabled 상태가 체크되기 때문에..
     *
     * @param view
     */
    public void onClickButton1(View view) {
        Toast.makeText(this, "count=" + (++count), Toast.LENGTH_LONG).show();
        view.setEnabled(false);
    }

    public void onClickButton2(View view) {
       SystemClock.sleep(10000);
    }

    public void onClickButton3(View view) {

    }

    public void onClickButton4(View view) {

    }

}

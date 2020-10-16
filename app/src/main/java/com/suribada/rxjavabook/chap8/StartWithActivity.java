package com.suribada.rxjavabook.chap8;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.suribada.rxjavabook.R;

public class StartWithActivity extends Activity {

    private ImageView image;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_three_buttons);
        image = findViewById(R.id.image);
    }

    public void onClickButton1(View v) {
    }

}

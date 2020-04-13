package com.suribada.rxjavabook.chap4;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;

import android.widget.TextView;

import com.suribada.rxjavabook.R;

import java.util.Date;

/**
 * Created by Noh.Jaechun on 2017. 2. 24..
 */
public class HandlerRepeatActivity extends Activity {

	private static final int DELAY_TIME = 1000;

	private Handler handler = new Handler();

	private TextView title;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text_and_button);
		title = findViewById(R.id.title);
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				title.setText("currentDate=" + new Date());
				handler.postDelayed(this, DELAY_TIME);
			}
		}, DELAY_TIME);
	}

	@Override
	protected void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}

}

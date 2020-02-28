package com.suribada.rxjavabook.leaktest;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.suribada.rxjavabook.R;

/**
 * 기본 Leak 상황
 */
public class LeakActivity1 extends Activity {

    private TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);
        title = findViewById(R.id.title);
    }

    public void onClickButton(View view) {
        new LeakAsyncTask().execute();
    }

    private class LeakAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            SystemClock.sleep(60000);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            title.setText("title");
        }

    }
}

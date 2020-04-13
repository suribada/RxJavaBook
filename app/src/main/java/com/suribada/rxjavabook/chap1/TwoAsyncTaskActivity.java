package com.suribada.rxjavabook.chap1;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import android.widget.Button;

import com.suribada.rxjavabook.R;

/**
 * Created by Noh.Jaechun on 2018. 2. 5..
 */

public class TwoAsyncTaskActivity extends Activity {

    private boolean firstCondition, secondCondition;

    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);
        button = findViewById(R.id.button);
        button.setEnabled(false);
        new AsyncTaskA().execute();
        new AsyncTaskB().execute();
    }

    private class AsyncTaskA extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            SystemClock.sleep(3000);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            firstCondition = true;
            enableButton();
        }
    }

    private class AsyncTaskB extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            SystemClock.sleep(5000);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            secondCondition = true;
            enableButton();
        }
    }

    private void enableButton() {
        if (firstCondition && secondCondition) {
            button.setEnabled(true);
        }
    }

}

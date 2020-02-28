package com.suribada.rxjavabook.leaktest;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.suribada.rxjavabook.R;

public class LeakActivity2 extends Activity {

    private TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);
        title = findViewById(R.id.title);
    }

    private void changeTitle(String value) {
        title.setText(value);
    }

    public void onClickButton(View view) {
        new LeakAsyncTask(this).execute();
    }

    private static class LeakAsyncTask extends AsyncTask<Void, Void, Void> {

        WeakReference<LeakActivity2> reference;

        LeakAsyncTask(LeakActivity2 activity) {
            reference = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SystemClock.sleep(60000);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (reference.get() != null) {
                reference.get().changeTitle("title");
            }
        }

    }

}

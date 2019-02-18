package com.suribada.rxjavabook.leaktest;

import java.lang.ref.WeakReference;

import android.os.AsyncTask;
import android.os.SystemClock;

class LeakAsyncTask extends AsyncTask<Void, Void, Void> {

    LeakActivity3 activity;

    LeakAsyncTask(LeakActivity3 activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        SystemClock.sleep(60000);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        activity.changeTitle("title");
    }

}

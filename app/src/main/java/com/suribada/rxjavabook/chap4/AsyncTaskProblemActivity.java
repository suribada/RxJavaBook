package com.suribada.rxjavabook.chap4;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.os.AsyncTaskCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.suribada.rxjavabook.R;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Noh.Jaechun on 2018. 4. 2..
 */
public class AsyncTaskProblemActivity extends Activity {

    private static final String TAG = "AsyncTaskProblem";

    private TextView title;
    private ImageView image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_two_buttons);
        title = (TextView) findViewById(R.id.title);
        image = (ImageView) findViewById(R.id.image);
    }

    public void onClickButton1(View view) {
        new BitmapDownloadTask().execute("http://suribada.com/profile.png");
    }

    private class BitmapDownloadTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                return downloadBitmap(urls[0]);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap == null) {
                // 에러 메시지 보여주기
                return;
            }
            image.setImageBitmap(bitmap);
        }
    }

    private Bitmap downloadBitmap(String url) throws Exception {
        Random random = new Random();
        boolean success = random.nextBoolean();
        if (success) {
            Bitmap bitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            canvas.drawRect(0, 0, 300, 300, paint);
            return bitmap;
        }
        throw new FileNotFoundException("file does not exist");
    }

    public void onClickButton2(View view) {
        composedList.clear();
        title.setText(null);
        AsyncTaskCompat.executeParallel(new AsyncTaskA());
        AsyncTaskCompat.executeParallel(new AsyncTaskB());
    }

    private ArrayList<String> composedList = new ArrayList<>();

    private CountDownLatch latch = new CountDownLatch(1);

    private class AsyncTaskA extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... params) {
            SystemClock.sleep(3000);
            return Arrays.asList("spring", "summer", "fall", "winter");
        }

        @Override
        protected void onPostExecute(List<String> result) {
            try {
                composedList.addAll(result);
                title.setText(TextUtils.join(", ", composedList));
            } catch (Exception e) {
                Toast.makeText(AsyncTaskProblemActivity.this, "Error=" + e.getMessage(), Toast.LENGTH_LONG).show();
            } finally {
                latch.countDown();
            }
        }
    }

    private class AsyncTaskB extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                SystemClock.sleep(1000);
                //SystemClock.sleep(5000);
                return Arrays.asList("east", "south", "west", "north");
            } catch (Exception e) {
                Log.d(TAG, "exception = " + e.getMessage());
                return null;
            } finally {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                }
            }
        }

        @Override
        protected void onPostExecute(List<String> result) {
            if (result != null) {
                composedList.addAll(result);
                title.setText(TextUtils.join(", ", composedList));
            }
        }

    }


}

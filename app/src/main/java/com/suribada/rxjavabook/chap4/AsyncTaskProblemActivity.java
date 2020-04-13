package com.suribada.rxjavabook.chap4;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.suribada.rxjavabook.R;

import java.io.FileNotFoundException;
import java.util.Random;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
        setContentView(R.layout.text_and_three_buttons);
        title = findViewById(R.id.title);
        image = findViewById(R.id.image);
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
            if (bitmap == null) { // (1) 시작
                image.setImageBitmap(null);
                Toast.makeText(AsyncTaskProblemActivity.this, "에러 발생",
                        Toast.LENGTH_LONG).show();
                return;
            } // (1)  끝
            image.setImageBitmap(bitmap);
        }

    }

    private class BitmapDownloadTask2 extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                return downloadBitmap(urls[0]);
            } catch (Exception e) {
                runOnUiThread(() -> { // (1)
                    image.setImageBitmap(null);
                    Toast.makeText(AsyncTaskProblemActivity.this, "에러 발생",
                            Toast.LENGTH_LONG).show();
                }); // (1) 끝
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                image.setImageBitmap(bitmap);
            }
        }

    }

    private static final int BITMAP_DOWNLOAD_ERROR = 26;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) { // (1) 시작
            switch (msg.what) {
                case BITMAP_DOWNLOAD_ERROR:
                    image.setImageBitmap(null);
                    Toast.makeText(AsyncTaskProblemActivity.this, "에러 발생",
                            Toast.LENGTH_LONG).show();
                    break;
            }
        } // (1) 끝
    };

    private class BitmapDownloadTask3 extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                return downloadBitmap(urls[0]);
            } catch (Exception e) {
                handler.sendEmptyMessage(BITMAP_DOWNLOAD_ERROR); // (2)
                return null;
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                image.setImageBitmap(bitmap);
            }
        }

    }

    private class BitmapDownloadTask4 extends AsyncTask<String, Void, Boolean> {

        private Bitmap bitmap;

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                bitmap = downloadBitmap(urls[0]);
                return Boolean.TRUE;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) { // (1) 시작
                image.setImageBitmap(null);
                Toast.makeText(AsyncTaskProblemActivity.this, "에러 발생",
                        Toast.LENGTH_LONG).show();
                return;
            } // (1)  끝
            image.setImageBitmap(bitmap);
        }

    }

    private class BitmapDownloadTask5 extends AsyncTask<String, Void, Pair<Bitmap, Exception>> {

        private Pair<Bitmap, Exception> pair;

        @Override
        protected Pair<Bitmap, Exception> doInBackground(String... urls) {
            try {
                return Pair.create(downloadBitmap(urls[0]), null);
            } catch (Exception e) {
                return Pair.create(null, e);
            }
        }

        @Override
        protected void onPostExecute(Pair<Bitmap, Exception> result) {
            if (pair.second != null) { // (1) 시작
                image.setImageBitmap(null);
                Toast.makeText(AsyncTaskProblemActivity.this, "에러 발생",
                        Toast.LENGTH_LONG).show();
                return;
            } // (1)  끝
            image.setImageBitmap(pair.first);
        }

    }

    /**
     * RuntimeException을 던지면 크래시 발생
     */
    private class BitmapDownloadTask6 extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                return downloadBitmap(urls[0]);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                image.setImageBitmap(bitmap);
            }
        }

    }

    public void onClickButton2(View view) {
        new BitmapDownloadTask6().execute("http://suribada.com/profile.png");
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

    private Single<Bitmap> downloadBitmapSingle(String url) {
        return Single.create(emitter -> {
            try {
                emitter.onSuccess(downloadBitmap(url));
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    public void onClickButton3(View view) {
        downloadBitmapSingle("http://suribada.com/profile.png")
                .subscribeOn(Schedulers.io()) // (1)
                .observeOn(AndroidSchedulers.mainThread()) // (2)
                .subscribe(bitmap -> image.setImageBitmap(bitmap),
                        e -> { // (3) 시작
                            image.setImageBitmap(null);
                            Toast.makeText(AsyncTaskProblemActivity.this, "에러 발생",
                                    Toast.LENGTH_LONG).show();
                        }); // (3) 끝
    }

}

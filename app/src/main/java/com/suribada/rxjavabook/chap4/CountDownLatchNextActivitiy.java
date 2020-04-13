package com.suribada.rxjavabook.chap4;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.suribada.rxjavabook.R;
import com.suribada.rxjavabook.api.model.Book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by lia on 2018-04-17.
 */
public class CountDownLatchNextActivitiy extends Activity {

    private static final String TAG = "CountDownLatch";
    enum Type { BESTSELLER, RECOMMEND, CATEGORY};

    private Handler handler = new Handler();

    private TextView title, bestseller, recommend, category;
    private View bestsellerLayout, recommendLayout, categoryLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books_main);
        title = findViewById(R.id.title);
        bestsellerLayout = findViewById(R.id.bestseller_layout);
        recommendLayout = findViewById(R.id.recommend_layout);
        categoryLayout = findViewById(R.id.category_layout);
        bestseller = findViewById(R.id.bestseller);
        recommend = findViewById(R.id.recommend);
        category = findViewById(R.id.category);
    }

    private CountDownLatch latch;

    public void onClickButton(View view) {
        latch = new CountDownLatch(3);  // (1)
        new BestSellerAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new RecommendAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new CategoryAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 7);
        showCompletedMessage(); // (2)
    }

    private class BestSellerAsyncTask extends AsyncTask<Void, Void, List<Book>> {
        @Override
        protected List<Book> doInBackground(Void... voids) {
            try {
                return loadBooks(Type.BESTSELLER);
            } catch (Exception e) {
                Log.d(TAG, "error", e);
            }
            return Collections.emptyList();
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            Log.d(TAG, "first=" + System.currentTimeMillis());
            bestsellerLayout.setVisibility(View.VISIBLE);
            bestseller.setText(books.toString());
            latch.countDown(); // (3)
        }
    }

    private class RecommendAsyncTask extends AsyncTask<Void, Void, List<Book>> {
        @Override
        protected List<Book> doInBackground(Void... voids) {
            try {
                return loadBooks(Type.RECOMMEND);
            } catch (Exception e) {
                Log.d(TAG, "error", e);
            }
            return Collections.emptyList();
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            Log.d(TAG, "second=" + System.currentTimeMillis());
            recommendLayout.setVisibility(View.VISIBLE);
            recommend.setText(books.toString());
            latch.countDown(); // (3)
        }
    }

    private class CategoryAsyncTask extends AsyncTask<Integer, Void, List<Book>> {
        @Override
        protected List<Book> doInBackground(Integer... categories) {
            try {
                return loadBooks(Type.CATEGORY, categories[0]);
            } catch (Exception e) {
                Log.d(TAG, "error", e);
            }
            return Collections.emptyList();
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            Log.d(TAG, "third=" + System.currentTimeMillis());
//            if (books.size() == 0) {
//                latch.countDown();
//                return;
//            }
            categoryLayout.setVisibility(View.VISIBLE);
            category.setText(books.toString());
            latch.countDown();
        }
    }

    private List<Book> loadBooks(Type type, int... args) throws Exception {
        List<Book> books = new ArrayList<>();
        switch (type) {
            case BESTSELLER:
                SystemClock.sleep(5000);
                books.add(new Book(1, "채식주의자", "한강"));
                books.add(new Book(2, "죄와벌", "도스토옙스키"));
                break;
            case RECOMMEND:
                SystemClock.sleep(1000);
                books.add(new Book(1, "저수지의 개들", "멍멍이"));
                books.add(new Book(2, "안나 카레리나", "톨스토이"));
                break;
            case CATEGORY:
                SystemClock.sleep(3000);
                books.add(new Book(7, "시험에 나오는 안드로이드", "노재춘"));
                books.add(new Book(7, "발로 번역한 RxJava", "김인태"));
                break;
        }
        return books;
    }

    private void showCompletedMessage() {
        new Thread(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
            }
            handler.post(() -> {
                Toast.makeText(CountDownLatchNextActivitiy.this, "completed to load books", Toast.LENGTH_LONG).show();
            });
        }).start();
    }

    public void onClickButtonClear(View view) {
        bestsellerLayout.setVisibility(View.GONE);
        recommendLayout.setVisibility(View.GONE);
        categoryLayout.setVisibility(View.GONE);
    }

}


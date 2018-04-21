package com.suribada.rxjavabook.chap4;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.os.AsyncTaskCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.suribada.rxjavabook.R;
import com.suribada.rxjavabook.api.model.Book;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by lia on 2018-04-17.
 */
public class CountDownLatchActivitiy extends Activity {

    private static final String TAG = "CountDownLatch";
    enum Type { BESTSELLER, RECOMMEND, CATEGORY};

    private TextView title, bestseller, recommend, category;
    private View bestsellerLayout, recommendLayout, categoryLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books_main);
        title = (TextView) findViewById(R.id.title);
        bestsellerLayout = findViewById(R.id.bestseller_layout);
        recommendLayout = findViewById(R.id.recommend_layout);
        categoryLayout = findViewById(R.id.category_layout);
        bestseller = (TextView) findViewById(R.id.bestseller);
        recommend = (TextView) findViewById(R.id.recommend);
        category = (TextView) findViewById(R.id.category);
    }

    private CountDownLatch latch;

    public void onClickButton(View view) {
        latch = new CountDownLatch(2);  // (1)
        AsyncTaskCompat.executeParallel(new BestSellerAsyncTask());
        AsyncTaskCompat.executeParallel(new RecommendAsyncTask());
        AsyncTaskCompat.executeParallel(new CategoryAsyncTask(), 7);
        //showCompletedMessage();
    }

    private class BestSellerAsyncTask extends AsyncTask<Void, Void, List<Book>> {
        @Override
        protected List<Book> doInBackground(Void... voids) {
            try {
                return loadBooks(Type.BESTSELLER);
            } catch (Exception e) {
                Log.d(TAG, "error", e);
            }
            //latch.countDown();
            return Collections.emptyList();
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            Log.d(TAG, "first=" + System.currentTimeMillis());
            bestsellerLayout.setVisibility(View.VISIBLE);
            bestseller.setText(books.toString());
            latch.countDown(); // (2)
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
            //latch.countDown();
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
                return loadBooks(Type.RECOMMEND, categories[0]);
            } catch (Exception e) {
                Log.d(TAG, "error", e);
            } finally {
                try { // (4) 시작
                    latch.await();
                } catch (InterruptedException e) {
                } // (4) 끝
            }
            return Collections.emptyList();
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            Log.d(TAG, "third=" + System.currentTimeMillis());
            categoryLayout.setVisibility(View.VISIBLE);
            category.setText(books.toString());
            showCompletedMessage(); // (5)
            //latch.countDown();
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
        /*
        try {
            latch.await();
        } catch (InterruptedException e) {
        }
        */
        Toast.makeText(this, "completed to load books", Toast.LENGTH_LONG).show();
    }

    public void onClickButtonClear(View view) {
        bestsellerLayout.setVisibility(View.GONE);
        recommendLayout.setVisibility(View.GONE);
        categoryLayout.setVisibility(View.GONE);
    }
}


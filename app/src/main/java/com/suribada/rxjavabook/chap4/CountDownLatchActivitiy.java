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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by lia on 2018-04-17.
 */
public class CountDownLatchActivitiy extends Activity {

    private static final String TAG = "CountDownLatch";
    private static final int BESTSELLER = 1;
    private static final int RECOMMEND = 2;
    private static final int CATEGORY = 3;

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

    public void onClickButton1(View view) {
        latch = new CountDownLatch(2);
        AsyncTaskCompat.executeParallel(new BestSellerAsyncTask());
        AsyncTaskCompat.executeParallel(new RecommendAsyncTask());
        AsyncTaskCompat.executeParallel(new CategoryAsyncTask(), 7);
    }

    private class BestSellerAsyncTask extends AsyncTask<Void, Void, List<Book>> {

        @Override
        protected List<Book> doInBackground(Void... voids) {
            List<Book> books = new ArrayList<>();
            try {
                SystemClock.sleep(3000);
                books.add(new Book(1, "채식주의자", "한강"));
                books.add(new Book(2, "죄와벌", "도스토옙스키"));
            } catch (Exception e) {
                Log.d(TAG, "error", e);
            }
            return books;
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            bestsellerLayout.setVisibility(View.VISIBLE);
            bestseller.setText(books.toString());
        }
    }

    private class RecommendAsyncTask extends AsyncTask<Void, Void, List<Book>> {

        @Override
        protected List<Book> doInBackground(Void... voids) {
            List<Book> books = new ArrayList<>();
            try {
                SystemClock.sleep(1000);
                books.add(new Book(1, "저수지의 개들", "멍멍이"));
                books.add(new Book(2, "안나 카레리나", "톨스토이"));
            } catch (Exception e) {
                Log.d(TAG, "error", e);
            }
            return books;
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            recommendLayout.setVisibility(View.VISIBLE);
            recommend.setText(books.toString());
        }
    }

    private class CategoryAsyncTask extends AsyncTask<Integer, Void, List<Book>> {

        @Override
        protected List<Book> doInBackground(Integer... categories) {
            int categoryId = categories[0];
            List<Book> books = new ArrayList<>();
            try {
                SystemClock.sleep(5000);
                books.add(new Book(7, "시험에 나오는 안드로이드", "노재춘"));
                books.add(new Book(7, "발로 번역한 RxJava", "김인태"));
            } catch (Exception e) {
                Log.d(TAG, "error", e);
            }
            return books;
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            categoryLayout.setVisibility(View.VISIBLE);
            category.setText(books.toString());
        }
    }

    private void showCompletedMessage() {
        
    }

    public void onClickButton3(View view) {
        composedList.clear();
        title.setText(null);
        AsyncTaskCompat.executeParallel(new AsyncTaskA());
        AsyncTaskCompat.executeParallel(new AsyncTaskB());
    }

    private ArrayList<String> composedList = new ArrayList<>();

    private CountDownLatch latch2 = new CountDownLatch(1);

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
                Toast.makeText(CountDownLatchActivitiy.this, "Error=" + e.getMessage(), Toast.LENGTH_LONG).show();
            } finally {
                latch2.countDown();
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
                    latch2.await();
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


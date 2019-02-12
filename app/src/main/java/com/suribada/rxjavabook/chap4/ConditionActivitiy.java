package com.suribada.rxjavabook.chap4;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.suribada.rxjavabook.R;
import com.suribada.rxjavabook.api.model.Book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 결국 의미 없는 샘플이 됨
 *
 * Created by lia on 2018-04-17.
 */
public class ConditionActivitiy extends Activity {

    private static final String TAG = "ConditionActivitiy";
    enum Type { BESTSELLER, RECOMMEND, CATEGORY};

    private TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);
        title = findViewById(R.id.title);
    }

    private Lock lock =  new ReentrantLock();
    private Condition bestsellerLock = lock.newCondition();
    private Condition recommendLock = lock.newCondition();

    private ArrayList<Book> allBooks = new ArrayList<>();

    public void onClickButton(View view) {
        new BestSellerAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new RecommendAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new CategoryAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 7);
    }

    private class BestSellerAsyncTask extends AsyncTask<Void, Void, List<Book>> {
        @Override
        protected List<Book> doInBackground(Void... voids) {
            lock.lock();
            try {
                return loadBooks(Type.BESTSELLER);
            } catch (Exception e) {
                Log.d(TAG, "error", e);
            }
            bestsellerLock.signal();
            lock.unlock();
            return Collections.emptyList();
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            addAndShowBooks(books);
        }
    }

    private void addAndShowBooks(List<Book> books) {
        allBooks.addAll(books);
        title.setText(allBooks.toString());
    }

    private class RecommendAsyncTask extends AsyncTask<Void, Void, List<Book>> {
        @Override
        protected List<Book> doInBackground(Void... voids) {
            lock.lock();
            try {
                return loadBooks(Type.RECOMMEND);
            } catch (Exception e) {
                Log.d(TAG, "error", e);
            }
            try {
                bestsellerLock.await();
            } catch (InterruptedException e) {
            }
            recommendLock.signal();
            lock.unlock();
            return Collections.emptyList();
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            addAndShowBooks(books);
        }
    }

    private class CategoryAsyncTask extends AsyncTask<Integer, Void, List<Book>> {
        @Override
        protected List<Book> doInBackground(Integer... categories) {
            lock.lock();
            try {
                return loadBooks(Type.CATEGORY, categories[0]);
            } catch (Exception e) {
                Log.d(TAG, "error", e);
            }
            try {
                recommendLock.await();
            } catch (InterruptedException e) {
            }
            lock.unlock();
            return Collections.emptyList();
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            addAndShowBooks(books);
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
        Toast.makeText(ConditionActivitiy.this, "completed to load books", Toast.LENGTH_LONG).show();
    }

}


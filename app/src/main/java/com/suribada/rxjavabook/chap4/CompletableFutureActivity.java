package com.suribada.rxjavabook.chap4;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.suribada.rxjavabook.R;
import com.suribada.rxjavabook.api.model.Book;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by lia on 2018-04-17.
 */
public class CompletableFutureActivity extends Activity {

    private static final String TAG = "CountDownLatch";
    enum Type { BESTSELLER, RECOMMEND, CATEGORY};

    private Handler handler = new Handler();

    private TextView bestseller, recommend, category;
    private View bestsellerLayout, recommendLayout, categoryLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books_main);
        bestsellerLayout = findViewById(R.id.bestseller_layout);
        recommendLayout = findViewById(R.id.recommend_layout);
        categoryLayout = findViewById(R.id.category_layout);
        bestseller = (TextView) findViewById(R.id.bestseller);
        recommend = (TextView) findViewById(R.id.recommend);
        category = (TextView) findViewById(R.id.category);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onClickButton(View view) {
        CompletableFuture<Void> bestsellerFuture = getBestSellerBooks();
        CompletableFuture<Void> recommendFuture = getRecommendBooks();
        CompletableFuture<Void> categoryFuture = getCategoryBooks(7);

        List<CompletableFuture<Void>> completableFutures = Arrays.asList(bestsellerFuture,
                recommendFuture, categoryFuture);
        CompletableFuture
                .allOf(completableFutures.toArray(new CompletableFuture[3])) // (1)
                .thenAccept(ignored -> { // (2) 시작
                    handler.post(() -> {
                        Toast.makeText(CompletableFutureActivity.this,
                                "completed to load books", Toast.LENGTH_LONG).show();
                    });
                }); // (2) 끝
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<Void> getBestSellerBooks() {
        CompletableFuture<List<Book>> completableFuture = new CompletableFuture<>();
        new Thread(() -> {
            try {
                completableFuture.complete(loadBooks(Type.BESTSELLER)); // (3)
            } catch (Exception ex) {
                completableFuture.complete(Collections.emptyList()); // (4)
            }
        }).start();
        return completableFuture.thenAccept(books -> { // (5) 시작
            handler.post(() -> {
                bestsellerLayout.setVisibility(View.VISIBLE);
                bestseller.setText(books.toString());
            });
        }); // (5) 끝
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<Void> getRecommendBooks() {
        CompletableFuture<List<Book>> completableFuture = new CompletableFuture<>();
        new Thread(() -> {
            try {
                completableFuture.complete(loadBooks(Type.RECOMMEND));
            } catch (Exception ex) {
                completableFuture.complete(Collections.emptyList());
            }
        }).start();
        return completableFuture.thenAccept(books -> {
            handler.post(() -> {
                recommendLayout.setVisibility(View.VISIBLE);
                recommend.setText(books.toString());
            });
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<Void> getCategoryBooks(int categoryId) {
        CompletableFuture<List<Book>> completableFuture = new CompletableFuture<>();
        new Thread(() -> {
            try {
                completableFuture.complete(loadBooks(Type.CATEGORY, categoryId));
            } catch (Exception ex) {
                completableFuture.complete(Collections.emptyList());
            }
        }).start();
        return completableFuture.thenAccept(books -> {
            handler.post(() -> {
                categoryLayout.setVisibility(View.VISIBLE);
                category.setText(books.toString());
            });
        });
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

    public void onClickButtonClear(View view) {
        bestsellerLayout.setVisibility(View.GONE);
        recommendLayout.setVisibility(View.GONE);
        categoryLayout.setVisibility(View.GONE);
    }

}


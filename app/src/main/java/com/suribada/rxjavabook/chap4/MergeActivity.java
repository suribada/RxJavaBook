package com.suribada.rxjavabook.chap4;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.suribada.rxjavabook.R;
import com.suribada.rxjavabook.api.model.Book;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.suribada.rxjavabook.chap4.MergeActivity.Type.BESTSELLER;

/**
 * Created by lia on 2018-04-17.
 */
public class MergeActivity extends Activity {

    private static final String TAG = "CountDownLatch";
    enum Type { BESTSELLER, RECOMMEND, CATEGORY};

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

    public void onClickButton(View view) {
        Single.merge(getBestSellerBooks().map(books -> Pair.create(BESTSELLER, books))
                        .subscribeOn(Schedulers.io()),
                getRecommendBooks().map(books -> Pair.create(Type.RECOMMEND, books))
                        .subscribeOn(Schedulers.io()),
                getCategoryBooks(7).map(books -> Pair.create(Type.CATEGORY, books))
                        .subscribeOn(Schedulers.io()))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(pair -> {
                    switch (pair.first) {
                        case BESTSELLER:
                            bestsellerLayout.setVisibility(View.VISIBLE);
                            bestseller.setText(pair.second.toString());
                            break;
                        case RECOMMEND:
                            recommendLayout.setVisibility(View.VISIBLE);
                            recommend.setText(pair.second.toString());
                            break;
                        case CATEGORY:
                            categoryLayout.setVisibility(View.VISIBLE);
                            category.setText(pair.second.toString());
                            break;
                    }
                }, System.err::println,
                    () -> Toast.makeText(MergeActivity.this,
                            "completed to load books", Toast.LENGTH_LONG).show());
    }

    public Single<List<Book>> getBestSellerBooks() {
        return Single.create(emitter -> {
            try {
                emitter.onSuccess(loadBooks(BESTSELLER));
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    public Single<List<Book>> getRecommendBooks() {
        return Single.create(emitter -> {
            try {
                emitter.onSuccess(loadBooks(Type.RECOMMEND));
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    public Single<List<Book>> getCategoryBooks(int categoryId) {
        return Single.create(emitter -> {
            try {
                emitter.onSuccess(loadBooks(Type.CATEGORY, categoryId));
            } catch (Exception e) {
                emitter.onError(e);
            }
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


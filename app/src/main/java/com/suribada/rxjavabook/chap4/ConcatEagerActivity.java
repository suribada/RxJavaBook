package com.suribada.rxjavabook.chap4;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.suribada.rxjavabook.R;
import com.suribada.rxjavabook.api.model.Book;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lia on 2018-04-17.
 */
public class ConcatEagerActivity extends Activity {

    private static final String TAG = "ConcatEagerActivity";
    enum Type { BESTSELLER, RECOMMEND, CATEGORY }

    private TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);
        title = (TextView) findViewById(R.id.title);
    }

    public void onClickButton(View view) {
        List<Observable<List<Book>>> booksObservable = Arrays.asList(
                getBestSellerBooks().toObservable().subscribeOn(Schedulers.io()),
                getRecommendBooks().toObservable().subscribeOn(Schedulers.io()),
                getCategoryBooks(7).toObservable().subscribeOn(Schedulers.io()));
        Observable.concatEager(booksObservable)
            .scan((total, chunk) -> {
                total.addAll(chunk);
                return total;
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(books -> title.setText(books.toString()),
                    System.err::println,
                    () -> Toast.makeText(ConcatEagerActivity.this,
                            "completed to load books", Toast.LENGTH_LONG).show());
    }

    public Single<List<Book>> getBestSellerBooks() {
        return Single.create(emitter -> {
            try {
                emitter.onSuccess(loadBooks(Type.BESTSELLER));
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
                SystemClock.sleep(3000);
                books.add(new Book(1, "채식주의자", "한강"));
                books.add(new Book(2, "죄와벌", "도스토옙스키"));
                break;
            case RECOMMEND:
                SystemClock.sleep(1000);
                books.add(new Book(1, "저수지의 개들", "멍멍이"));
                books.add(new Book(2, "안나 카레리나", "톨스토이"));
                break;
            case CATEGORY:
                SystemClock.sleep(5000);
                books.add(new Book(7, "시험에 나오는 안드로이드", "노재춘"));
                books.add(new Book(7, "발로 번역한 RxJava", "김인태"));
                break;
        }
        return books;
    }

}


package com.suribada.rxjavabook.chap6;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.stetho.Stetho;
import com.suribada.rxjavabook.R;
import com.suribada.rxjavabook.api.BookSampleRepository;

import java.util.ArrayList;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Created by lia on 2018-04-09.
 */
public class BookApiCallActivity extends Activity {

    private static final String TAG  = "BookApiCall";

    private BookSampleRepository repository = new BookSampleRepository();

    private TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.four_buttons);
        title = findViewById(R.id.title);
        Stetho.initializeWithDefaults(this);
    }

    public void onClickButton1(View view) {
        Single.merge(repository.getBestSeller(),
                    repository.getRecommendBooks(),
                    repository.getCategoryBooks(7))
                .subscribeOn(Schedulers.io())
                .collect(ArrayList::new, ArrayList::addAll)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(books -> title.setText(books.toString()));
    }

    /**
     * 예제를 위해 Single을 Observable로 변경한 것
     * @param view
     */
    public void onClickButton1_Observable(View view) {
        Observable.merge(repository.getBestSellerObservable(),
                repository.getRecommendBooksObservable(),
                repository.getCategoryBooksObservable(7))
                .subscribeOn(Schedulers.io()) // (1)
                .collect(ArrayList::new, ArrayList::addAll)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(books -> title.setText(books.toString()),
                        e -> Log.d(TAG, "error", e));
    }

    public void onClickButton2(View view) {
        Single.merge(repository.getBestSeller().subscribeOn(Schedulers.io()), // (1)
                    repository.getRecommendBooks().subscribeOn(Schedulers.io()), // (2)
                    repository.getCategoryBooks(7).subscribeOn(Schedulers.io())) // (3)
                .collect(ArrayList::new, ArrayList::addAll) // (4)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(books -> title.setText(books.toString()),
                        e -> Log.d(TAG, "error", e));
    }

    public void onClickButton2_Observable(View view) {
        Observable.merge(repository.getBestSellerObservable().subscribeOn(Schedulers.io()), // (1)
                repository.getRecommendBooksObservable().subscribeOn(Schedulers.io()), // (2)
                repository.getCategoryBooksObservable(7).subscribeOn(Schedulers.io())) // (3)
                .collect(ArrayList::new, ArrayList::addAll)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(books -> title.setText(books.toString()),
                        e -> Log.d(TAG, "error", e));
    }

    public void onClickButton3(View view) {
        repository.getBookCategories().toObservable()
                .flatMapIterable(categories -> categories)
                .flatMapSingle(category -> repository.getCategoryBooks(category.categoryId))
                .subscribeOn(Schedulers.io())
                .collect(ArrayList::new, ArrayList::addAll)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(books -> title.setText(books.toString()));
    }

    public void onClickButton3_Observable(View view) {
        repository.getBookCategoriesObservable()
                .flatMapIterable(categories -> categories)
                .flatMap(category -> repository.getCategoryBooksObservable(category.categoryId))
                .subscribeOn(Schedulers.io()) // (1)
                .collect(ArrayList::new, ArrayList::addAll)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(books -> title.setText(books.toString()),
                        e -> Log.d(TAG, "error", e));
    }

    public void onClickButton4(View view) {
        repository.getBookCategories().toObservable()
                .subscribeOn(Schedulers.io()) // (1)
                .flatMapIterable(categories -> categories)
                .flatMapSingle(category ->
                        repository.getCategoryBooks(category.categoryId).subscribeOn(Schedulers.io())) // (2)
                .collect(ArrayList::new, ArrayList::addAll)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(books -> title.setText(books.toString()),
                        e -> Log.d(TAG, "error", e));
    }

    public void onClickButton4_Observable(View view) {
        repository.getBookCategoriesObservable()
                .subscribeOn(Schedulers.io()) // (1)
                .flatMapIterable(categories -> categories)
                .flatMap(category -> repository.getCategoryBooksObservable(category.categoryId)
                        .subscribeOn(Schedulers.io())) // (2)
                .collect(ArrayList::new, ArrayList::addAll)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(books -> title.setText(books.toString()),
                        e -> Log.d(TAG, "error", e));
    }

}

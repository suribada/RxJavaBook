package com.suribada.rxjavabook.chap5;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import android.view.View;

import com.suribada.rxjavabook.R;

import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.disposables.SerialDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SerialDisposableActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_three_buttons);
    }

    private int i = 0;

    public void onClickButton1(View view) {
        search("keyword" + (i++));
    }

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private void search(String keyword) {
        compositeDisposable.add(searchApi(keyword) // (1)
                .subscribeOn(Schedulers.io())
                .subscribe(System.out::println));
    }

    private Disposable searchDisposable;

    private void searchRecent(String keyword) {
        if (searchDisposable != null && !searchDisposable.isDisposed()) { // (1)
            searchDisposable.dispose();
        } // (1) 끝
        searchDisposable = searchApi(keyword) // (2)
                .subscribeOn(Schedulers.io())
                .subscribe(System.out::println);
    }

    public void onClickButton2(View view) {
        searchRecent("keyword" + (i++));
    }

    private SerialDisposable serialDisposable = new SerialDisposable();

    private void searchRecent2(String keyword) {
        serialDisposable.set(searchApi(keyword) // (1)
                .subscribeOn(Schedulers.io())
                .subscribe(System.out::println));
    }

    public void onClickButton3(View view) {
        searchRecent2("keyword" + (i++));
    }


    public void wrongCompositeDisposable() {
        List<String> keywords = Arrays.asList("김", "노", "박");
        Observable.fromIterable(keywords)
                .filter(keyword -> keyword.length() > 0)
                .subscribe(keyword -> {
                    compositeDisposable.add(searchApi(keyword)
                            .subscribeOn(Schedulers.io())
                            .subscribe(System.out::println));
                });
        compositeDisposable.add(Observable.fromIterable(keywords)
                .filter(keyword -> keyword.length() > 0)
                .flatMap(keyword -> searchApi(keyword)
                            .subscribeOn(Schedulers.io()))
                .subscribe(System.out::println));
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose(); // (2)
        searchDisposable.dispose();
        serialDisposable.dispose();
        super.onDestroy();
    }

    private Observable<SearchResult> searchApi(String keyword) {
        return Observable.create(emitter -> {
            SystemClock.sleep(3000);
            emitter.onNext(new SearchResult(keyword));
        });
    }

    private class SearchResult {
        String result;

        public SearchResult(String keyword) {
            this.result = keyword + "_result";
        }

        @Override
        public String toString() {
            return result;
        }
    }

}

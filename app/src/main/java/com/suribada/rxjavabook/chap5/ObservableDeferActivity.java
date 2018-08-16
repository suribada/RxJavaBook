package com.suribada.rxjavabook.chap5;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.suribada.rxjavabook.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Noh.Jaechun on 2018. 8. 15..
 */
public class ObservableDeferActivity extends Activity {

    private Disposable timeDisposable;
    private TextView title, time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_two_buttons);
        title = findViewById(R.id.title);
        time = findViewById(R.id.time);
        timeDisposable = Observable.interval(0,1, TimeUnit.SECONDS)
                .takeUntil(searchObs) // (1) 그 순간에 subscribe 되기 때문에 아래로 전달되지 않는다. takeUntil은 Hot Observable에 적합?
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> time.setText("elapsed=" + value));
    }

    @Override
    protected void onDestroy() {
        if (!timeDisposable.isDisposed()) {
            timeDisposable.dispose();
        }
        super.onDestroy();
    }

    public void onClickButton1(View view) {
        keyword = "곰젤리";
        search();
    }

    private String keyword;

    private Observable<SearchResult> searchObs = Observable.defer(() -> {
        return Observable.create(emitter -> {
            emitter.onNext(new SearchResult(keyword));
            emitter.onComplete();
        });
    });

    private void search() {
        searchObs.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(searchResult -> title.setText(searchResult.toString()));
    }

    private void search2() {
        Observable.create(emitter -> { // (1) 시작
            emitter.onNext(new SearchResult(keyword));
            emitter.onComplete();
        }) // (1) 끝
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(searchResult -> title.setText(searchResult.toString()));
    }

    private Observable<SearchResult> searchRx(String keyword) {
        return Observable.just(new SearchResult(keyword));
    }

}

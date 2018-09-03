package com.suribada.rxjavabook.chap5;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.suribada.rxjavabook.R;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Noh.Jaechun on 2018. 9. 4..
 */
public class SearchActivity2 extends Activity {

    private EditText searchText;
    private Button search, weatherKeyword, carKeyword, rxjavaKeyword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_subject);
        searchText = findViewById(R.id.search_text);
        search = findViewById(R.id.search_btn);
        weatherKeyword = findViewById(R.id.weather);
        rxjavaKeyword = findViewById(R.id.rxjava);
        carKeyword = findViewById(R.id.search_btn);
        Observable.merge(RxView.clicks(search).map(ignored -> searchText.getText().toString()),
            RxView.clicks(weatherKeyword).map(ignored -> "날씨"),
            RxView.clicks(carKeyword).map(ignored -> "자동차"),
            RxView.clicks(rxjavaKeyword).map(ignored -> "RxJava")
        ).subscribe(keyword -> searchKeyword(keyword));
    }

    private void searchKeyword(String keyword) {
        Toast.makeText(this, "keyword=" + keyword, Toast.LENGTH_LONG).show();
    }

}

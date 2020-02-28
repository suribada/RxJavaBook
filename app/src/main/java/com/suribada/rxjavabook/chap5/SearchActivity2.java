package com.suribada.rxjavabook.chap5;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.suribada.rxjavabook.R;

import io.reactivex.rxjava3.core.Observable;

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
        carKeyword = findViewById(R.id.car);
        rxjavaKeyword = findViewById(R.id.rxjava);
        Observable.merge(
            RxView.clicks(search).map(ignored -> searchText.getText().toString()),
            RxView.clicks(weatherKeyword).map(ignored -> "날씨"),
            RxView.clicks(carKeyword).map(ignored -> "자동차"),
            RxView.clicks(rxjavaKeyword).map(ignored -> "RxJava")
        ).subscribe(keyword -> searchKeyword(keyword));
    }

    private void searchKeyword(String keyword) {
        Toast.makeText(this, "keyword=" + keyword, Toast.LENGTH_LONG).show();
    }

}

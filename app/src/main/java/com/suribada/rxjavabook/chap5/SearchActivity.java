package com.suribada.rxjavabook.chap5;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.suribada.rxjavabook.R;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Noh.Jaechun on 2018. 9. 4..
 */
public class SearchActivity extends Activity {

    private EditText searchText;

    private Subject<String> keywordSubject = PublishSubject.create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_subject);
        searchText = findViewById(R.id.search_text);
        keywordSubject.subscribe(keyword -> searchKeyword(keyword)); // (1)
    }

    public void onClickSearch(View view) { // (2) 시작
        keywordSubject.onNext(searchText.getText().toString());
    } // (2) 끝

    public void onClickWeather(View view) { // (3) 시작
        keywordSubject.onNext("날씨");
    }

    public void onClickCar(View view) {
        keywordSubject.onNext("자동차");
    }

    public void onClickRxJava(View view) {
        keywordSubject.onNext("RxJava");
    } // (3) 끝

    private void searchKeyword(String keyword) {
        Toast.makeText(this, "keyword=" + keyword, Toast.LENGTH_LONG).show();
    }

}

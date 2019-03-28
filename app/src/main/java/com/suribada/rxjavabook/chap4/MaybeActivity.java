package com.suribada.rxjavabook.chap4;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.json.JSONObject;

import com.suribada.rxjavabook.R;

import io.reactivex.Maybe;

public class MaybeActivity extends Activity {

    private String text = "{\"best\":\"RxJava\", \"good\":\"Android\"}";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_three_buttons);
    }

    public void onClickButton1(View view) throws Exception {
        JSONObject jsonObject = new JSONObject(text);
        JSONObject bookJson = jsonObject.optJSONObject("best");
        String book = null;
        if (bookJson != null) {
            book = jsonObject.optString("title");
            if (book == null) {
                book = jsonObject.optString("default_book");
            }
        } else {
            book = jsonObject.optString("default_book");
        }
        System.out.println("book=" + book);
    }

    public void onClickButton2(View view) throws Exception {
        JSONObject jsonObject = new JSONObject(text);
        String book = Maybe.fromCallable(() -> jsonObject.optJSONObject("best"))
                .map(json -> json.optString("title"))
                .blockingGet(jsonObject.optString("default_book"));
        System.out.println("book=" + book);
    }

    public void onClickButton3(View view) throws Exception {
        JSONObject jsonObject = new JSONObject(text);
        String book = Maybe.fromCallable(() -> jsonObject.optJSONObject("best"))
                .map(json -> json.optString("title"))
                .concatWith(Maybe.fromCallable(() -> jsonObject.optString("default_book")))
                .firstElement().blockingGet();
        System.out.println("book=" + book);
    }
}

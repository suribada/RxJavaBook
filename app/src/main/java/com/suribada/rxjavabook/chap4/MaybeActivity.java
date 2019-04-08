package com.suribada.rxjavabook.chap4;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import com.suribada.rxjavabook.R;

import io.reactivex.Maybe;

public class MaybeActivity extends Activity {

    private String text = "{\"no_best\":\"RxJava\", \"no_good\":\"Android\"}";
    private String text2 = "{\"best\":\"RxJava\", \"no_good\":\"Android\"}";
    private String text3 = "{\"no_best\":\"RxJava\", \"good\":\"Android\"}";
    private String text4 = "{\"best\":\"RxJava\", \"good\":\"Android\"}";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_three_buttons);
    }

    public void onClickButton1(View view) throws Exception {
        legacy(text);
        legacy(text2);
        legacy(text3);
        legacy(text4);
    }

    private void legacy(String input) throws JSONException {
        JSONObject jsonObject = new JSONObject(input);
        JSONObject bookJson = jsonObject.optJSONObject("best");
        String book = null;
        if (bookJson != null) {
            book = jsonObject.optString("title");
            if (book == null) {
                book = jsonObject.optString("author");
            }
        } else {
            book = jsonObject.optString("good");
        }
        System.out.println("book=" + book);
    }

    public void onClickButton2(View view) throws Exception {
       supposeNotNull(text);
       supposeNotNull(text2);
       supposeNotNull(text3);
       supposeNotNull(text4);
    }

    private void supposeNotNull(String input) throws JSONException {
        JSONObject jsonObject = new JSONObject(input);
        String book = Maybe.fromCallable(() -> jsonObject.optJSONObject("best"))
                .map(json -> json.optString("title"))
                .blockingGet(jsonObject.optString("good"));
        System.out.println("book=" + book);
    }

    public void onClickButton3(View view) throws Exception {
        acceptNullable(text);
        acceptNullable(text2);
        acceptNullable(text3);
        acceptNullable(text4);
    }

    private void acceptNullable(String input) throws JSONException {
        JSONObject jsonObject = new JSONObject(input);
        String book = Maybe.fromCallable(() -> jsonObject.optJSONObject("best"))
                .map(json -> json.optString("title"))
                .concatWith(Maybe.fromCallable(() -> jsonObject.optString("default_book")))
                .firstElement().blockingGet();
        System.out.println("book=" + book);
    }
}

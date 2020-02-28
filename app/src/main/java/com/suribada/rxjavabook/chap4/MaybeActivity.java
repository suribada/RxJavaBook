package com.suribada.rxjavabook.chap4;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import com.suribada.rxjavabook.R;

import io.reactivex.rxjava3.core.Maybe;

public class MaybeActivity extends Activity {

    private String text = "{\"best\":{\"book\": \"RxJava\"}, \"no_good\":\"Android\"}";
    private String text2 = "{\"no_best\":\"RxJava\", \"good\":\"Android\"}";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.four_buttons);
    }

    public void onClickButton1(View view) throws Exception {
        legacy(text);
        legacy(text2);
    }

    public void onClickButton2(View view) throws Exception {
        legacy2(text);
        legacy2(text2);
    }

    private String getDefaultBook() {
        return null;
    }

    private void legacy(String input) throws JSONException {
        JSONObject jsonObject = new JSONObject(input);
        String book = null;
        JSONObject bestJson = jsonObject.optJSONObject("best");
        if (bestJson != null) { // (1)
            book = bestJson.optString("book");
            if (book == null) { // (2)
                book = getDefaultBook(); // (3)
            }
        } else {
            book = getDefaultBook(); // (4)
        }
        System.out.println("book=" + book);
    }

    private void legacy2(String input) throws JSONException {
        JSONObject jsonObject = new JSONObject(input);
        String book = null;
        JSONObject bestJson = jsonObject.optJSONObject("best");
        if (bestJson != null) {
            book = bestJson.optString("book");
        }
        if (book == null) {
            book = getDefaultBook();
        }
        System.out.println("book=" + book);
    }

    public void onClickButton3(View view) throws Exception {
        // getDefaultBook이 null을 리턴하면 크래시
       supposeNotNull(text);
       supposeNotNull(text2);
    }

    private void supposeNotNull(String input) throws JSONException {
        JSONObject jsonObject = new JSONObject(input);
        String book = Maybe.fromCallable(() -> jsonObject.optJSONObject("best")) // (1)
                .map(json -> json.optString("book")) // (2)
                .blockingGet(getDefaultBook()); // (3)
        System.out.println("book=" + book);
    }

    public void onClickButton4(View view) throws Exception {
        acceptNullable(text);
        acceptNullable(text2);
    }

    private void acceptNullable(String input) throws JSONException {
        JSONObject jsonObject = new JSONObject(input);
        String book = Maybe.fromCallable(() -> jsonObject.optJSONObject("best"))
                .map(json -> json.optString("book"))
                .switchIfEmpty(Maybe.fromCallable(() -> getDefaultBook()))// (1)
                .blockingGet(); // (2)
        /*
        String book = Maybe.fromCallable(() -> jsonObject.optJSONObject("best"))
                .map(json -> json.optString("book"))
                .concatWith(Maybe.fromCallable(() -> getDefaultBook()))
                .firstElement().blockingGet();
                */
        System.out.println("book=" + book);
    }
}

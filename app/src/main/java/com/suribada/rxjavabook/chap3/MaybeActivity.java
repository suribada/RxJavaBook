package com.suribada.rxjavabook.chap3;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import com.suribada.rxjavabook.R;

import org.json.JSONObject;

import io.reactivex.rxjava3.core.Maybe;

public class MaybeActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);
    }

    public void onClickButton(View view) throws Exception {
        JSONObject jsonObject = new JSONObject("{\"name\":\"노재춘\"}");
        Maybe.fromCallable(() -> jsonObject.optString("name")) // (1)
                .subscribe(System.out::println,
                        System.err::println);
        Maybe.fromCallable(() -> jsonObject.optString("nickname")) // (1)
                .subscribe(System.out::println,
                        System.err::println);

    }
}

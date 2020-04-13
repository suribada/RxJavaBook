package com.suribada.rxjavabook.chap7;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.suribada.rxjavabook.R;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DebounceActivity extends Activity {

    private EditText searchText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_subject);
        searchText = findViewById(R.id.search_text);
        getSearchTextObservable()
                .debounce(1, TimeUnit.SECONDS) // (1)
                .distinctUntilChanged() // (2)
                .flatMap(keyword -> getAutoCompleteList(keyword) // (3)
                        .subscribeOn(Schedulers.io())
                        .onErrorComplete()) // (4)
                .subscribe(System.out::println, System.err::println);
    }

    private Observable<String> getSearchTextObservable() {
        return Observable.create(emitter -> {
            searchText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    System.out.println("change=" + charSequence);
                    emitter.onNext(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        });
    }

    private Observable<CharSequence> getAutoCompleteList(CharSequence charSequence) {
        return Observable.create(emitter -> {
            Random random = new Random();
            int value = random.nextInt(3);
            System.out.println("value=" + value);
            if (value < 1) {
                emitter.onNext("result=" + charSequence);
            } else {
                emitter.onError(new IndexOutOfBoundsException());
            }
        });
    }
}

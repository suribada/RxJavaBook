package com.suribada.rxjavabook.chap7;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.jakewharton.rxbinding3.widget.RxTextView;
import com.suribada.rxjavabook.R;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

//TODO rxjava3 버전으로 변경
public class RxBindingDebounceActivity extends Activity {

    private EditText searchText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_subject);
        searchText = findViewById(R.id.search_text);
        RxTextView.textChanges(searchText)
                .map(CharSequence::toString)
                .debounce(1, TimeUnit.SECONDS)
                .distinctUntilChanged()
                .flatMap(keyword -> getAutoCompleteList(keyword)
                        .subscribeOn(Schedulers.io())
                        .onErrorReturnItem("000")
                )
                .subscribe(System.out::println, System.err::println);
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

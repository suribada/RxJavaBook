package com.suribada.rxjavabook.chap1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.jakewharton.rxbinding4.widget.RxTextView;
import com.suribada.rxjavabook.R;

import io.reactivex.rxjava3.core.Observable;

public class PlusActivity extends Activity {

    private EditText first, second;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        first = findViewById(R.id.first);
        second = findViewById(R.id.second);
        Observable.combineLatest(RxTextView.textChanges(first), RxTextView.textChanges(second),
                (x, y) -> Integer.valueOf(x.toString()) + Integer.parseInt(y.toString()))
                .subscribe(sum -> System.out.println("sum=" + sum));
    }


}

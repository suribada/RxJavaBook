package com.suribada.rxjavabook.chap8;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.suribada.rxjavabook.R;

import io.reactivex.Observable;

/**
 * Created by lia on 2018-05-01.
 */
public class CustomActivity extends Activity {

    Observable<State> stateObservable = Observable.create(emitter -> {
        emitter.onNext(new State.LoadingState());
        emitter.onNext(new State.DataState("suribada"));
        emitter.onNext(new State.DataState("horseridingking"));
        emitter.onNext(new State.ErrorState(new IllegalArgumentException("not allowed")));
    });

    private TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_three_buttons);
        title = findViewById(R.id.title);
    }

    public void onClickButton1(View view) {
        stateObservable.filter(value -> value instanceof State.LoadingState)
                .cast(State.LoadingState.class)
                .subscribe(state -> showText(state.toString()));
    }

    private void showText(String input) {
        title.setText(input);
    }

    public void onClickButton2(View view) {
        stateObservable.to(Filters.cast(State.LoadingState.class))
                .subscribe(state -> showText(state.toString()));
        stateObservable.to(Filters.cast(State.DataState.class))
                .subscribe(System.out::println);
    }

    public void onClickButton3(View view) {
        showText("");
    }

}

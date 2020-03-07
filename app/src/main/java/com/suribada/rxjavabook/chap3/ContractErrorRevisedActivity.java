package com.suribada.rxjavabook.chap3;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//TODO
//import com.jakewharton.rxbinding2.view.RxView;
import com.suribada.rxjavabook.R;
import com.suribada.rxjavabook.model.ViewState;

import java.util.Random;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observables.ConnectableObservable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Created by lia on 2018-03-21.
 */
public class ContractErrorRevisedActivity extends Activity {

    private TextView title;
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);
        title = findViewById(R.id.title);
        button = findViewById(R.id.button);
        /* TODO
        RxView.clicks(button)
                .flatMap(ignored -> getBestSeller()
                        .subscribeOn(Schedulers.io())
                        .map(title -> new ViewState.Result(title)) // (1)
                        .cast(ViewState.class) // (2)
                        .onErrorReturn(e -> new ViewState.Error(e))) // (3)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(viewState -> {
                    if (viewState instanceof ViewState.Result) { // (4)
                        title.setText(((ViewState.Result) viewState).title);
                    } else if (viewState instanceof ViewState.Error) { // (5)
                        Toast.makeText(this, "문제 발생", Toast.LENGTH_LONG).show();
                    }
                });
         */
    }

    private Observable<String> getBestSeller() {
        Random random = new Random();
        int val = random.nextInt(5);
        return Observable.fromCallable(() -> "title " + 10 / val);
    }

    private void ofTypeOperator() {
        /* TODO
        ConnectableObservable<ViewState> viewStateObservable = RxView.clicks(button)
                .flatMap(ignored -> getBestSeller()
                        .subscribeOn(Schedulers.io())
                        .map(title -> new ViewState.Result(title))
                        .cast(ViewState.class)
                        .onErrorReturn(e -> new ViewState.Error(e)))
                .publish(); // (1)
        viewStateObservable.ofType(ViewState.Result.class) // (2)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(state -> title.setText(state.title)); // (3)
        viewStateObservable.ofType(ViewState.Error.class) // (4)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(error -> {
                    Toast.makeText(this, "문제 발생", Toast.LENGTH_LONG).show();
                });
        Disposable disposable = viewStateObservable.connect(); // (5)
         */
    }

}

package com.suribada.rxjavabook.chap3;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.suribada.rxjavabook.R;

import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
        title = (TextView) findViewById(R.id.title);
        button = (Button) findViewById(R.id.button);
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
    }

    private Observable<String> getBestSeller() {
        Random random = new Random();
        int val = random.nextInt(5);
        return Observable.fromCallable(() -> "title " + 10 / val);
    }

    interface ViewState {
        class Error implements ViewState {
            Throwable e;

            Error(Throwable e) {
                this.e = e;
            }
        }

        class Result implements ViewState {
            String title;

            Result(String title) {
                this.title = title;
            }
        }
    }

}

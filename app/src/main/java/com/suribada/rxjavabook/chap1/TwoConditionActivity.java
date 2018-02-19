package com.suribada.rxjavabook.chap1;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.suribada.rxjavabook.R;

import io.reactivex.Completable;

/**
 * Created by lia on 2018-02-05.
 */

public class TwoConditionActivity extends Activity {

    private Button button;
    private boolean conditionA, conditionB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);
        button = (Button) findViewById(R.id.button);
        button.setEnabled(false);

        initializeA(new Callback() {
            @Override
            public void onSuccess() {
                //...
                conditionA = true;
                enableButton();
            }

        });
        initializeB(new Callback() {
            @Override
            public void onSuccess() {
                //..,
                conditionB = true;
                enableButton();
            }

        });
        Completable.mergeArray(initializeA(), initializeB())
                .subscribe(() -> {
                    button.setEnabled(true);
                });
    }

    private void enableButton() {
        if (conditionA && conditionB) {
            button.setEnabled(true);
        }
    }

    private void initializeA(Callback callback) {
    }

    private void initializeB(Callback callback) {
    }

    private Completable initializeA() {
        return Completable.fromAction(() -> {
            //...
        });
    }

    private Completable initializeB() {
        return Completable.fromAction(() -> {
            //...
        });
    }

    interface Callback {
        void onSuccess();
    }
}

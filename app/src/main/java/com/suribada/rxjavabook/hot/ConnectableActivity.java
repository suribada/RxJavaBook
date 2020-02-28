package com.suribada.rxjavabook.hot;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import com.suribada.rxjavabook.R;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.ConnectableObservable;

/**
 * Created by lia on 2017-10-17.
 */

public class ConnectableActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_two_buttons);
    }

    private ConnectableObservable<Integer> threeRandoms =
            Observable.interval(1, TimeUnit.SECONDS)
            //Observable.range(1, 3)
                    .map(i -> randomInt()).publish();

    public void onClickButton1(View view) {
        threeRandoms.subscribe(i -> System.out.println("Observer 1: " + i));

        threeRandoms.scan(0, (total, next) -> total + next)
                .subscribe(i -> System.out.println("Observer 2: " + i));

        threeRandoms.connect();
    }

    public void onClickButton2(View view) {
        threeRandoms.subscribe(i -> System.out.println("Observer3: " + i));
    }

    public static int randomInt() {
        return ThreadLocalRandom.current().nextInt(100000);
    }

}

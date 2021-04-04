package com.suribada.rxjavabook.chap1;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.suribada.rxjavabook.R;

import io.reactivex.rxjava3.core.Observable;

/**
 * Created by lia on 2018-01-21.
 */
public class ObserverActivity extends Activity {

    private static final String TAG = "ObserverActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);
    }

    public void onClickButton(View view) {
        Observable<Integer> source = Observable.just(1, 2, 4, 7, 8, 11, 14); // (1)

        Observable<Integer> obs1 = source.filter(x -> x % 2 == 0);
        obs1.subscribe(System.out::println); // (2)

        Observable<Integer> obs2 = obs1.map(x -> x * 100);
        obs2.subscribe(System.out::println); // (3)

        Observable<Integer> obs3 = obs2.filter(x -> x <= 1000)
                .map(x -> findNearPrime(x));
        obs3.subscribe(System.out::println); // (4)

        source.filter(x -> x % 2 == 0)
                .map(x -> x * 100)
                .filter(x -> x <= 1000)
                .map(x -> findNearPrime(x))
                .subscribe(System.out::println);

        Observable.just(1, 2, 4, 7, 8, 11, 14)  // (1)
                .filter(x -> x % 2 == 0) // (2)
                .map(x -> x * 100) // (3)
                .filter(x -> x <= 1000) // (4)
                .map(x -> findNearPrime(x)) // (5)
                .subscribe(System.out::println);
    }

    private int findNearPrime(Integer x) {
        return 997; // for example
    }

}

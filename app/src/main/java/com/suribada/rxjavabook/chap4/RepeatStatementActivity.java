package com.suribada.rxjavabook.chap4;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import com.suribada.rxjavabook.R;

import io.reactivex.rxjava3.core.Observable;

/**
 * Created by lia on 2018-05-30.
 */

public class RepeatStatementActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);
    }

    public void onClickButton(View view) {
        testRanges();
        testRangesRx();
    }

    private void testRanges() {
        long current = System.currentTimeMillis();
        for (int i = 0; i <= 9; i++) {
            if (i % 2 == 0) {
                continue;
            }
            for (int j = 0; j <= 9 ; j++) {
                int j2 = j * 10; // (1) 시작
                if (j == 1 || j == 3) {
                    j2 = j * 100;
                } // (1) 끝
                for (int k = 0; k <= 9 ; k++) {
                    if (k % 3 == 0) {
                        continue;
                    }
                    System.out.println(i + j2 + k * 1000);
                }
            }
        }
        System.out.println("elapsed 1=" + (System.currentTimeMillis() - current));
    }

    private void testRangesRx() {
        long current = System.currentTimeMillis();
        Observable.range(0, 10)
                .filter(x -> x % 2 > 0)
                .flatMap(x ->
                        Observable.range(0, 10).map(y -> {
                            if (y == 1 || y == 3) { // (1) 시작
                                return y * 100;
                            } else {
                                return y * 10;
                            } // (1) 끝
                        }).map(y -> x + y))
                .flatMap(value -> Observable.range(0, 10).filter(z -> z % 3 > 0)
                        .map(z -> value + z * 1000))
                .subscribe(System.out::println);
        System.out.println("elapsed 2=" + (System.currentTimeMillis() - current));
    }
}

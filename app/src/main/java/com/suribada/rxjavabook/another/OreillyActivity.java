package com.suribada.rxjavabook.another;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import com.suribada.rxjavabook.R;

import java.math.BigDecimal;

import io.reactivex.rxjava3.core.Observable;

/**
 * Created by lia on 2018-01-08.
 */

public class OreillyActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.many_buttons);
    }

    public void onClickButton1(View view) {
        Observable.just(new BigDecimal(1), new BigDecimal(2), new BigDecimal(3),
                    new BigDecimal(4), new BigDecimal(5))
                .reduce(BigDecimal.ZERO, (total, val) -> total.add(val))
                .subscribe(System.out::println);
        Observable.just(new BigDecimal(1), new BigDecimal(2), new BigDecimal(3),
                new BigDecimal(4), new BigDecimal(5))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .subscribe(System.out::println);
    }
}

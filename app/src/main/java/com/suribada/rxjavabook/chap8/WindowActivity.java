package com.suribada.rxjavabook.chap8;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.jakewharton.rxbinding4.view.RxView;
import com.suribada.rxjavabook.R;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;

public class WindowActivity extends Activity {

    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);
        button = findViewById(R.id.button);
        Observable.interval(5, TimeUnit.SECONDS)
                .map(ignored -> ThreadLocalRandom.current().nextInt(10, 20)) // (1)
                .doOnNext(v -> System.out.println(System.currentTimeMillis() + " next= " + v)) // (2)
                .window(RxView.clicks(button)) // (3)
                .flatMapSingle(obs -> obs.reduce(0L, (total, next) -> total + next)) // (4)
                .subscribe(System.out::println);
    }

    /**
     * buffer 연산자를 쓴 거나 똑같다..
     */
    private void another() {
        Observable.interval(5, TimeUnit.SECONDS)
                .map(ignored -> ThreadLocalRandom.current().nextInt(10, 20))
                .window(RxView.clicks(button))
                .flatMapSingle(obs -> obs.toList())
                .map(integers -> {
                    if (integers.size() == 0) {
                        return "no value";
                    }
                    if (integers.size() == 1) {
                        return integers.get(0);
                    }
                    StringBuilder sb = new StringBuilder();
                    int sum = 0;
                    for (int each : integers) {
                        sb.append(each + " + ");
                        sum += each;
                    }
                    sb.replace(sb.length() - 3, sb.length(), " = ");
                    sb.append(sum);
                    return sb.toString();
                }).subscribe(System.out::println);

    }

}

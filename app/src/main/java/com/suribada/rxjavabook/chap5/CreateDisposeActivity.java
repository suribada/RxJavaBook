package com.suribada.rxjavabook.chap5;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.suribada.rxjavabook.R;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Noh.Jaechun on 2018. 8. 17..
 */
public class CreateDisposeActivity extends Activity {

    private TextView elapsed, randomNumbers;
    private Disposable elapsedDisposable, generateDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_random);
        elapsed = findViewById(R.id.elapsed);
        //....
        randomNumbers = findViewById(R.id.random_numbers);
        elapsedDisposable = Observable.interval(1, TimeUnit.SECONDS) // (1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(time -> elapsed.setText("elapsed time=" + time));
    }

    private Random random = new Random();

    public void onClickGenerate1(View view) {
        generateDisposable = Observable.<int[]>create(emitter -> { // (2)
            emitter.setDisposable(elapsedDisposable); // (3)
            while (!emitter.isDisposed()) {
                int[] numbers = generateRandom6();
                emitter.onNext(numbers); // (4)
                if (!emitter.isDisposed()) { // (4)
                    SystemClock.sleep(10000); // (5)
                }
            }
        }).map(numbers -> {
            StringBuilder sb = new StringBuilder();
            for (int each : numbers) {
                sb.append(each + " ");
            }
            return sb.toString();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> randomNumbers.setText("Selected Numbers: " + value));
    }

    public void onClickGenerate(View view) {
        generateDisposable = Observable.interval(0,10, TimeUnit.SECONDS)
            .doOnDispose(() -> elapsedDisposable.dispose())
            .map(ignored -> generateRandom6())
            .map(numbers -> {
                StringBuilder sb = new StringBuilder();
                for (int each : numbers) {
                    sb.append(each + " ");
                }
                return sb.toString();
            }).observeOn(AndroidSchedulers.mainThread())
            .subscribe(value -> randomNumbers.setText("Selected Numbers: " + value));
    }

    private int[] generateRandom6() {
        int[] numbers = new int[6];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = random.nextInt(45) + 1;
            for (int j = i - 1; j >= 0; j--) {
                if (numbers[i] == numbers[j]) {
                    i--;
                    break;
                }
            }
        }
        return numbers;
    }

    public void onClickOk(View view) {
        if (generateDisposable != null && !generateDisposable.isDisposed()) { // (5) 시작
            generateDisposable.dispose();
        } // (5) 끝
    }

    @Override
    protected void onDestroy() {
        if (generateDisposable != null && !generateDisposable.isDisposed()) {
            generateDisposable.dispose();
        }
        super.onDestroy();
    }
}

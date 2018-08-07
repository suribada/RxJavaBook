package com.suribada.rxjavabook.chap5;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.suribada.rxjavabook.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Noh.Jaechun on 2018. 8. 6..
 */
public class ObservableCreateActivity extends Activity {

    private TextView title;
    private View button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.four_buttons);
        title = findViewById(R.id.title);
        button = findViewById(R.id.button1);
        try (FileOutputStream fos = openFileOutput("book.txt", Context.MODE_PRIVATE);
                PrintWriter out = new PrintWriter(fos)) {
            out.println("자바");
            out.println("안드로이드");
            out.println("RxJava");
        } catch (Exception e) {
            e.printStackTrace();
        }

        clickDisposable = RxEventObservable.clicks(title)
                .subscribe(ignored -> {
                    Toast.makeText(this, "Clicked", Toast.LENGTH_LONG).show();
                    title.setText("클릭 동작");
                });
    }

    private Disposable clickDisposable, fileDisposable;

    public void onClickButton1(View view) {
        if (clickDisposable != null && !clickDisposable.isDisposed()) {
            clickDisposable.dispose();
        }
    }

    /**
     * 다시 등록
     *
     * @param view
     */
    public void onClickButton2(View view) {
        clickDisposable = RxEventObservable.clicks(title)
                .subscribe(ignored -> {
                    Toast.makeText(this, "Clicked", Toast.LENGTH_LONG).show();
                    title.setText("클릭 동작");
                });
    }

    public void onClickButton3(View view) {
        // first option
        try {
            fileDisposable = RxStreamReader.lines(openFileInput("book.txt"))
                    .scan(new StringBuilder(), (sb, line) ->  sb.append(line))
                    .map(StringBuilder::toString)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(value -> {
                        title.setText(value);
                    }, e -> Toast.makeText(this, "reading file error", Toast.LENGTH_LONG).show(),
                    () -> Toast.makeText(this, "reading file complete", Toast.LENGTH_LONG).show());
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "File not exits", Toast.LENGTH_LONG).show();
        }
        // second option
        Single.fromCallable(() -> openFileInput("book.txt"))
            .flatMapObservable(RxStreamReader::lines)
                .scan(new StringBuilder(), (sb, line) ->  sb.append(line))
                .map(StringBuilder::toString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    title.setText(value);
                }, e -> {
                    if (e instanceof FileNotFoundException) {
                        Toast.makeText(this, "File not exits", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "reading file error", Toast.LENGTH_LONG).show();
                    }
                }, () -> Toast.makeText(this, "reading file complete", Toast.LENGTH_LONG).show());
    }

    public void onClickButton4(View view) {
        if (fileDisposable != null && !fileDisposable.isDisposed()) {
            fileDisposable.dispose();
        }
    }

}

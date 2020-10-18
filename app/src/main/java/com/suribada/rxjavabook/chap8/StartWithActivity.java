package com.suribada.rxjavabook.chap8;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.suribada.rxjavabook.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

public class StartWithActivity extends Activity {

    private ImageView image;
    private ImageDownloader imageDownloader;
    private Subject<String> subject = PublishSubject.create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageDownloader = new ImageDownloader(this);
        setContentView(R.layout.text_and_three_buttons);
        image = findViewById(R.id.image);
        getText().debounce(3, TimeUnit.SECONDS) // (1)
                .startWithItem("대기") // (2)
                .subscribe(System.out::println);
    }

    public void onClickButton1(View v) {
        imageDownloader.showImage_notRecommended(image, "http://goo.gl/gEgYUd");
    }

    public void onClickButton2(View v) {
        imageDownloader.showImage(image, "http://goo.gl/gEgYUd");
    }

    private Observable<String> getText() {
        return subject;
    }

    int i = 0;

    public void onClickButton3(View v) {
        subject.onNext("Text=" + (++i));
    }

}

package com.suribada.rxjavabook.chap5;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.suribada.rxjavabook.R;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GlideActivity extends Activity {

    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_two_buttons);
        imageView = findViewById(R.id.image);
    }

    public void onClickButton1(View view) {
        getBitmapObservable()
                .subscribeOn(Schedulers.io()) // (1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(imageView::setImageBitmap,
                        Throwable::printStackTrace);
    }

    private Observable<Bitmap> getBitmapObservable() {
        return Observable.fromFuture(Glide.with(getContext()) // (1)
                .asBitmap()
                .load("http://goo.gl/gEgYUd")
                .submit()); // (2)
    }

    private Observable<Bitmap> getBitmapObservable2() {
        return Observable.fromFuture(Glide.with(getContext())
                .asBitmap()
                .load("http://goo.gl/gEgYUd")
                .submit(), Schedulers.io()); // (1)
    }

    public void onClickButton2(View view) {
        Glide.with(getContext())
                .load("http://goo.gl/gEgYUd")
                .into(imageView);
    }

    /**
     * 예제 때문에 일부러 만든 메서드
     */
    private Context getContext() {
        return this;
    }
}

package com.suribada.rxjavabook.chap8;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.suribada.rxjavabook.R;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ImageDownloader {

    private final Context context;
    private final Bitmap loadingImage;

    ImageDownloader(Context context) {
        this.context = context;
        this.loadingImage = BitmapFactory.decodeResource( // (1) 시작
                context.getResources(), R.drawable.loading); // (1) 끝
    }

    private Observable<Bitmap> download(String url) { // (2) 시작
        return Observable.fromFuture(Glide.with(context)
                .asBitmap()
                .load(url)
                .submit());
    } // (2) 끝

    public void showImage_notRecommended(ImageView imageView, String url) {
        imageView.setImageBitmap(loadingImage); // (3)
        download(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(imageView::setImageBitmap); // (4)
    }

    public void showImage(ImageView imageView, String url) {
        download(url)
                .subscribeOn(Schedulers.io())
                .startWithItem(loadingImage) // (1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(imageView::setImageBitmap, this::showErrorImage);
    }

    private void showErrorImage(Throwable e) {

    }

}

package com.suribada.rxjavabook.chap7;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.suribada.rxjavabook.SystemClock;
import com.suribada.rxjavabook.model.Profile;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

@RunWith(RobolectricTestRunner.class)
public class ToListMergeTest {

    @Test
    public void merge() {
        List<Profile> profiles = getProfiles();
        Observable.fromIterable(profiles)
                .map(profile -> profile.getImage())
                .flatMap(image -> downloadProfileImage(image).subscribeOn(Schedulers.io())) // (1)
                .toList() // (2)
                .map(bitmaps -> makeMultiProfile()) // (3)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmap -> showMultiProfile(bitmap), // (4)
                        e -> showDefaultImage()); // (5)
        SystemClock.sleep(5000);
    }

    private void showDefaultImage() {
    }

    private Bitmap makeMultiProfile() {
        return Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
    }

    private void showMultiProfile(Bitmap bitmap) {
        System.out.println("drawMultiProfiles");
    }

    private Observable<Bitmap> downloadProfileImage(String image) {
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(0xffff00ff);
        canvas.drawRect(0F, 0F, 100, 100, paint);
        return Observable.just(bitmap);
    }

    private List<Profile> getProfiles() {
        return Arrays.asList(new Profile("1", "url1"),
                new Profile("2", "url2"));
    }

}

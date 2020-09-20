package com.suribada.rxjavabook.chap10;

import android.graphics.Bitmap;

public class MenuOrder {

    private int num;
    private Bitmap image;

    public MenuOrder(int num) {
        this.num = num;
        this.image = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
    }

    public Bitmap getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "num=" + num +
                '}';
    }
}

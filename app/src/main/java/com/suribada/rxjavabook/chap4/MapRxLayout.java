package com.suribada.rxjavabook.chap4;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.jakewharton.rxbinding2.view.RxView;
import com.suribada.rxjavabook.R;

import io.reactivex.rxjava3.core.Observable;

/**
 * Created by Noh.Jaechun on 2018. 5. 9
 */
public class MapRxLayout extends FrameLayout {

    private View zoomIn, zoomOut, gpsActivation;

    public MapRxLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.map_layout, this);
        zoomIn = findViewById(R.id.zoom_in);
        zoomOut = findViewById(R.id.zoom_out);
        gpsActivation = findViewById(R.id.gps_activation);
    }

    public Observable<Object> getGpsActivationObservable() { // (1) 시작
        return RxView.clicks(gpsActivation);
    } // (1) 끝

    public Observable<Integer> getZoomObservable() { // (2) 시작
        Observable<Integer> zoomInObservable = RxView.clicks(zoomIn)
                .map(Void -> Integer.valueOf(1));
        Observable<Integer> zoomOutObservable = RxView.clicks(zoomOut)
                .map(Void -> Integer.valueOf(-1));
        return zoomInObservable.mergeWith(zoomOutObservable);
    } // (2) 끝

}
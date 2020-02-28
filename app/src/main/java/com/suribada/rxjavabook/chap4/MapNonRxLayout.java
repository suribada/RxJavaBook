package com.suribada.rxjavabook.chap4;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.suribada.rxjavabook.R;

/**
 * Created by Noh.Jaechun on 2018. 5. 9
 */

public class MapNonRxLayout extends FrameLayout implements View.OnClickListener {

    public interface MapListener { // (1) 시작
        void onZoomInClick();
        void onZoomOutClick();
        void onGpsActivationClick(); // (1) 끝
    }

    private View zoomIn, zoomOut, gpsActivation;
    private MapListener mapListener;

    public MapNonRxLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.map_layout, this);
        zoomIn = findViewById(R.id.zoom_in);
        zoomOut = findViewById(R.id.zoom_out);
        gpsActivation = findViewById(R.id.gps_activation);
        zoomIn.setOnClickListener(this);
        zoomOut.setOnClickListener(this);
        gpsActivation.setOnClickListener(this);
    }

    public void setMapListener(@Nullable MapListener mapListener) { // (2) 시작
        this.mapListener = mapListener;
        /*
        OnClickListener onClickListener = (mapListener == null) ? null : this;
        zoomIn.setOnClickListener(onClickListener);
        zoomOut.setOnClickListener(onClickListener);
        gpsActivation.setOnClickListener(onClickListener);
        */
    } // (2) 끝

    @Override
    public void onClick(View v) {
        if (mapListener == null) { // (3) 시작
            return;
        } // (3) 끝
        switch (v.getId()) {
            case R.id.zoom_in: // (4) 시작
                mapListener.onZoomInClick();
                break;
            case R.id.zoom_out:
                mapListener.onZoomOutClick();
                break;
            case R.id.gps_activation:
                mapListener.onGpsActivationClick();
                break; // (4) 끝
        }
    }

}

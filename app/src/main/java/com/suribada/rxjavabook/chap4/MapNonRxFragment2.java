package com.suribada.rxjavabook.chap4;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suribada.rxjavabook.R;

/**
 * Created by Noh.Jaechun on 2018. 5. 9
 */

public class MapNonRxFragment2 extends Fragment implements View.OnClickListener {

    public interface MapListener { // (1) 시작
        void onZoomInClick();
        void onZoomOutClick();
        void onGpsActivationClick(); // (1) 끝
    }

    private View zoomIn, zoomOut, gpsActivation;
    private MapListener mapListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MapListener) {
            mapListener = (MapListener) context;
        } else {
            throw new RuntimeException("This Context does not implement MapListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.map_layout, null);
        zoomIn = view.findViewById(R.id.zoom_in);
        zoomOut = view.findViewById(R.id.zoom_in);
        gpsActivation = view.findViewById(R.id.gps_activation);
        zoomIn.setOnClickListener(this);
        zoomOut.setOnClickListener(this);
        gpsActivation.setOnClickListener(this);
        return view;
    }

    public void setMapListener(@Nullable MapListener mapListener) { // (2) 시작
        this.mapListener = mapListener;
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

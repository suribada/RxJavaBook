package com.suribada.rxjavabook.chap4;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;
import com.suribada.rxjavabook.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;

/**
 * Created by Noh.Jaechun on 2018. 5. 9
 */

public class MapRxFragment extends Fragment {

    private View zoomIn, zoomOut, gpsActivation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.map_layout, null);
        zoomIn = view.findViewById(R.id.zoom_in);
        zoomOut = view.findViewById(R.id.zoom_in);
        gpsActivation = view.findViewById(R.id.gps_activation);
        return view;
    }


    public Observable<Object> getGpsActivationObservable() { // (1) 시작
        return RxView.clicks(gpsActivation);
    } // (1) 끝

    public Observable<Integer> getZoomObservable() { // (2) 시작
        Observable<Integer> zoomInObservable = RxView.clicks(zoomIn)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .map(Void -> Integer.valueOf(1));
        Observable<Integer> zoomOutObservable = RxView.clicks(zoomOut)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .map(Void -> Integer.valueOf(-1));
        return zoomInObservable.mergeWith(zoomOutObservable);
    } // (2) 끝

}
package com.suribada.rxjavabook.chap4;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.suribada.rxjavabook.R;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by lia on 2018-05-11.
 */
public class TwoMapActivity extends Activity {

    private static final String TAG = "MapActivity";

    private MapNonRxLayout mapNonRxLayout;
    private MapRxLayout mapRxLayout;

    private int currentZoom = 8;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_map_layout);
        mapNonRxLayout = (MapNonRxLayout) findViewById(R.id.map);
        mapRxLayout = (MapRxLayout) findViewById(R.id.map_rx);
    }

    public void onClickSet1(View view) {
        mapNonRxLayout.setMapListener(new MapNonRxLayout.MapListener() {
            @Override
            public void onZoomInClick() {
                currentZoom ++; // (1)
                Log.d(TAG, "zoomInClick currentZoom=" + currentZoom);
            }

            @Override
            public void onZoomOutClick() {
                if (currentZoom <= 1) { // (2) 시작
                    return;
                } // (2) 끝
                currentZoom --; // (3)
                Log.d(TAG, "zoomOutClick currentZoom=" + currentZoom);
            }

            @Override
            public void onGpsActivationClick() {
                Log.d(TAG, "gpsActivation");
            }
        });
    }

    public void onClickReset1(View view) {
        mapNonRxLayout.setMapListener(null); // (4)
    }

    /*
    private CompositeDisposable disposables = new CompositeDisposable(); // (1)

    public void onClickSet2(View view) {
        disposables.add(mapRxLayout.getGpsActivationObservable() // (2)
                .subscribe(ignored -> Log.d(TAG, "gpsActivation")));
        disposables.add(mapRxLayout.getZoomObservable() // (3)
                .filter(value -> (value >= 0 || currentZoom > 1))
                .subscribe(value -> {
                    currentZoom += value;
                    Log.d(TAG, "currentZoom=" + currentZoom);
                }));
    }

    public void onClickReset2(View view) {
       disposables.dispose(); // (4)
    }
    */

    private Disposable disposableGpsActivation;
    private Disposable disposableZoom;

    public void onClickSet2(View view) {
        disposableGpsActivation = mapRxLayout.getGpsActivationObservable()
                .subscribe(ignored -> Log.d(TAG, "gpsActivation"));
        disposableZoom = mapRxLayout.getZoomObservable()
                .filter(value -> !(value < 0 && currentZoom <= 1)) // (1)
                .subscribe(value -> {
                    currentZoom += value; // (2)
                    Log.d(TAG, "currentZoom=" + currentZoom);
                });
    }

    public void onClickReset2(View view) {
        disposableGpsActivation.dispose(); // (3) 시작
        disposableZoom.dispose(); // (3) 끝
    }

}

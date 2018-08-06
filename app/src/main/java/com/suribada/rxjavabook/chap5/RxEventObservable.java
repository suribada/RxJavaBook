package com.suribada.rxjavabook.chap5;

import android.view.View;

import io.reactivex.Observable;

/**
 * Created by Noh.Jaechun on 2018. 8. 6..
 */
public class RxEventObservable {

    private final static Object EVENT = new Object();

    public static Observable<Object> clicks(View view) {
        return Observable.create(emitter -> {
            view.setOnClickListener(v -> {
                if (!emitter.isDisposed()) {
                    emitter.onNext(EVENT);
                }
            });
            emitter.setCancellable(() -> view.setOnClickListener(null));
        });
    }

}

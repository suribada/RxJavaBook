package com.suribada.rxjavabook.chap5;

import android.view.View;

import io.reactivex.Observable;

/**
 * Created by Noh.Jaechun on 2018. 8. 6..
 */
public class RxEventObservable {

    private final static Object EVENT = new Object(); // (1)

    public static Observable<Object> clicks(View view) {
        return Observable.create(emitter -> {
            view.setOnClickListener(v -> {
                if (!emitter.isDisposed()) { // (2)
                    emitter.onNext(EVENT); // (3)
                }
            });
            emitter.setCancellable(() -> view.setOnClickListener(null)); // (4)
        });
    }

}

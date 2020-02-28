package com.suribada.rxjavabook.seudo;

import android.util.Base64;

import io.reactivex.rxjava3.core.Observable;

/**
 * Created by Naver on 2017. 10. 13..
 */
public class FromCallable {

    public Observable<String> getDecodeName(String name) {
        return Observable.create(emitter -> {
            try {
                emitter.onNext(new String(Base64.decode(name, 0)));
                emitter.onComplete();
            } catch (IllegalArgumentException e) {
                emitter.onError(e);
            }
        });
    }

    public Observable<String> getDecodeName2(String name) {
        return Observable.fromCallable(() -> new String(Base64.decode(name, 0)));
    }

}

package com.suribada.rxjavabook.error;

import android.util.Base64;

import io.reactivex.Single;

/**
 * Created by lia on 2018-01-13.
 */
public class Base64Decoder {

    public Single<String> getDecodedString(String input) {
        return Single.fromCallable(()
                -> new String(Base64.decode(input.getBytes(), Base64.DEFAULT)));
    }

}


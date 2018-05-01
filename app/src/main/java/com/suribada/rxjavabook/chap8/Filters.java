package com.suribada.rxjavabook.chap8;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by lia on 2018-05-01.
 */

public class Filters {

    public static <T> Function<Observable<T>, Observable<? extends T>> cast(Class<? extends T> clazz) {
        return source -> source.filter(value -> clazz.isInstance(value))
                .cast(clazz);
    }

}

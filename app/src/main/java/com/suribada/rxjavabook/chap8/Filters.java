package com.suribada.rxjavabook.chap8;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;

/**
 * Created by lia on 2018-05-01.
 */

public class Filters {

    /**
     * instanceof 필터링과 캐스팅을 한꺼번에 실행하는 변환 연산자
     * ofType 연산자와 동일
     *
     * ex) stateObservable.to(Filters.cast(State.LoadingState.class)).subscribe(state -> showText(state.toString()));
     */
    public static <T> Function<Observable<T>, Observable<? extends T>> cast(Class<? extends T> clazz) {
        return source -> source.filter(value -> clazz.isInstance(value))
                .cast(clazz);
    }

}

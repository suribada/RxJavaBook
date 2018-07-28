package com.suribada.rxjavabook.chap5;

/**
 * Created by lia on 2018-07-24.
 */

public class Observable2<T> {

    private T t;

    T get1() {
        return t;
    }

    <T> T get2() {
        return (T) t;
    }

    static <T> T get3() {
        return null;
    }

}

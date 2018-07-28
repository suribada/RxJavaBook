package com.suribada.rxjavabook.chap5;

/**
 * Created by lia on 2018-07-17.
 */

public interface Consumer2<T> {
    /**
     * Consume the given value.
     * @param t the value
     * @throws Exception on error
     */
    <U>  void accept(T t, U u) throws Exception;
    <T> T get();

    T get2();

}
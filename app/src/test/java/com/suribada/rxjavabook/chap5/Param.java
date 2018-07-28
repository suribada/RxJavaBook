package com.suribada.rxjavabook.chap5;

/**
 * Created by lia on 2018-07-17.
 */

public class Param<T> implements Consumer2<T> {

    @Override
    public <U>  void accept(T t1, U u) throws Exception {
    }

    public <T, U>  void accept2(T t1, U u) throws Exception { // <T, U>는 <U>로 변경 가능
    }


    public static <T, U> U accept3(T t, U u) { // <T, U>를 <U>로 쓸 수 없음
        return null;
    }

    @Override
    public <T> T get() {
        return null;
    }
}
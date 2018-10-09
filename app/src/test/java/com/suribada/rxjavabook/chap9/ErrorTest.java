package com.suribada.rxjavabook.chap9;

import org.junit.Test;

import io.reactivex.Single;

public class ErrorTest {

    /**
     * onErrorReturnItem 에도  null 전달하면 안된다.
     */
    @Test
    public void testOnErrorReurn() {
        String a = Single.fromCallable(() -> {
            if (true) {
                throw new Exception("error");
            } else {
                return "haha";
            }}
            ).onErrorReturnItem(null).blockingGet();
        System.out.println(a);

    }
}

package com.suribada.rxjavabook;

import org.junit.Test;

import io.reactivex.rxjava3.functions.Predicate;

/**
 * Created by lia on 2017-10-16.
 */

public class LambdaTest {

    @Test
    public void testLambda() {
        Predicate<Integer> even = (number) -> number % 2 == 0;
        boolean isEven = false;
        try {
            isEven = even.test(50);
            System.out.println("isEven=" + isEven);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}

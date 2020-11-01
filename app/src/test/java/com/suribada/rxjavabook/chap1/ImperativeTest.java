package com.suribada.rxjavabook.chap1;

import org.junit.Test;

public class ImperativeTest {

    @Test
    public void plus() {
        int a = 13;
        int b = 17;
        int sum = a + b;
        System.out.println("sum=" + sum); // (1)
        a = 15;
        System.out.println("sum=" + sum); // (2)
        sum = a + b;
        System.out.println("sum=" + sum); // (3)
    }
}

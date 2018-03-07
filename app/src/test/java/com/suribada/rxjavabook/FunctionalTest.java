package com.suribada.rxjavabook;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lia on 2018-03-08.
 */
public class FunctionalTest {

    @Test
    public void perfLambdaTest() {
        long start = System.currentTimeMillis();
        AtomicInteger value = new AtomicInteger(0);
        for (int i = 0; i < 10000000; i++) {
            new Runnable() {
                @Override
                public void run() {
                    //System.out.println(value.getAndIncrement());
                }
            }.run();
        }
        System.out.println("elapsed 1=" + (System.currentTimeMillis() - start));
    }

    @Test
    public void perfAnonyousTest() {
        long start = System.currentTimeMillis();
        AtomicInteger value = new AtomicInteger(0);
        for (int i = 0; i < 10000000; i++) {
            ((Runnable) () -> {
                //System.out.println(value.getAndIncrement())
            }).run();
        }
        System.out.println("elapsed 1=" + (System.currentTimeMillis() - start));
    }

}

package com.suribada.rxjavabook.seudo;

/**
 * Created by lia on 2018-03-17.
 */
public class Functional {

    public void action1() {
        Runnable r = () -> { someAction(); };
        new Runnable() {
            @Override
            public void run() {
                someAction();
            }
        };
        new Runnable() {
            @Override
            public void run() {
                someAction();
            }
        }.run();
    }

    private void someAction() {
        System.out.println("hello");
    }
}

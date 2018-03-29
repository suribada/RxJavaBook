package com.suribada.rxjavabook.chap2;

/**
 * Created by Noh.Jaechun on 2018. 3. 26..
 */

public class MutableValue {

    private long val;

    public synchronized void add(int input) {
        val += input;
    }

    public long getSum() {
        return val;
    }
}

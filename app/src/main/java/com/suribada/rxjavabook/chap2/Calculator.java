package com.suribada.rxjavabook.chap2;

/**
 * Created by Noh.Jaechun on 2018. 3. 26..
 */

public class Calculator {

    ImmutableValue immutableValue = new ImmutableValue(0L);

    public void add(int j) {
        immutableValue = immutableValue.add(j);
    }

    public long getSum() {
        return immutableValue.getSum();
    }

}

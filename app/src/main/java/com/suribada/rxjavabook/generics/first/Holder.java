package com.suribada.rxjavabook.generics.first;

/**
 * Created by Noh.Jaechun on 2018. 12. 21..
 */
public class Holder<T> {

    private T t;

    public void set(T t) {
        this.t = t;
    }

    public T get() {
        return t;
    }

}

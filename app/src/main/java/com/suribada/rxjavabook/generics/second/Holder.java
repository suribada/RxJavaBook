package com.suribada.rxjavabook.generics.second;

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

    // 1st
    /*
    public void set(Holder<T> t) {
        this.t = t.get();
    }

    public void transfer(Holder<T> holder) {
        holder.set(t);
    }
    */

    // 2nd
    public void set(Holder<? extends T> holder) {
        this.t = holder.get();
    }

    public void transfer(Holder<? super T> holder) {
        holder.set(t);
    }

}

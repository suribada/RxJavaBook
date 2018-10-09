package com.suribada.rxjavabook.chap5;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Noh.Jaechun on 2018. 7. 29..
 */
public class CartRepository {
    public Observable<AddResult> add(Product product) {
        return Observable.just(new AddResult());
    }

    public Observable<List<Product>> query(String keyword) {
        return Observable.just(new Product(), new Product()).toList().toObservable();
    }

    public Observable<TerminalResult> terminate() {
        return Observable.just(new TerminalResult());
    }
}

package com.suribada.rxjavabook.chap5;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

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

    public Observable<Result> getResult() {
        return Observable.just(new Result());
    }
}

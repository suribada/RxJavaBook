package com.suribada.rxjavabook.chap5;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

/**
 * Created by Noh.Jaechun on 2018. 7. 29..
 */
public class ShoppingCart {

    private String userId;

    private CartRepository repository = new CartRepository();

    private Observable<AddResult> add1(Product product) {
        checkUserId1();
        return repository.add(product);
    }

    private Observable<List<Product>> query1(String keyword) {
        checkUserId1();
        return repository.query(keyword);
    }

    private void checkUserId1() {
        if (userId == null) {
            throw new AuthentificationException("userId is null");
        }
    }

    private Observable checkUserId2() { // (1) 시작
        return (userId == null) ? Observable.error(new AuthentificationException("userId is null"))
                : Observable.empty();
    } // (1) 끝

    private Observable<AddResult> add2(Product product) {
        return Observable.concat(checkUserId2(), repository.add(product)); // (2)
    }

    private Observable<List<Product>> query2(String keyword) {
        return Observable.concat(checkUserId2(), repository.query(keyword)); // (3)
    }

    private void log(Result result) {
        //....
    }

    private void logCurrent2() {
//        Observable.concat(checkUserId2(), repository.getResult())
//                .subscribe(result -> log(result)); // (1) 컴파일 에러
        Observable<Result> obs = Observable.concat(checkUserId2(), repository.getResult()); // (2)
        obs.subscribe(result -> log(result));
    }


    private Observable<AddResult> add3(Product product) {
        return Observable.concat(checkUserId3(), repository.add(product));
    }

    private Observable<List<Product>> query3(String keyword) {
        return Observable.concat(checkUserId3(), repository.query(keyword));
    }

    private void logCurrent3() {
        Observable.concat(checkUserId3(), repository.getResult())
                .subscribe(result -> log(result));
    }


    private <T> Observable<T> checkUserId3() {
        return (userId == null) ? Observable.error(new AuthentificationException("userId is null"))
                : Observable.empty();
    }

}

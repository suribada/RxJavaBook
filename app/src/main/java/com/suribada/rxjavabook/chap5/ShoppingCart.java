package com.suribada.rxjavabook.chap5;

import android.accounts.AuthenticatorException;

import java.util.List;
import java.util.TreeMap;

import io.reactivex.Observable;

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
            throw new AuthentificationRuntimeException("userId is null");
        }
    }

    private Observable<AddResult> add2(Product product) {
        return Observable.concat(checkUserId2(), repository.add(product));
    }

    private Observable<List<Product>> query2(String keyword) {
        return Observable.concat(checkUserId2(), repository.query(keyword));
    }

    private Observable checkUserId2() {
        return (userId == null) ? Observable.error(new AuthentificationRuntimeException("userId is null"))
                : Observable.empty();
    }

    private void logDatabase(TerminalResult terminalResult) {
        //....
    }

    private void terminate2() {
//        Observable.concat(checkUserId2(), repository.terminate())
//                .subscribe(result -> logDatabase(result)); // (1) 컴파일 에러
        Observable<TerminalResult> obs = Observable.concat(checkUserId2(), repository.terminate()); // (2)
        obs.subscribe(result -> logDatabase(result));
    }


    private Observable<AddResult> add3(Product product) {
        return Observable.concat(checkUserId3(), repository.add(product));
    }

    private Observable<List<Product>> query3(String keyword) {
        return Observable.concat(checkUserId3(), repository.query(keyword));
    }

    private void terminate3() {
        Observable.concat(checkUserId3(), repository.terminate())
                .subscribe(result -> logDatabase(result));
    }


    private <T> Observable<T> checkUserId3() {
        return (userId == null) ? Observable.error(new AuthentificationRuntimeException("userId is null"))
                : Observable.empty();
    }

}

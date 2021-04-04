package com.suribada.rxjavabook.chap5;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Created by Noh.Jaechun on 2018. 8. 3..
 */
public class FromCallableTest {

    private DbHelper dbHelper = new DbHelper();

    @Test
    public void testCallable() {
        int id = 1;
        Observable.just(dbHelper.getProduct(id)) // (1)
                .subscribeOn(Schedulers.io())
                .flatMap(product -> getStockApi(product)) // (2)
                .subscribe(System.out::println);
    }

    @Test
    public void testCallable2() {
        int id = 1;
        Observable.fromCallable(() -> dbHelper.getProduct(id)) // (1)
                .subscribeOn(Schedulers.io())
                .flatMap(product -> getStockApi(product))
                .subscribe(System.out::println);
    }

    @Test
    public void testCallable3() {
        String param = "123";
        Observable<Integer> obs = Observable.create(emitter -> {
            try {
                emitter.onNext(Integer.parseInt(param));
                emitter.onComplete();
            } catch (NumberFormatException e) {
                emitter.onError(e);
            }
        });
    }

    public void testCallable4() {
        String param = "123";
        Observable<Integer> obs = Observable.fromCallable(() -> Integer.parseInt(param));
    }

    private Observable<Stock> getStockApi(Product product) {
        return Observable.just(new Stock());
    }

    private class DbHelper {
        public List<Product> getProductList() {
            return Collections.emptyList();
        }

        public Product getProduct(int id) {
            System.out.println("thread=" + Thread.currentThread().getName());
            return new Product();
        }
    }
}

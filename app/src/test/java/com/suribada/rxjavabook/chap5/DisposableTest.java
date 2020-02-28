package com.suribada.rxjavabook.chap5;

import org.junit.Test;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * Created by Noh.Jaechun on 2018. 10. 1..
 */
public class DisposableTest {

    @Test
    public void range() {
        Disposable disposable = Observable.range(1, 3)
                .doOnDispose(() -> System.out.println("doOnDispose"))
                .subscribe(System.out::println);
        disposable.dispose();

    }
}

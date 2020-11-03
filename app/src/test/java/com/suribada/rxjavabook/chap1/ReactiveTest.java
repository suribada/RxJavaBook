package com.suribada.rxjavabook.chap1;

import org.junit.Test;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

public class ReactiveTest {

    @Test
    public void plus() {
        Subject<Integer> a = PublishSubject.create();
        Subject<Integer> b = PublishSubject.create();
        Observable.combineLatest(a, b, (x, y) -> x + y)
                .subscribe(sum -> System.out.println("sum=" + sum));
        a.onNext(13);
        b.onNext(17);
        a.onNext(15);
    }
}

package com.suribada.rxjavabook.chap1;

import org.junit.Test;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

public class ReactiveTest {

    @Test
    public void plus() {
        Subject<Integer> a = PublishSubject.create(); // (1) 시작
        Subject<Integer> b = PublishSubject.create(); // (1) 끝
        Observable.combineLatest(a, b, (x, y) -> x + y) // (2)
                .subscribe(sum -> System.out.println("sum=" + sum)); // (3)
        a.onNext(13); // (4) 시작
        b.onNext(17);
        a.onNext(15);
        b.onNext(18); // (4) 끝
    }
}

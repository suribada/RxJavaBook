package com.suribada.rxjavabook;

import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.internal.operators.observable.ObservableFilter;
import io.reactivex.internal.operators.observable.ObservableFromIterable;
import io.reactivex.internal.operators.observable.ObservableMap;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by lia on 2017-10-12.
 */

public class RxJava1Test {
    private List<String> list = Arrays.asList("Android", "iOS", "Bada");

    @Test
    public void testIterator() {
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            if (next.length() > 3) {
                String value =  "OS:" + next;
                showText(value);
            }
        }
    }

    @Test
    public void testObservable() {
        Observable.fromIterable(list)
            .filter(value -> value.length() > 3)
            .map(os -> "OS:" + os)
            //.subscribe(value -> showText(value));
            .subscribe(this::showText);
    }

    private void showText(String input) {
        System.out.println("input=" + input);
    }

    @Test
    public void testDisposableObserver() {
        DisposableObserver observer = Observable.fromIterable(list)
                .filter(value -> value.length() > 3)
                .map(os -> "OS:" + os)
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        showText(s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        observer.dispose();
    }

    @Test
    public void testGugudan() {
        Observable.range(2, 8)
            .flatMap(row -> Observable.range(1, 9)
                    .map(col -> String.format("%d x %d = %d", row, col, row * col)))
            .subscribe(System.out::println);

    }

    @Test
    public void testGugudan2() {
        Observable.range(1, 9)
            .flatMap(row -> Observable.range(2, 8),
                    (row, col) -> String.format("%d x %d = %d", col, row, col * row))
            .subscribe(System.out::println);
    }

    @Test
    public void testGuguDan3() {
        Observable.range(1, 9)
                .join(Observable.range(2, 8),
                        i -> Observable.never(),
                        i -> Observable.never(),
                        (row, col) -> String.format("%d x %d = %d", col, row, col * row))
                .subscribe(System.out::println);
    }

    @Test
    public void testGugudan4() {
        Observable.range(1, 9)
                .groupJoin(Observable.range(2, 8),
                        i -> Observable.never(),
                        i -> Observable.never(),
                        (row, ObservableCol) -> ObservableCol)
                .subscribe(System.out::println);
    }

    @Test
    public void testSingle() {
        Single.create(e -> {}).subscribe(System.out::println, e -> e.printStackTrace());
    }
}

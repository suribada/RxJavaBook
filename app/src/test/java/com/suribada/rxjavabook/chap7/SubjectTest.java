package com.suribada.rxjavabook.chap7;

import org.junit.Test;

import java.util.Arrays;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableConverter;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

public class SubjectTest {

    @Test
    public void mergeSubject() {
        Subject<Integer> left = PublishSubject.create();
        Subject<Integer> right = PublishSubject.create();
        Subject<Integer> right2 = PublishSubject.create();
        Subject<Integer> right3 = PublishSubject.create();
        Subject<Integer> right4 = PublishSubject.create();
        Observable.merge(Arrays.asList(left, right, right2, right3, right4)).subscribe(System.out::println);
        left.onNext(1);
        left.onNext(3);
        left.onNext(5);
        right.onNext(5);
        right.onNext(5);
        right2.onNext(6);
        right3.onNext(7);
        right3.onNext(8);
        right4.onNext(9);
        Observable.just(11, 13, 15, 17, 19).subscribe(right::onNext);
    }

    @Test
    public void mergeSubject2() {
        Subject<Integer> left = PublishSubject.create();
        Subject<Integer> right = PublishSubject.create();
        Subject<Integer> right2 = PublishSubject.create();
        Subject<Integer> right3 = PublishSubject.create();
        Subject<Integer> right4 = PublishSubject.create();
        Observable.merge(Arrays.asList(left, right, right2, right3, right4)).to(new MessageBinder().bind(System.out::println));
        left.onNext(1);
        left.onNext(3);
        left.onNext(5);
        right.onNext(5);
        right.onNext(5);
        right2.onNext(6);
        right3.onNext(7);
        right3.onNext(8);
        right4.onNext(9);
        Observable.just(11, 13, 15, 17, 19).subscribe(right4::onNext);
    }

    class MessageBinder {
        ObservableConverter<Integer, Disposable> bind(Consumer<Integer> consumer) {
            return source -> source.subscribeWith(new DisposableObserver<Integer>() {
                @Override
                public void onNext(Integer integer) {
                    try {
                        consumer.accept(integer);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
        }
    }
}
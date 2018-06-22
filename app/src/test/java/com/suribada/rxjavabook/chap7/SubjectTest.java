package com.suribada.rxjavabook.chap7;

import org.junit.Test;

import java.util.Arrays;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

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
        Function<Observable<Integer>, Disposable> bind(Consumer<Integer> consumer) {
            return source -> source.subscribeWith(new DisposableObserver<Integer>() {
                @Override
                public void onNext(Integer integer) {
                    try {
                        consumer.accept(integer);
                    } catch (Exception e) {
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
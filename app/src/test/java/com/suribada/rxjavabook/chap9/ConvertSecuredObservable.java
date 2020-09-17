package com.suribada.rxjavabook.chap9;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class ConvertSecuredObservable<T extends SecuredUser> extends Observable<T> {

    private final ObservableSource<? super User> source;

    public ConvertSecuredObservable(ObservableSource<User> source) {
        this.source = source;
    }

    @Override
    protected void subscribeActual(@NonNull Observer<? super T> observer) {
        source.subscribe(new SecuredObserver(observer));
    }

    static final class SecuredObserver<T extends User> implements Observer<T>, Disposable {

        private Disposable upstream;
        private Observer<SecuredUser> downstream;

        SecuredObserver(Observer<SecuredUser> downstream) {
            this.downstream = downstream;
        }

        @Override
        public void onSubscribe(@NonNull Disposable d) {
            upstream = d;
            downstream.onSubscribe(d);
        }

        @Override
        public void onNext(User user) {
            System.out.println("onNext user=" + user);
            if (user.isInvalid()) { // (2) 시작
                reportUser(user);
                return;
            } // (2) 끝
            downstream.onNext(SecuredUser.create(user)); // (3)
        }

        @Override
        public void onError(@NonNull Throwable e) {
            downstream.onError(e);
        }

        @Override
        public void onComplete() {
            downstream.onComplete();
        }

        @Override
        public void dispose() {
            upstream.dispose();
        }

        @Override
        public boolean isDisposed() {
            return upstream.isDisposed();
        }
    }

    public static void reportUser(User user) {
        System.out.println("report User=" + user);
    }
}
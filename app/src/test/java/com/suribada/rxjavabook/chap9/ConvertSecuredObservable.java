package com.suribada.rxjavabook.chap9;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class ConvertSecuredObservable<T extends SecuredUser> extends Observable<T> { // (1)

    private final ObservableSource<? super User> source;

    public ConvertSecuredObservable(ObservableSource<User> source) { // (2) 시작
        this.source = source;
    } // (2) 끋

    @Override
    protected void subscribeActual(@NonNull Observer<? super T> observer) { // (3) 시작
        source.subscribe(new SecuredObserver(observer));
    } // (3) 끋

    private static class SecuredObserver<T extends User> implements Observer<T>, Disposable { // (4)

        private Disposable upstream;
        private Observer<SecuredUser> downstream;

        SecuredObserver(Observer<SecuredUser> downstream) { // (4) 시작
            this.downstream = downstream;
        } // (4) 시작

        @Override
        public void onSubscribe(@NonNull Disposable d) {
            upstream = d;
            downstream.onSubscribe(d);
        }

        @Override
        public void onNext(User user) {
            System.out.println("onNext user=" + user);
            if (user.isInvalid()) {
                reportUser(user);
                return;
            }
            downstream.onNext(SecuredUser.create(user));
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

        private void reportUser(User user) {
            System.out.println("report User=" + user);
        }
    }

}
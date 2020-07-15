package com.suribada.rxjavabook.chap7;

import org.junit.Test;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

public class HideTest {

    private LoginManager loginManager = new LoginManager();

    @Test
    public void castLoginObservable() {
        ((Subject) loginManager.getLoginObservable()).onNext(true);
    }

    @Test//(expected = ClassCastException.class)
    public void castLoginObservable2() {
        ((Subject) loginManager.getLoginObservable2()).onNext(true);
    }

    @Test//(expected = ClassCastException.class)
    public void castLoginObservable3() {
        ((Subject) loginManager.getLoginObservable3()).onNext(true);
    }

    private class LoginManager {
        private Subject<Boolean> loginSubject = PublishSubject.create();

        public Observable<Boolean> getLoginObservable() { // (1)
            return loginSubject;
        }

        public Observable<Boolean> getLoginObservable2() {
            return loginSubject.hide(); // (1)
        }

        public Observable<Boolean> getLoginObservable3() {
            return loginSubject.filter(login -> true);
        }
    }
}

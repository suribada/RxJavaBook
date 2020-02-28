package com.suribada.rxjavabook;

import org.junit.Test;

import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import io.reactivex.rxjava3.subjects.Subject;

/**
 * Created by lia on 2017-10-15.
 */

public class SubjectTest {

    @Test
    public void testBehavior() {
        Subject<String> subject = BehaviorSubject.create();
        subject.subscribe(text -> System.out.println("obs1:" + text),
                e -> System.out.println("obs1 error"),
                () -> System.out.println("obs1 complete"));

        subject.onNext("java");
        subject.onNext("android");
        subject.onComplete();

        subject.subscribe(text -> System.out.println("obs2:" + text),
                e -> System.out.println("obs2 error"),
                () -> System.out.println("obs2 complete"));

        subject.onNext("Naver");
    }

    @Test
    public void testReplay() {
        Subject<String> subject = ReplaySubject.create();
        subject.subscribe(text -> System.out.println("obs1:" + text),
                e -> System.out.println("obs1 error"),
                () -> System.out.println("obs1 complete"));

        subject.onNext("java");
        subject.onNext("android");
        subject.onComplete();

        subject.subscribe(text -> System.out.println("obs2:" + text),
                e -> System.out.println("obs2 error"),
                () -> System.out.println("obs2 complete"));

        subject.onNext("Naver");
    }
}

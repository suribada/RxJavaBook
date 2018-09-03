package com.suribada.rxjavabook.chap5;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Noh.Jaechun on 2018. 9. 2..
 */
public class SubjectTest {

    @Test
    public void testSerialSubject() throws InterruptedException {
        //Observable<Integer> obs = PublishSubject.<Integer>create().serialize();
        Subject<Integer> subject = PublishSubject.<Integer>create().toSerialized(); // (1)
        subject.subscribe(System.out::println); // (2)
        new Thread(() -> {
            subject.onNext(1);
            subject.onNext(2);
            subject.onNext(3);
            subject.onNext(4);
        }).start();
        new Thread(() -> {
            subject.onNext(5);
            subject.onNext(6);
            subject.onNext(7);
            subject.onNext(8);
        }).start();
        Thread.sleep(2000);
    }

    @Test
    public void testPublishSubject() {
        Subject<Integer> subject = PublishSubject.create(); // (1)
        subject.onNext(1);
        subject.onNext(2);
        subject.subscribe(value -> System.out.println("obs1 value=" + value)); // (2)
        subject.onNext(3);
        subject.onNext(4);
        subject.subscribe(value -> System.out.println("obs2 value=" + value)); // (3)
        subject.onNext(5);
        subject.onNext(6);
    }

    @Test
    public void testPublishSubjectWithTerminal() {
        Subject<Integer> subject = PublishSubject.create();
        subject.onNext(1);
        subject.subscribe(value -> System.out.println("obs1 value=" + value),
                e -> System.out.println("obs1 error=" + e.getMessage()));
        subject.onNext(2);
        subject.onError(new NullPointerException("null is not permitted")); // (1)
        subject.onNext(3); // (2)
        subject.subscribe(value -> System.out.println("obs2 value=" + value),
                e -> System.out.println("obs2 error=" + e.getMessage())); // (3)
    }

    @Test
    public void testBehaviorSubject() {
        Subject<Integer> subject = BehaviorSubject.create();
        subject.onNext(1);
        subject.onNext(2);
        subject.subscribe(value -> System.out.println("obs1 value=" + value)); // (1)
        subject.onNext(3);
        subject.onNext(4);
        subject.subscribe(value -> System.out.println("obs2 value=" + value)); // (2)
        subject.onNext(5);
        subject.onNext(6);
    }

    @Test
    public void testReplaySubject() {
        Subject<Integer> subject = ReplaySubject.create();
        subject.onNext(1);
        subject.onNext(2);
        subject.subscribe(value -> System.out.println("obs1 value=" + value));
        subject.onNext(3);
        subject.onNext(4);
        subject.subscribe(value -> System.out.println("obs2 value=" + value));
        subject.onNext(5);
        subject.onNext(6);
    }

    @Test
    public void testAsyncSubject() {
        AsyncSubject<Integer> subject = AsyncSubject.create();
        subject.onNext(1);
        subject.onNext(2);
        subject.subscribe(value -> System.out.println("obs1 value=" + value),
                Throwable::printStackTrace,
                () -> System.out.println("obs1 onComplete"));
        subject.onNext(3);
        subject.onNext(4);
        subject.subscribe(value -> System.out.println("obs2 value=" + value),
                Throwable::printStackTrace,
                () -> System.out.println("obs2 onComplete"));
        subject.onNext(5);
        subject.onNext(6);
        subject.onComplete(); // (1)
        subject.subscribe(value -> System.out.println("obs3 value=" + value),
                Throwable::printStackTrace,
                () -> System.out.println("obs3 onComplete"));
        subject.onNext(7); // (2)
    }

}

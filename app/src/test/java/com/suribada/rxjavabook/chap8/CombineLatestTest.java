package com.suribada.rxjavabook.chap8;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;

public class CombineLatestTest {

    @Test
    public void combineLatest_simple() {
        Observable.combineLatest(Observable.interval(0L,1, TimeUnit.SECONDS), // (1)
                Observable.just("A", "B", "C"), // (2)
                (x, y) -> x + y) // (3)
                .subscribe(System.out::println);
        SystemClock.sleep(5000);
    }
    @Test
    public void combineLatest() {
        Observable.combineLatestArray(new Observable[] {Observable.just("A", "B"), Observable.just(1, 2)},
                (objects -> objects[0].toString() + "/" + objects[1].toString()))
                .subscribe(System.out::println, System.err::println,
                        () -> System.out.println("onComplete"));
    }

    @Test
    public void combineLatest_list() {
        boolean upperCase = false;
        List<Observable<?>> obsList = new ArrayList<>();
        if (upperCase) {
            obsList.add(Observable.just("A", "B"));
        } else {
            obsList.add(Observable.just("a", "b"));
        }
        obsList.add(Observable.just(1, 2, 3));

        Observable.combineLatest(obsList,
                (objects -> objects[0].toString() + "/" + objects[1].toString()))
                .subscribe(System.out::println);
    }
}

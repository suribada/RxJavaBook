package com.suribada.rxjavabook.chap8;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class CombineLatestTest {

    @Test
    public void combineLatest() {
        Observable.combineLatestArray(new Observable[] {Observable.just("A", "B"), Observable.just(1, 2)},
                (objects -> objects[0].toString() + "/" + objects[1].toString()))
                .subscribe(System.out::println);
    }

    @Test
    public void combineLatest_list() {
        List<Observable<?>> obsList = new ArrayList<>();
        obsList.add(Observable.just("A", "B"));
        obsList.add(Observable.just(1, 2));

        Observable.combineLatest(obsList,
                (objects -> objects[0].toString() + "/" + objects[1].toString()))
                .subscribe(System.out::println);
    }
}

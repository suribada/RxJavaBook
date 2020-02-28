package com.suribada.rxjavabook.chap7;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

import io.reactivex.rxjava3.core.Observable;

public class ToListTest {

    @Test
    public void toListDefault() {
        Observable.just(1, 3, 5, 2).toSortedList((a, b) -> a.compareTo(b))
                .subscribe(System.out::println);
    }

    @Test
    public void toListCollectionSupplier() {
        Observable.just(1, 3, 5, 2, 3).toList(HashSet::new)
                .subscribe(System.out::println);
    }

}

package com.suribada.rxjavabook.chap7;

import com.suribada.rxjavabook.chap5.SearchResult;

import org.junit.Test;

import java.util.Collections;

import io.reactivex.rxjava3.core.Observable;


public class SortedTest {

    /**
     * SearchResult는 Comparable을 구현하지 않았으므로 ClassCastException을 onError에 내보낸다.
     */
    @Test
    public void sorted() {
        Observable.just(new SearchResult("Kotlin"), new SearchResult("Java"))
                .sorted()
                .subscribe(System.out::println, System.err::println);
    }

    @Test
    public void sortedWithComparator() {
        Observable.just(new SearchResult("Kotlin"), new SearchResult("Java"))
                .sorted((o1, o2) -> Integer.compare(o1.count(), o2.count())) // (1)
                .subscribe(System.out::println);
    }

    @Test
    public void sortedWithComparator_withAlternative() {
        Observable.just(new SearchResult("Kotlin"), new SearchResult("Java"))
                .toList() // (1)
                .toObservable()
                .map(v -> {
                    Collections.sort(v, (o1, o2) -> Integer.compare(o1.count(), o2.count()));
                    return v;
                }).flatMapIterable(x -> x) // (1) 끝
                .subscribe(System.out::println);
    }
}

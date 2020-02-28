package com.suribada.rxjavabook.chap4;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

/**
 * Created by Noh.Jaechun on 2018. 5. 24..
 */
public class CollectionTest {

    @Test
    public void testArray() {
        Integer[] integers = new Integer[] {1, 2};
        Observable.fromArray(integers)
                .subscribe(System.out::println);
        //primitive arrays are not supported
        // https://stackoverflow.com/questions/43560993/rxjava-compile-error-on-the-following-case?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
        // https://github.com/ReactiveX/RxJava/issues/3518
        int[] integers2 = new int[] {1, 2};
        int[] integers3 = new int[] {3, 4};
        System.out.println(integers2.getClass().getSimpleName());
        Observable.fromArray(integers2, integers3) // 각 배열이 하나의 객체로 간주
                .subscribe(System.out::println);
    }

    @Test
    public void testArray2() {
        Integer[] integers = new Integer[] {1, 2};
        Observable.fromArray(integers)
                .subscribe(System.out::println);
    }

    @Test
    public void testJust() {
        Observable<Integer> firstObservable = Observable.just(1, 2, 4, 5, 7, 3);
        Observable<Integer> secondObservable = Observable.just(1, 3, 5, 7, 9);

        Observable<Integer> resultObservable = firstObservable.flatMap(x -> secondObservable,
                (x, y) -> {
                    if (x == y) {
                        return Observable.just(x);
                    } else {
                        return Observable.<Integer>empty();
                    }
                }).flatMap(obs -> obs);
        resultObservable.subscribe(System.out::println);
        resultObservable.toList().subscribe(System.out::println);
    }

    @Test
    public void testCollection() {
        List<Integer> firstList = Arrays.asList(1, 2, 4, 5, 7, 3);
        List<Integer> secondList = Arrays.asList(1, 3, 5, 7, 9);
        Observable.fromIterable(firstList)
                .flatMap(x -> Observable.fromIterable(secondList),
                    (x, y) -> {
                        if (x == y) {
                            return Observable.just(x); // (1)
                        } else {
                            return Observable.empty(); // (2)
                        }
                    }).flatMap(obs -> obs) // (3)
                    .subscribe(System.out::println);
    }

    @Test
    public void testCollection2() {
        List<Integer> firstList = Arrays.asList(1, 2, 4, 5, 7, 3);
        List<Integer> secondList = Arrays.asList(1, 3, 5, 7, 9);
        Observable.fromIterable(firstList)
                .flatMap(x -> Observable.fromIterable(secondList)
                        .filter(y -> (y == x))) // (1)
                .subscribe(System.out::println);
    }

    @Test
    public void testCollection3() {
        Integer[] firstArray = new Integer[] {1, 2, 4, 5, 7, 3};
        Integer[] secondArray = new Integer[] {1, 3, 5, 7, 9};
        List<Integer> integers = Observable.fromArray(firstArray)
                .flatMap(x -> Observable.fromArray(secondArray)
                        .filter(y -> (y == x)))
                .toList().blockingGet();
        System.out.println(integers);
    }
}

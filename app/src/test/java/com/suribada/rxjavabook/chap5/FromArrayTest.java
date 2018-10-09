package com.suribada.rxjavabook.chap5;

import org.junit.Test;

import io.reactivex.Observable;

/**
 * Created by Noh.Jaechun on 2018. 8. 2..
 */
public class FromArrayTest {

    @Test
    public void testArray() {
        Observable.fromArray().subscribe(System.out::println, System.err::println, // (1)
                () -> System.out.println("onCompleted")); // (1) 끝
        Observable.fromArray(new Integer[] {}).subscribe(System.out::println, // (2) 시작
                System.err::println,
                () -> System.out.println("onCompleted2")); // (2) 끝
        Observable.fromArray(1).subscribe(System.out::println); // (3)
        Observable.fromArray(2, 3).subscribe(System.out::println); // (4)
        Observable.fromArray(new Integer[] {4, 5}).subscribe(System.out::println); // (5)
        Observable.fromArray(new int[] {6, 7}).subscribe(System.out::println); // (6) 기대 결과는?
        int[] values  = new int[] {8, 9};
        Class<?> clazz = int[].class;
        System.out.println(values.getClass().getName());
        System.out.println(values.toString());
    }
}

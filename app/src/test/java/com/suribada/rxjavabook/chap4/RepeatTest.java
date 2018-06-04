package com.suribada.rxjavabook.chap4;

import android.os.SystemClock;

import com.suribada.rxjavabook.chap1.Bookmark;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;

/**
 * 클래스로더 캐시 때문에 한꺼번에 테스트 실행할 때와 각각 실행할 때가 속도 차이가 많다.
 *
 * Created by lia on 2018-05-23.
 */
public class RepeatTest {

    @Test
    public void testRanges() {
        long current = System.currentTimeMillis();
        for (int i = 0; i <= 9; i++) {
            if (i % 2 == 0) {
                continue;
            }
            for (int j = 0; j <= 9 ; j++) {
                int j2 = j * 10; // (1) 시작
                if (j == 1 || j == 3) {
                    j2 = j * 100;
                } // (1) 끝
                for (int k = 0; k <= 9 ; k++) {
                    if (k % 3 == 0) {
                        continue;
                    }
                    System.out.println(i + j2 + k * 1000);
                }
            }
        }
        System.out.println("elapsed 1=" + (System.currentTimeMillis() - current));
    }

    @Test
    public void testRangesRx() {
        long current = System.currentTimeMillis();
        Observable.range(0, 10)
                .filter(x -> x % 2 > 0)
                .flatMap(x ->
                        Observable.range(0, 10).map(y -> {
                            if (y == 1 || y == 3) { // (1) 시작
                                return y * 100;
                            } else {
                                return y * 10;
                            } // (1) 끝
                        }).map(y -> x + y))
                .flatMap(value -> Observable.range(0, 10).filter(z -> z % 3 > 0)
                        .map(z -> value + z * 1000))
                .subscribe(System.out::println);
        System.out.println("elapsed 2=" + (System.currentTimeMillis() - current));
    }

    @Test
    public void testList() {
        long current = System.currentTimeMillis();
        List<Integer> firstList = Arrays.asList(0, 1, 2, 4, 5, 6, 9, 11, 15, 19);
        List<Integer> secondList = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> thirdList = Arrays.asList(0, 2, 3, 4, 7, 8, 9);
        for (Integer i : firstList) {
            if (i % 2 == 0) {
                continue;
            }
            for (Integer j : secondList) {
                int j2 = j * 10;
                if (j == 1 || j == 3) {
                    j2 = j * 100;
                }
                for (Integer k : thirdList) {
                    if (k % 3 == 0) {
                        continue;
                    }
                    System.out.println(i + j2 + k * 1000);
                }
            }
        }
        System.out.println("elapsed 3=" + (System.currentTimeMillis() - current));
    }

    @Test
    public void testListRx() {
        long current = System.currentTimeMillis();
        List<Integer> firstList = Arrays.asList(0, 1, 2, 4, 5, 6, 9, 11, 15, 19);
        List<Integer> secondList = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> thirdList = Arrays.asList(0, 2, 3, 4, 7, 8, 9);
        Observable.fromIterable(firstList)
                .filter(x -> x % 2 > 0)
                .flatMap(x ->
                        Observable.fromIterable(secondList).map(y -> {
                            if (y == 1 || y == 3) { // (1) 시작
                                return y * 100;
                            } else {
                                return y * 10;
                            } // (1) 끝
                        }).map(y -> x + y))
                .flatMap(value -> Observable.fromIterable(thirdList).filter(z -> z % 3 > 0)
                        .map(z -> value + z * 1000))
                .subscribe(System.out::println);
        System.out.println("elapsed 4=" + (System.currentTimeMillis() - current));
    }

    private void calculate(Observable<Integer> firstObs, Observable<Integer> secondObs,
                           Observable<Integer> thirdObs) {
        firstObs.filter(x -> x % 2 > 0)
            .flatMap(x ->
                    secondObs.map(y -> {
                        if (y == 1 || y == 3) { // (1) 시작
                            return y * 100;
                        } else {
                            return y * 10;
                        } // (1) 끝
                    }).map(y -> x + y))
            .flatMap(value -> thirdObs.filter(z -> z % 3 > 0)
                    .map(z -> value + z * 1000))
            .subscribe(System.out::println);
    }

    @Test
    public void loadCollection() {
        long current = System.currentTimeMillis();
        List<Integer> firstList = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        for (Integer i : firstList) {
            System.out.println(i);
        }
        System.out.println("elapsed 5-1=" + (System.currentTimeMillis() - current));
    }

    @Test
    public void diffRange() {
        long current = System.currentTimeMillis();
        Observable.range(0, 10)
                .subscribe(System.out::println);
//        Observable.range(2, 7)
//                .subscribe(System.out::println);
        System.out.println("elapsed 5=" + (System.currentTimeMillis() - current));
    }

    @Test
    public void diffCollection() {
        long current = System.currentTimeMillis();
        List<Integer> firstList = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        Observable.fromIterable(firstList)
                .subscribe(System.out::println);
        System.out.println("elapsed 6=" + (System.currentTimeMillis() - current));
    }

}

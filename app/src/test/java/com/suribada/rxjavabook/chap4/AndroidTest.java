package com.suribada.rxjavabook.chap4;

import org.junit.Test;

import io.reactivex.Observable;

/**
 * Created by lia on 2018-05-23.
 */
public class AndroidTest {

    @Test
    public void testRanges() {
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
    }

    @Test
    public void testRangesRx() {
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
    }


}

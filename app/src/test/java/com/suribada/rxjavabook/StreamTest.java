package com.suribada.rxjavabook;

import org.junit.Test;

import java.util.Arrays;

/**
 * Created by lia on 2018-01-22.
 */

public class StreamTest {

    @Test
    public void listStream() {
        Arrays.asList(1, 2, 4, 7, 8, 11, 14).stream()
                .filter(x -> x % 2 == 0)
                .map(x -> x * 100)
                .filter(x -> x <= 1000)
                .map(x -> findNearPrime(x))
                .forEach(System.out::println);

    }

    private int findNearPrime(Integer x) {
        return 997;
    }

}

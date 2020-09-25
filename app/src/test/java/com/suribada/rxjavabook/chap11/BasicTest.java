package com.suribada.rxjavabook.chap11;

import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class BasicTest {

    @Rule
    public RxSchedulerRule schedulerRule = new RxSchedulerRule();

    @Test
    public void test() {
        assertTrue(9 > 5);
    }
}

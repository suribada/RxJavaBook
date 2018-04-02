package com.suribada.rxjavabook.error;

import org.junit.Test;

import java.util.Date;

/**
 * Created by Naver on 2017. 10. 16..
 */

public class DateFormatterTest {

    @Test
    public void testFormatter() {
        String input = "2017-xx-30 02:20:20";
        DateFormatter formatter = new DateFormatter();
        formatter.getTimeInMillis(input)
                .onErrorReturnItem(0L)
                .subscribe(System.out::println);
        formatter.getTimeInMillis(input)
                .onErrorReturnItem(new Date().getTime())
                .subscribe(System.out::println);
    }

    @Test
    public void testFormatterBlockingGet() {
        String input = "2017-xx-30 02:20:20";
        DateFormatter formatter = new DateFormatter();
        long start = formatter.getTimeInMillis(input)
                .onErrorReturnItem(0L)
                .blockingGet();
        long end = formatter.getTimeInMillis(input)
                .onErrorReturnItem(new Date().getTime())
                .blockingGet();
        System.out.println("start=" + start + ", end=" + end);
    }

}

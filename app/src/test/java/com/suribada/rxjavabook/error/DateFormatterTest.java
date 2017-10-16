package com.suribada.rxjavabook.error;

import org.junit.Test;

import java.util.Date;

/**
 * Created by Naver on 2017. 10. 16..
 */

public class DateFormatterTest {

    @Test
    public void testFormatter() {
        DateFormatter formatter = new DateFormatter();
        formatter.getTimeInMillis("2017-02-02 02:20:20").subscribe(System.out::println);
//        formatter.getTimeInMillis("2017-xx-30 02:20:20")
//                .subscribe(System.out::println);
        formatter.getTimeInMillis("2017-xx-30 02:20:20")
                .onErrorReturnItem(0L)
                .subscribe(System.out::println);
        formatter.getTimeInMillis("2017-xx-30 02:20:20")
                .onErrorReturnItem(new Date().getTime())
                .subscribe(System.out::println);
    }
}

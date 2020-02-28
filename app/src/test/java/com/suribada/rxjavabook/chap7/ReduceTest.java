package com.suribada.rxjavabook.chap7;

import org.junit.Test;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class ReduceTest {

    @Test
    public void reduce() {
        Single<List<String>> single = Observable.just("신중섭", "하동현", "권태환")
                .reduceWith(() -> {
                    ArrayList<String> list = new ArrayList();
                    list.add("노재춘");
                    return list;
                }, (list, item) -> {
                    list.add(item);
                    return list;
                });
        single.subscribe(System.out::println);
        single.subscribe(System.out::println);
    }

    @Test
    public void diff() {
        Observable.just("신중섭", "하동현", "권태환")
                .reduceWith(ArrayList::new, (list, item) -> {
                    list.add(item);
                    return list;
                }).subscribe(System.out::println);
        Observable.just("신중섭", "하동현", "권태환")
                .collect(ArrayList::new, (list, item) -> list.add(item))
                .subscribe(System.out::println);
    }

    @Test
    public void assign() {
        List<String> list = new ArrayList<>();
        String[] values = {"신중섭", "하동현", "권태환"};
        for (String item: values) {
            list.add(item);
            list = list; // (1)
        }
        System.out.println(list);
    }

    @Test
    public void immutable() {
        String text = "가나다라마바사";
        String subtext = text.substring(1, 4); // (1)
        System.out.println(text + ", " + subtext); // (2)
        BigDecimal bigDecimal = new BigDecimal("3.1");
        BigDecimal pi = bigDecimal.add(new BigDecimal("0.041592")); // (3)
        System.out.println(bigDecimal + ", " + pi); // (4)
    }

    @Test
    public void immutable2() {
        String text = "가나다라마바사";
        text = text.substring(1, 4); // (1)
        BigDecimal pi = new BigDecimal("3.1");
        pi = pi.add(new BigDecimal("0.041592")); // (2)
    }
}

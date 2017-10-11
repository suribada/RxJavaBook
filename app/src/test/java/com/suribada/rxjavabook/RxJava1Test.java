package com.suribada.rxjavabook;

import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by lia on 2017-10-12.
 */

public class RxJava1Test {
    private List<String> list = Arrays.asList("Android", "iOS", "Bada");

    @Test
    public void testIterator() {
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            if (next.length() > 3) {
                String value =  "OS:" + next;
                showText(value);
            }
        }
    }

    @Test
    public void testObservable() {
        Observable.fromIterable(list)
            .filter(value -> value.length() > 3)
            .map(os -> "OS:" + os)
            //.subscribe(value -> showText(value));
            .subscribe(this::showText);
    }

    private void showText(String input) {
        System.out.println("input=" + input);
    }
}

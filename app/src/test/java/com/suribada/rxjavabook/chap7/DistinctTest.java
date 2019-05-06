package com.suribada.rxjavabook.chap7;

import org.junit.Test;

import java.util.ArrayList;

import io.reactivex.Observable;

public class DistinctTest {

    @Test
    public void testDistinct() {
        Observable.just("이 효근", "노재춘", "강 사 룡", "이 효 근", "이효근", "노 재춘", "원빈")
                .distinct(name -> name.replaceAll("\\s+","")) // (1)
                .subscribe(System.out::println);
        System.out.println("----");
        Observable.just("이 효근", "노재춘", "강 사 룡", "이 효 근", "이효근", "노 재춘", "원빈")
                .distinct(name -> name.length())
                .subscribe(System.out::println);
    }

    @Test
    public void testDistinctCollection() {
        Observable.just("하동현", "신중섭", "신중섭", "하동현", "권태환")
                .distinct(x -> x, () -> new ArrayList<Object>())
                .subscribe(System.out::println);
    }
}

class Quiz {
    String question;
    String answer;
}

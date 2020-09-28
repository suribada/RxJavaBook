package com.suribada.rxjavabook.chap8;

import androidx.core.util.Pair;

import org.junit.Test;

import io.reactivex.rxjava3.core.Observable;

public class GroupByTest {

    @Test
    public void groupBy() {
        Observable<Student> obs = Observable.just(
                Student.create("노재춘", 61),
                Student.create("강사룡", 85),
                Student.create("이효근", 72),
                Student.create("하동현", 68),
                Student.create("권태환", 78),
                Student.create("이창신", 91),
                Student.create("최태용", 80),
                Student.create("최희탁", 77));
        obs.groupBy(student -> {
            if (student.getScore() >= 90) { // (1) 시작
                return "A";
            }
            if (student.getScore() >= 80) {
                return "B";
            }
            if (student.getScore() >= 70) {
                return "C";
            }
            if (student.getScore() >= 60) {
                return "D";
            }
            return "F"; // (1) 끝
        }, Student::getName) // (2)
                .flatMapSingle(grouped -> grouped.toList()
                        .map(list -> Pair.create(grouped.getKey(), list))) // (3)
                .subscribe(System.out::println);
    }
}

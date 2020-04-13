package com.suribada.rxjavabook.chap7;

import androidx.annotation.NonNull;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;

import io.reactivex.rxjava3.core.Observable;

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

    @Test
    public void hashTest() {
        HashSet<Quiz> quizHashSet = new HashSet<>();
        System.out.println(quizHashSet.add(new Quiz("Q1", "A1")));
        System.out.println(quizHashSet.add(new Quiz("Q1", "A1")));
    }

    @Test
    public void distinctTest() {
        Observable.just(new Quiz("Q1", "A1"), new Quiz("Q1", "A1"),
                new Quiz("Q2", "A1"), new Quiz("Q2", "A2"))
                .distinct()
                .subscribe(System.out::println);
    }

    @Test
    public void distinctKeyTest() {
        Observable.just(new Quiz("Q1", "A1"), new Quiz("Q1", "A1"),
                new Quiz("Q2", "A1"), new Quiz("Q2", "A2"))
                .distinct(quiz -> quiz.answer)
                .subscribe(System.out::println);
    }

    @Test
    public void testDistinctUntilChanged_basic() {
        Observable.just(new Quiz("Q1", "A1"), new Quiz("Q1", "A1"),
                new Quiz("Q2", "A1"), new Quiz("Q2", "A2"))
                .distinctUntilChanged(quiz -> quiz.answer)
                .subscribe(System.out::println);
        System.out.println("------");
        Observable.just(new Quiz("Q1", "A1"), new Quiz("Q1", "A1"),
                new Quiz("Q2", "A1"), new Quiz("Q2", "A2"))
                .distinctUntilChanged((previous, current) -> previous.answer.equals(current.answer))
                .subscribe(System.out::println);
    }

    @Test
    public void testDistinctUntilChanged() {
        Observable.just( new Profile("강", "사룡"), new Profile("권", "태환"),
                new Profile("태환", "권"), new Profile("신", "중섭"))
                .distinctUntilChanged((previous, current) -> previous.equals(current) ||
                        (previous.firstName.equals(current.lastName)
                        && previous.lastName.equals(current.firstName)))
                .subscribe(System.out::println);
    }

    @Test
    public void testDistinctUntilChanged_problem() {
        Observable<String> obs = Observable.create(emitter -> {
            String value = "0123";
            value.replace('0', 'A');
            emitter.onNext(value);
            value.replace('1', 'B');
            emitter.onNext(value);
            value.replace('2', 'C');
            emitter.onNext(value);
            emitter.onComplete();
        });
        obs.distinctUntilChanged()
            .subscribe(System.out::println);
    }

    @Test
    public void testDistinctUntilChanged_problem2() {
        Observable<StringBuilder> obs = Observable.create(emitter -> {
            StringBuilder value = new StringBuilder("0123");
            value.append("4");
            emitter.onNext(value); // (1)
            value.append("5");
            emitter.onNext(value); // (2)
            value.append("6");
            emitter.onNext(value); // (3)
            emitter.onComplete();
        });
        obs.distinctUntilChanged() // (4)
            .subscribe(System.out::println); // (5)
        //obs.subscribe(System.out::println);
    }

    @Test
    public void testDistinctUntilChanged_problem2_fixed() {
        Observable<StringBuilder> obs = Observable.create(emitter -> {
            StringBuilder value = new StringBuilder("0123");
            value.append("4");
            emitter.onNext(value);
            value.append("5");
            emitter.onNext(value);
            value.append("6");
            emitter.onNext(value);
            emitter.onComplete();
        });
        obs.map(StringBuilder::toString) // (1)
            .distinctUntilChanged()
            .subscribe(System.out::println);
    }
}

class Quiz {
    String question;
    String answer;

    Quiz(@NonNull String question, @NonNull String answer) {
        this.question = question;
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Quiz quiz = (Quiz) o;

        if (!question.equals(quiz.question)) return false;
        return answer.equals(quiz.answer);
    }

    @Override
    public int hashCode() {
        int result = question.hashCode();
        result = 31 * result + answer.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}

class Profile {
    String firstName;
    String lastName;

    public Profile(@NonNull String firstName, @NonNull String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Profile profile = (Profile) o;

        if (!firstName.equals(profile.firstName)) return false;
        return lastName.equals(profile.lastName);
    }

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
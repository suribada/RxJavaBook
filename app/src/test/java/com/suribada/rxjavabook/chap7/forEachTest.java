package com.suribada.rxjavabook.chap7;

import org.junit.Test;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.internal.functions.Functions;

public class forEachTest {

    @Test
    public void forEachWhile() {
        Observable.just(3, 1, 4, 1, 5, 9, 2)
                .forEachWhile(value -> {
                    System.out.println("value=" + value); // (1)
                    return value <= 5; // (2)
                }, Functions.emptyConsumer(), () -> System.out.println("onComplete"));
    }

    @Test
    public void takeUntil() {
        Observable.just(3, 1, 4, 1, 5, 9, 2)
                .takeUntil(value -> value > 5) // (1)
                .subscribe(value -> System.out.println("value=" + value),
                        Functions.emptyConsumer(), () -> System.out.println("onComplete"));
    }

    @Test
    public void forEachWhile_withWorkers() {
        getCandidates()
                .concatMap(candidate -> apply(candidate))
                .forEachWhile(pass -> pass); // (1)
    }

    @Test
    public void takeUntil_withWorkers() {
        getCandidates()
                .concatMap(candidate -> apply(candidate))// (1)
                .takeWhile(pass -> pass) // (2)
                .subscribe(); // (3)
    }

    private int count = 0;
    private Observable<Boolean> apply(Candidate candidate) {
        System.out.println(candidate.name);
        return Observable.just(++count <= 5);
    }

    private Observable<Candidate> getCandidates() {
        return Observable.just(new Candidate("노재춘"), new Candidate("김인태"), new Candidate("김진태"),
                new Candidate("김진태2"), new Candidate("김진태3"), new Candidate("김진태4"),
                new Candidate("김진태5"));
    }

    class Candidate {

        private String name;

        Candidate(String name) {
            this.name =  name;
        }
    }
}

package com.suribada.rxjavabook.chap5;

import org.junit.Test;

import io.reactivex.Observable;

public class CreateTest {

    @Test
    public void testGenerate() {
        Observable.<Observable<Integer>, Integer>generate(() -> 10, (startNo, emmiter) -> {
            int toNo = startNo + 10;
            if (toNo < 100) {
                emmiter.onNext(Observable.range(startNo, 10));
            } else {
                emmiter.onComplete();
            }
            return toNo;
        }).flatMap(obs -> obs).subscribe(System.out::println);
    }

}

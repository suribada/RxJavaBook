package com.suribada.rxjavabook.chap5;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.Single;

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

    @Test
    public void testInnerCreate() {
        Observable.just("noh", "jae", "chun")
                .flatMap(name -> Observable.<String>create(emitter -> {
                    emitter.onNext("char=" + name.charAt(0));
                }).concatMap(value -> Observable.range(0, value.length())))
                        .subscribe(System.out::println);
    }

}

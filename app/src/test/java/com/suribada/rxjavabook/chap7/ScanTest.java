package com.suribada.rxjavabook.chap7;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.junit.Test;

import com.suribada.rxjavabook.SystemClock;

import io.reactivex.rxjava3.core.Observable;

public class ScanTest {

    @Test
    public void testScan_default() {
        Observable.just(1, 2, 3, 4)
                .scan(-5, (sum, current) -> sum + current)
                .subscribe(System.out::println);
        System.out.println("-----");
        Observable.just(new VoteCount(10, 20),
                new VoteCount(15, 100),
                new VoteCount(50, 15))
                .scan(new VoteCount(0, 20),
                        (sum, current) -> {
                            sum.add(current);
                            return sum;
                        })
                .subscribe(System.out::println);
        System.out.println("-----");
        // 컴파일 에러
//        Observable.just("신중섭", "하동현", "권태환")
//                .scan(new ArrayList<>(), (list, item) -> list.add(item)).subscribe(System.out::println);
        Observable.just("신중섭", "하동현", "권태환")
                .scan(new ArrayList<>(), (list, item) -> {
                    list.add(item);
                    return list;
                }).subscribe(System.out::println);
        System.out.println("-----");
        Observable.just("노재춘|", "이창신|", "최희탁|")
                .scan(new StringBuilder(), (result, item) -> result.append(item))
                .map(StringBuilder::toString)
                .subscribe(System.out::println);
        System.out.println("-----");
        Observable.just("신중섭", "하동현", "권태환")
                .scanWith(ArrayList::new, (list, item) -> {
                    list.add(item);
                    return list;
                }).subscribe(System.out::println);
        Observable.just("신중섭", "하동현", "권태환")
                .scanWith(() -> {
                    ArrayList<String> list = new ArrayList();
                    list.add("노재춘");
                    return list;
                }, (list, item) -> {
                    list.add(item);
                    return list;
                }).subscribe(System.out::println);
    }

    @Test
    public void testScan() {
        getVoteCountObservable().scan((voteCount, current) -> {
            voteCount.add(current);
            return voteCount;
        }).subscribe(System.out::println);
        SystemClock.sleep(12000);
    }

    private Observable<VoteCount> getVoteCountObservable() {
        return Observable.create(emitter -> {
            emitter.onNext(new VoteCount(100, 70));
            SystemClock.sleep(2000);
            emitter.onNext(new VoteCount(40, 30));
            SystemClock.sleep(2000);
            emitter.onNext(new VoteCount(120, 200));
            SystemClock.sleep(2000);
            emitter.onNext(new VoteCount(30, 80));
            SystemClock.sleep(2000);
            emitter.onNext(new VoteCount(89, 60));
            SystemClock.sleep(2000);
        });
    }
}

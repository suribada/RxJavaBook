package com.suribada.rxjavabook.chap8;

import androidx.core.util.Pair;

import com.suribada.rxjavabook.SystemClock;
import com.suribada.rxjavabook.chap5.SearchResult;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

public class SwitchMapTest {

    /**
     * 업스트림에서 이벤트가 오는 순간 기존 매핑 Observable이 취소되기 때문에 40에 대해서만 값이 출력된다.
     * switchMap에 적합하지 않다.
     */
    @Test
    public void switchMap_invalid() {
        Observable.just(2, 5, 17, 40)
                .switchMap(v -> Observable.interval(v, TimeUnit.MILLISECONDS) // (1)
                        .map(i -> (i + 1) * v + "밀리초 경과"))
                .subscribe(System.out::println);
        SystemClock.sleep(3000);
    }

    @Test
    public void search_flatMap() {
        getKeywordObservable()
                .flatMap(keyword -> searchApi(keyword) // (1)
                        .subscribeOn(Schedulers.io()))
                .subscribe(System.out::println);
        searchSubject.onNext("안드로이드");
        searchSubject.onNext("코틀린");
        SystemClock.sleep(5000);
    }

    @Test
    public void search_switchMap() {
        getKeywordObservable()
                .switchMap(keyword -> searchApi(keyword)
                        .subscribeOn(Schedulers.io()))
                .subscribe(System.out::println);
        searchSubject.onNext("안드로이드");
        searchSubject.onNext("코틀린");
        SystemClock.sleep(5000);
    }

    private Subject<String> searchSubject = PublishSubject.create();

    private Observable<String> getKeywordObservable() {
        return searchSubject;
    }

    private Observable<SearchResult> searchApi(String keyword) {
        return Observable.create(emitter -> {
            SystemClock.sleep(1500);
            emitter.onNext(new SearchResult(keyword));
        });
    }

    @Test
    public void search_sameThread() {
        getPopularKeywords()
                .subscribeOn(Schedulers.io()) // (1)
                .switchMap(keyword -> searchApi(keyword)) // (2
                .subscribe(System.out::println);
        SystemClock.sleep(10000);
    }

    @Test
    public void search_differentThread() {
        getPopularKeywords()
                .subscribeOn(Schedulers.io())
                .switchMap(keyword -> searchApi(keyword).subscribeOn(Schedulers.io()))
                .subscribe(System.out::println);
        SystemClock.sleep(10000);
    }

    private Observable<String> getPopularKeywords() {
        return Observable.create(emitter -> {
            emitter.onNext("자바");
            SystemClock.sleep(1000);
            emitter.onNext("코틀린");
            SystemClock.sleep(1000);
            emitter.onNext("안드로이드");
            emitter.onComplete();
        });
    }

    @Test
    public void switchOnNext_invalid() {
        Observable.switchOnNext(
                Observable.fromArray( // (1) 끝
                        Observable.interval(2, TimeUnit.SECONDS).map(v -> "2초:" + v),
                        Observable.interval(3, TimeUnit.SECONDS).map(v -> "3초:" + v),
                        Observable.interval(4, TimeUnit.SECONDS).map(v -> "4초:" + v)
                )) // (1) 끝
                .subscribe(System.out::println);
        SystemClock.sleep(30000);
    }

    @Test
    public void switchOnNext_valid() {
        Observable.switchOnNext(
                Observable.interval(150, TimeUnit.MILLISECONDS) // (1)
                        .map(v -> Observable.interval(50, TimeUnit.MILLISECONDS) // (2) 시작
                                .map(inner -> Pair.create(v, inner))
                        ) // (2) 끝
                ).subscribe(System.out::println);
        SystemClock.sleep(3000);
    }

    @Test
    public void switchOnNext_valid_equivalent() {
        Observable.interval(150, TimeUnit.MILLISECONDS)
                .switchMap(v -> Observable.interval(50, TimeUnit.MILLISECONDS)
                        .map(inner -> Pair.create(v, inner)))
                .subscribe(System.out::println);
        SystemClock.sleep(3000);
    }


}

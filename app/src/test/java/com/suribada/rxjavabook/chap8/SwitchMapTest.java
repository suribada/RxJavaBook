package com.suribada.rxjavabook.chap8;

import com.suribada.rxjavabook.SystemClock;
import com.suribada.rxjavabook.chap5.SearchResult;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
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
                .switchMap(v -> Observable.interval(v, TimeUnit.MILLISECONDS)
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
            SystemClock.sleep(1000);
            emitter.onNext(new SearchResult(keyword));
        });
    }


}

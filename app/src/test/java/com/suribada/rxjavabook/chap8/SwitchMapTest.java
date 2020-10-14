package com.suribada.rxjavabook.chap8;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;

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

}

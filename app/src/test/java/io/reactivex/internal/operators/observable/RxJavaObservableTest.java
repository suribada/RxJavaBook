package io.reactivex.internal.operators.observable;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.internal.observers.LambdaObserver;

/**
 * Created by Naver on 2017. 10. 13..
 */
public class RxJavaObservableTest {

    private List<String> list = Arrays.asList("Android", "iOS", "Bada");


    @Test
    public void simpleTest() {
        Observable.fromIterable(list)
                .filter(value -> value.length() > 3)
                .map(os -> "OS:" + os)
                .subscribe(this::showText);

        new ObservableMap<>(
                new ObservableFilter<String>(
                        new ObservableFromIterable(list),
                        (value -> value.length() > 3)),
                os -> "OS:" + os);

        Observer<String> observer = new LambdaObserver<String>(this::showText, null,
                null, null);

        new ObservableFilter<String>(
                new ObservableFromIterable(list), (value -> value.length() > 3)
        ).subscribeActual(new ObservableMap.MapObserver(observer, os -> "OS:" + os));

        // 최종
        new ObservableFromIterable(list).subscribeActual(
                new ObservableFilter.FilterObserver<String>(
                        new ObservableMap.MapObserver(observer, os -> "OS:" + os),
                        (value -> value.length() > 3)));

    }

    private void showText(String input) {
        System.out.println("input=" + input);
    }


}

package io.reactivex.internal.operators.observable;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.ArrayCompositeDisposable;
import io.reactivex.internal.functions.Functions;
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

        new ObservableMap<>( // (1)
                new ObservableFilter<String>( // (2)
                        new ObservableFromIterable(list), // (3)
                        (value -> value.length() > 3)), // (4)
                os -> "OS:" + os) // (5)
                .subscribe(this::showText);

        Observer<String> observer = new LambdaObserver<>(this::showText,
                Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION, Functions.emptyConsumer());

        new ObservableFilter<String>(
                    new ObservableFromIterable(list), // (1)
                    (value -> value.length() > 3) // (2)
                ).subscribeActual(
                    new ObservableMap.MapObserver(observer, os -> "OS:" + os)
                ); // (3)

        // 최종
        new ObservableFromIterable(list)
                .subscribeActual(
                        new ObservableFilter.FilterObserver<String>( // (1)
                            new ObservableMap.MapObserver(observer, os -> "OS:" + os), // (2)
                            (value -> value.length() > 3)
                        )
                ); // (3)
        ArrayCompositeDisposable compositeDisposable = new ArrayCompositeDisposable(3);
        CompositeDisposable compositeDisposable1 = new CompositeDisposable();

    }

    private void showText(String input) {
        System.out.println("input=" + input);
    }

}

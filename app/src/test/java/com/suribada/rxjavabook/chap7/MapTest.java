package com.suribada.rxjavabook.chap7;

import com.suribada.rxjavabook.chap8.State;
import com.suribada.rxjavabook.model.Animal;
import com.suribada.rxjavabook.model.Cat;
import com.suribada.rxjavabook.model.Dog;
import com.suribada.rxjavabook.model.ViewState;

import org.junit.Test;

import io.reactivex.rxjava3.core.Observable;

public class MapTest {

    /**
     * ClassCastException 발생
     */
    @Test
    public void testCast() {
        Observable.just(new Cat()).cast(Dog.class)
                .subscribe(System.out::println,
                        Throwable::printStackTrace,
                        () -> System.out.println("onComplete"));
    }

    @Test
    public void testCast2() {
        /*
        Observable.just(new Cat()).map(cat -> (Dog) cat) // 컴파일 에러
                .subscribe(System.out::println,
                        Throwable::printStackTrace,
                        () -> System.out.println("onComplete"));
        */
    }

    @Test
    public void testCast3() {
         Observable.merge(Observable.just(new Cat()), Observable.just(new Dog("진순이")))
                 .subscribe(System.out::println);
    }

    @Test
    public void testOfType() {
        Observable.just(new Cat()).ofType(Dog.class)
                .subscribe(System.out::println,
                        Throwable::printStackTrace,
                        () -> System.out.println("onComplete"));
    }

    @Test
    public void testOfType2() {
        Observable<ViewState> stateObservable = Observable.create(emitter -> { // (1) 시작
            emitter.onNext(new ViewState.Loading());
            emitter.onNext(new ViewState.Result("suribada"));
            emitter.onNext(new ViewState.Result("horseridingking"));
            emitter.onNext(new ViewState.Error(new IllegalArgumentException("not allowed")));
        }); // (1) 끝
        stateObservable.ofType(ViewState.Loading.class) // (2)
                .subscribe(state -> showText(state.toString()));
        stateObservable.ofType(ViewState.Result.class) // (3)
                .subscribe(System.out::println);
    }

    private void showText(String text) {
        System.out.println(text);
    }
}

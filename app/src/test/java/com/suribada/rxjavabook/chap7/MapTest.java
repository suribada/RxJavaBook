package com.suribada.rxjavabook.chap7;

import com.suribada.rxjavabook.chap5.SearchResult;
import com.suribada.rxjavabook.chap8.State;
import com.suribada.rxjavabook.model.Animal;
import com.suribada.rxjavabook.model.Cat;
import com.suribada.rxjavabook.model.Dog;
import com.suribada.rxjavabook.model.ViewState;

import org.junit.Test;

import java.util.Optional;

import io.reactivex.rxjava3.core.Observable;

public class MapTest {

    @Test
    public void map() {
        Observable.just("Java", "Kotlin", "C++", "Go", "Python")
                .map(String::length) // (1)
                .subscribe(System.out::println);

    }
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

    @Test
    public void mapOptional() {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .mapOptional(value -> {
                    if (value % 2 == 0) { // (1) 시작
                        return Optional.of("값=" + value);
                    } // (1) 끝
                    return Optional.empty(); // (2)
                })
                .subscribe(System.out::println);

        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .mapOptional(value -> (value % 2 == 0) ? Optional.of("값=" + value) : Optional.empty())
                .subscribe(System.out::println);
    }

    @Test
    public void mapOptionalAlternative() {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .filter(value -> value % 2 == 0)
                .map(value -> "값=" + value)
                .subscribe(System.out::println);
    }

    private SearchResult search(String keyword) {
        if (keyword.trim().length() == 0) { // (1) 시작
            return null;
        } // (1) 끝
        return new SearchResult(keyword);
    }

    @Test
    public void searchKeywords() {
        Observable.just("Java", "  ", "Kotlin")
                .map(keyword -> search(keyword))
                .subscribe(System.out::println, System.err::println);
    }

    private Observable<SearchResult> search2(String keyword) { // (1)
        if (keyword.trim().length() == 0) { // (2) 시작
            return Observable.empty();
        } // (2) 끝
        return Observable.just(new SearchResult(keyword)); // (3)
    }

    @Test
    public void searchKeywords2() {
        long start = System.currentTimeMillis();
        Observable.just("Java", "  ", "Kotlin")
                .flatMap(keyword -> search2(keyword)) // (4)
                .subscribe(System.out::println, System.err::println);
        System.out.println("time=" + (System.currentTimeMillis() - start));
    }

    private SearchResult search3(String keyword) {
        if (keyword.trim().length() == 0) {
            return null;
        }
        return new SearchResult(keyword);
    }

    @Test
    public void searchKeywords3() {
        long start = System.currentTimeMillis();
        Observable.just("Java", "  ", "Kotlin")
                .mapOptional(keyword -> {
                    SearchResult searchResult = search(keyword);
                    return (searchResult == null) ? Optional.empty() : Optional.of(searchResult); // (1)
                })
                .subscribe(System.out::println, System.err::println);
        System.out.println("time=" + (System.currentTimeMillis() - start));
    }

    private Optional<SearchResult> search4(String keyword) {
        if (keyword.trim().length() == 0) {
            return Optional.empty();
        }
        return Optional.of(new SearchResult(keyword));
    }

    @Test
    public void searchKeywords4() {
        long start = System.currentTimeMillis();
        Observable.just("Java", "  ", "Kotlin")
                .mapOptional(keyword -> search4(keyword))
                .subscribe(System.out::println, System.err::println);
        System.out.println("time=" + (System.currentTimeMillis() - start));
    }
}

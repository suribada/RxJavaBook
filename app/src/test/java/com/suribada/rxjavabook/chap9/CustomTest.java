package com.suribada.rxjavabook.chap9;

import com.suribada.rxjavabook.SystemClock;
import com.suribada.rxjavabook.chap8.Sale;
import com.suribada.rxjavabook.chap8.Student;

import org.junit.Test;

import hu.akarnokd.rxjava3.math.MathObservable;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CustomTest {

    @Test
    public void not_compose() {
        getSales()
                .doOnSubscribe(ignored -> showProgress()) // (1) 시작
                .doOnTerminate(() -> hideProgress()) // (1) 끝
                .subscribe(System.out::println);
        getStudents()
                .doOnSubscribe(ignored -> showProgress()) // (2) 시작
                .doOnTerminate(() -> hideProgress()) // (2) 끝
                .subscribe(System.out::println);
    }

    private void showProgress() {
        System.out.println("진행중");
    }

    private void hideProgress() {
        System.out.println("이제 그만");
    }

    private Observable<Sale> getSales() {
        return Observable.create(emitter -> {
            SystemClock.sleep(2000);
            emitter.onNext(Sale.create(1000));
            emitter.onNext(Sale.create(2000));
            emitter.onComplete();
        });
    }

    private Observable<Student> getStudents() {
        return Observable.create(emitter -> {
            SystemClock.sleep(1000);
            emitter.onNext(new Student("이효근"));
            emitter.onNext(new Student("남생이"));
            emitter.onComplete();
        });
    }

    @Test
    public void compose() {
        getSales()
                .compose(progress()) // (1)
                .subscribe(System.out::println);
        getStudents()
                .compose(progress()) // (2)
                .subscribe(System.out::println);
    }

    private <T> ObservableTransformer<T, T> progress() {
        return upstream -> upstream
                .doOnSubscribe(ignored -> showProgress()) // (1) 시작
                .doOnTerminate(() -> hideProgress()); // (1) 끝
    }

    @Test
    public void to() {
        Observable<Integer> obs = Observable.fromArray(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9) // (1) 시작
                .map(v -> v * 100); // (1) 끝
        MathObservable.sumInt(obs) // (2)
                .subscribe(sum -> System.out.println("sum=" + sum));

        Observable.fromArray(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9)
                .map(v -> v * 100)
                .to(MathObservable::sumInt) // (1)
                .subscribe(sum -> System.out.println("sum=" + sum));

        obs.to(MathObservable::min)
                .subscribe(min -> System.out.println("min=" + min));
        obs.to(MathObservable::max)
                .subscribe(max -> System.out.println("max=" + max));
        obs.to(MathObservable::averageFloat)
                .subscribe(average -> System.out.println("average=" + average));

        // rxjava extras의 Strings에는 join, decode, concat이 있다.
    }

    private Observable<User> getUsers() {
        return Observable.just(
                new User("노재춘", "888888-1234567", "010-2257-1234", 22, "경기 용인시 기흥구 보정로 87"),
                new User("이효근", "888881-1234567", "010-3257-1234", 23, "경기 용인시 기흥구 보정로 88"),
                new User("강사룡", "888882-1234567", "010-4257-1234", 24, "경기 용인시 기흥구 보정로 89"),
                new User("권태환", "888883-1234567", "010-5257-1234", 25, "경기 용인시 기흥구 보정로 90")
        );
    }

    @Test
    public void lift() {
        getUsers().lift(downstream -> new Observer<User>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(User user) {

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }


}

package com.suribada.rxjavabook.chap9;

import com.suribada.rxjavabook.SystemClock;
import com.suribada.rxjavabook.chap8.Sale;
import com.suribada.rxjavabook.chap8.Student;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava3.math.MathObservable;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
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

    @Test
    public void lift() {
        Disposable disposable = getUsers().lift(downstream -> new Observer<User>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                downstream.onSubscribe(d); // (1)
            }

            @Override
            public void onNext(User user) {
                System.out.println("onNext user=" + user);
                if (user.isInvalid()) { // (2) 시작
                    reportUser(user);
                    return;
                } // (2) 끝
                downstream.onNext(SecuredUser.create(user)); // (3)
            }

            @Override
            public void onError(@NonNull Throwable e) {
                downstream.onError(e); // (4)
            }

            @Override
            public void onComplete() {
                downstream.onComplete(); // (5)
            }

        }).subscribe(System.out::println);

        SystemClock.sleep(2500);
        disposable.dispose();
        SystemClock.sleep(6000);
    }

    /**
     * downStream.onSubscribe()를 호출하지 않기 때문에
     * 소스 스트림까지 dispose 되지 않는 문제 있음
     * DisposableObserver는 마지막에만 사용할 것
     */
    @Test
    public void lift_withDisposableObserver() {
        Disposable disposable = getUsers().lift(downstream -> new DisposableObserver<User>() { // (1)

            @Override
            public void onNext(User user) {
                System.out.println("onNext user=" + user);
                if (user.isInvalid()) {
                    reportUser(user);
                    return;
                }
                if (!isDisposed()) { // (2) 끝
                    downstream.onNext(SecuredUser.create(user));
                } // (2) 끝
            }

            @Override
            public void onError(@NonNull Throwable e) {
                downstream.onError(e);
            }

            @Override
            public void onComplete() {
                downstream.onComplete();
            }
        }).subscribe(System.out::println);

        SystemClock.sleep(2500);
        disposable.dispose();
        SystemClock.sleep(8000);
    }

    private class CustomObserver<T extends User> implements Observer<T>, Disposable {

        final Observer<? super SecuredUser> downstream;
        Disposable upstream;

        public CustomObserver(Observer<? super SecuredUser> downstream) { // (1) 시작
            this.downstream = downstream;
        } // (1) 끝

        @Override
        public void onSubscribe(@NonNull Disposable d) {
            upstream = d; // (2)
            downstream.onSubscribe(this); // (3)
        }

        @Override
        public void onNext(@NonNull User user) {
            System.out.println("onNext user=" + user);
            if (user.isInvalid()) {
                reportUser(user);
                return;
            }
            if (!isDisposed()) {
                downstream.onNext(SecuredUser.create(user));
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
            downstream.onError(e);
        }

        @Override
        public void onComplete() {
            downstream.onComplete();
        }

        @Override
        public void dispose() { // (4) 시작
            upstream.dispose();
        } // (4) 끝

        @Override
        public boolean isDisposed() { // (5) 시작
            return upstream.isDisposed();
        } // (5) 끝

    }

    @Test
    public void lift_withCustomOperator() {
        Disposable disposable = getUsers().lift(downstream -> new CustomObserver<>(downstream)) // (1)
                .subscribe(System.out::println);
        SystemClock.sleep(3500);
        disposable.dispose();
        SystemClock.sleep(8000);

    }

    @Test
    public void lift_withCustomOperator_lambda() {
        Disposable disposable = getUsers().lift(CustomObserver::new)
                .subscribe(System.out::println);
        SystemClock.sleep(3500);
        disposable.dispose();
        SystemClock.sleep(8000);

    }

    private Observable<User> getUsers() {
        return Observable.just(
                new User("노재춘", "888888-1234567", "010-2257-1234", 22, "용인시 기흥구 보정로 87"),
                new User("이효근", "888881-1234567", "010-3257-1234", 23, "용인시 수지구 떡볶로 88"),
                new User("강사룡", "888882-1234567", "010-4257-1234", 24, "성남시 분당구 불정로 89"),
                new User("권태환", "888883-1234567", "010-5257-1234", 25, "수원시 권선구 철길 90")
        ).zipWith(Observable.interval(1, TimeUnit.SECONDS).take(5), // (1)
                (user, i) -> user); // (2)
    }

    private void reportUser(User user) {
        System.out.println("report User=" + user);
    }


}

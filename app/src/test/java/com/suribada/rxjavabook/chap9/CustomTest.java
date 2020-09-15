package com.suribada.rxjavabook.chap9;

import com.suribada.rxjavabook.SystemClock;
import com.suribada.rxjavabook.chap8.Sale;
import com.suribada.rxjavabook.chap8.Student;

import org.junit.Test;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableTransformer;
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
}

package com.suribada.rxjavabook.chap8;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ConcatMapTest {

    /**
     * onComplete 이벤트가 없는 Observable 문제
     */
    @Test
    public void concatMap() {
        getDepartments()
                .concatMap(department -> getOutGoings4(department))
                .subscribe(System.out::println);
        SystemClock.sleep(2000);
    }

    private Observable<Department> getDepartments() {
        return Observable.just(new Department("iOS팀"),
                new Department("안드로이드팀"), new Department("FE팀"), new Department("서버팀"));
    }

    int i = 0;

    private Observable<OutGoing> getOutGoings(Department department) {
        return Observable.just(new OutGoing(++i), new OutGoing(++i),
                new OutGoing(++i), new OutGoing(++i), new OutGoing(++i));
    }

    @Test
    public void concatMap_thread() {
        long start = System.currentTimeMillis();
        getDepartments()
                .subscribeOn(Schedulers.io()) // (1)
                .concatMap(department ->
                        getOutGoings(department)
                                .subscribeOn(Schedulers.io()) // (2)
                )
                .doOnComplete(() -> System.out.println("elapsed=" + (System.currentTimeMillis() - start)))
                .subscribe(System.out::println);
        SystemClock.sleep(2000);
    }
    @Test
    public void concatMap_thread2() {
        long start = System.currentTimeMillis();
        getDepartments()
                .subscribeOn(Schedulers.io()) // (1)
                .concatMap(department -> getOutGoings(department))
                .doOnComplete(() -> System.out.println("elapsed=" + (System.currentTimeMillis() - start)))
                .subscribe(System.out::println);
        SystemClock.sleep(2000);
    }

    @Test
    public void concatMap_thread3() {
        long start = System.currentTimeMillis();
        getDepartments()
                .concatMap(department -> getOutGoings(department), 2, Schedulers.io()) // (1)
                .doOnComplete(() -> System.out.println("elapsed=" + (System.currentTimeMillis() - start)))
                .subscribe(System.out::println);
        SystemClock.sleep(2000);
    }

    /**
     * onComplete를 하지 않은 Observable이 있음
     */
    private Observable<OutGoing> getOutGoings4(Department department) {
        switch (department.getName()) {
            case "안드로이드팀":
                return Observable.create(emitter -> {
                    emitter.onNext(new OutGoing(2000));
                });
            case "iOS팀":
                return Observable.create(emitter -> {
                    emitter.onNext(new OutGoing(3000));
                    emitter.onComplete();
                });
            case "FE팀":
                return Observable.create(emitter -> {
                    emitter.onNext(new OutGoing(8000));
                    emitter.onComplete();
                });
            case "서버팀":
                return Observable.create(emitter -> {
                    emitter.onNext(new OutGoing(3000));
                    emitter.onComplete();
                });
        }
        throw new IllegalArgumentException();
    }

    /**
     * 1개의 스레드에서는
     * 업스트림에서 에러가 발생하면 tillTheEnd가 true나 false거나 결과가 동일하다.
     */
    @Test
    public void delayError() {
        getDepartmentsWithError()
                .concatMapDelayError(this::getOutGoings, false, Flowable.bufferSize())
                .subscribe(System.out::println, System.err::println);
    }

    /**
     * 에러 내보내는 Observable
     */
    private Observable<Department> getDepartmentsWithError() {
        return Observable.merge(Observable.just(new Department("안드로이드팀")),
                Observable.error(new IllegalArgumentException("IllegalArgument")),
                Observable.just(new Department("FE팀")),
                Observable.just(new Department("서버팀")));
    }

    /**
     * 매핑된 Observable에서 에러. 동일한 스레드 에러 즉시 보여준다.
     */
    @Test
    public void delayError_tillTheEndFalse() {
        getDepartments()
                .doOnNext(department -> System.out.println("upstream=" + department.toString()))
                .concatMapDelayError(department -> getOutGoingsWithError(department)
                        .doOnError(e -> System.err.println(e.toString())), false, Flowable.bufferSize())
                .subscribe(System.out::println, System.err::println);
    }

    /**
     * 별도 스레드에서는 에러가 바로 통지되지 않는다.
     */
    @Test
    public void delayError_tillTheEndFalse_withThread() {
        getDepartments()
                .doOnNext(department -> System.out.println("upstream=" + department.toString()))
                .concatMapDelayError(department -> getOutGoingsWithError(department).subscribeOn(Schedulers.io())
                        .doOnError(e -> System.err.println(e.toString())), false, Flowable.bufferSize())
                .subscribe(System.out::println, System.err::println);
        SystemClock.sleep(2000);
    }

    /**
     * 에러를 미룸
     */
    @Test
    public void delayError_tillTheEndTrue() {
        getDepartments()
                .concatMapDelayError(this::getOutGoingsWithError, true, Flowable.bufferSize())
                .subscribe(System.out::println, System.err::println);
    }

    /**
     * 에러를 내보내는 것
     */
    private Observable<OutGoing> getOutGoingsWithError(Department department) {
        switch (department.getName()) {
            case "안드로이드팀":
                return Observable.create(emitter -> {
                    emitter.onNext(new OutGoing(2000));
                    emitter.onNext(new OutGoing(100));
                    emitter.onError(new IllegalArgumentException());
                });
            case "iOS팀":
                return Observable.create(emitter -> {
                    emitter.onNext(new OutGoing(3000));
                    emitter.onComplete();
                });
            case "FE팀":
                return Observable.create(emitter -> {
                    emitter.onNext(new OutGoing(8000));
                    emitter.onComplete();
                });
            case "서버팀":
                return Observable.create(emitter -> {
                    emitter.onNext(new OutGoing( 7000));
                    emitter.onComplete();
                });
        }
        throw new IllegalArgumentException();
    }

    /**
     * 업스트림 에러. tillTheEnd는 기본값 true
     */
    @Test
    public void concatMapDelayWithThread() {
        Observable.interval(300, TimeUnit.MILLISECONDS).take(4)
                .concatWith(Observable.error(new IllegalArgumentException()))
                .doOnNext(v -> System.out.println("upstream next=" + v))
                .concatMapDelayError(v -> Observable.interval(100, TimeUnit.MILLISECONDS).take(3))
                .subscribe(v -> System.out.println("observer onNext=" + v),
                        System.err::println);
        SystemClock.sleep(5000);
    }

    /**
     * 업스트림 에러. tillTheEnd는 기본값 true
     */
    @Test
    public void concatMapDelayWithThread_tillTheEndFalse() {
        Observable.interval(300, TimeUnit.MILLISECONDS).take(4)
                .concatWith(Observable.error(new IllegalArgumentException()))
                .doOnNext(v -> System.out.println(System.currentTimeMillis() + " upstream next=" + v))
                .doOnError(e -> System.err.println(System.currentTimeMillis() + " error=" + e))
                .concatMapDelayError(v -> Observable.interval(100, TimeUnit.MILLISECONDS).take(3), false, Flowable.bufferSize())
                .subscribe(v -> System.out.println(System.currentTimeMillis() + " observer onNext=" + v),
                        System.err::println);
        SystemClock.sleep(5000);
    }

    @Test
    public void concatMapDelayWithThread_tillTheEndFalse_withInnerError() {
        Observable.interval(150, TimeUnit.MILLISECONDS).take(4)
                .doOnNext(v -> System.out.println(System.currentTimeMillis() + " upstream next=" + v))
                .concatMapDelayError(v -> Observable.interval(300, TimeUnit.MILLISECONDS).take(3)
                        .concatWith(Observable.error(new IllegalArgumentException())), false, Flowable.bufferSize())
                .subscribe(v -> System.out.println(System.currentTimeMillis() + " observer onNext=" + v),
                        System.err::println);
        SystemClock.sleep(5000);
    }

    @Test
    public void concatMapDelayWithThread_tillTheEndTrue_withInnerError() {
        Observable.interval(150, TimeUnit.MILLISECONDS).take(4)
                .doOnNext(v -> System.out.println(System.currentTimeMillis() + " upstream next=" + v))
                .concatMapDelayError(v -> Observable.interval(300, TimeUnit.MILLISECONDS).take(3)
                        .concatWith(Observable.error(new IllegalArgumentException())), true, Flowable.bufferSize())
                .subscribe(v -> System.out.println(System.currentTimeMillis() + " observer onNext=" + v),
                        System.err::println);
        SystemClock.sleep(5000);
    }

}

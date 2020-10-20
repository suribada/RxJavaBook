package com.suribada.rxjavabook.chap7;

import com.suribada.rxjavabook.SystemClock;
import com.suribada.rxjavabook.chap1.Bookmark;

import org.junit.Test;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.internal.schedulers.ComputationScheduler;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RetryTest {

    @Test
    public void normal() {
        io.reactivex.Observable<Integer> obs;
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10)
                .map(i -> 10 / (5 - i))
                .subscribe(System.out::println, System.err::println);
        SystemClock.sleep(3000);
    }

    @Test
    public void retry() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10)
                .map(i -> 10 / (5 - i))
                .retry(3)
                .subscribe(System.out::println, System.err::println);
        SystemClock.sleep(3000);
    }

    @Test
    public void retryUntil() {
        long start = System.currentTimeMillis();
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10)
                .map(i -> 10 / (5 - i)) // (1)
                .retryUntil(() -> System.currentTimeMillis() - start > 5000) // (2)
                .subscribe(System.out::println, System.err::println);
        SystemClock.sleep(10000);
    }

    @Test
    public void retryPredicate() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10)
                .map(i -> 10 / (5 - i))
                .retry(e -> e instanceof ArithmeticException)
                .subscribe(System.out::println, System.err::println);
        SystemClock.sleep(3000);
    }

    @Test
    public void retryPredicate2() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10)
                .map(i -> 10 / (5 - i))
                .retry(3, e -> e instanceof ArithmeticException)
                .subscribe(System.out::println, System.err::println);
        SystemClock.sleep(3000);
    }

    @Test
    public void retryPredicate3() {
        getBookmarks()
                .retry(3, e -> e instanceof ConnectException || e instanceof SocketTimeoutException)
                .subscribe(System.out::println, System.err::println);
    }

    private Observable<Bookmark> getBookmarks() {
        return Observable.<Bookmark>error(new SocketTimeoutException("socketTimeout"))
                .doOnError(e -> System.out.println("에러"));
    }

    @Test
    public void retryBiPredicate() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10)
                .map(i -> 10 / (5 - i))
                .retry((times, e) -> times < 3 && e instanceof ArithmeticException)
                .subscribe(System.out::println, System.err::println);
        SystemClock.sleep(3000);
    }

    @Test
    public void retryBiPredicate2() {
        getBookmarks()
                .retry((times, e) -> {
                    if (times < 3 && e instanceof ConnectException) {
                        return true;
                    }
                    if (times < 4 && e instanceof SocketTimeoutException) {
                        return true;
                    }
                    return false;
                })
                .subscribe(System.out::println, System.err::println);
        getBookmarks()
                .retry((times, e) -> (times < 3 && e instanceof ConnectException) ||
                    (times < 4 && e instanceof SocketTimeoutException))
                .subscribe(System.out::println, System.err::println);
    }

    /**
     * 이런 식은 안 됨
     */
    @Test
    public void retryWhenWithFault1() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10)
                .map(i -> 10 / (5 - i))
                .doOnSubscribe(disposable -> System.out.println("upstream subscribed"))
                .doOnDispose(() -> System.out.println("upstream disposed"))
                .doOnNext(v -> System.out.println("upstream value=" + v))
                .retryWhen(errors -> Observable.timer(1, TimeUnit.SECONDS) // (1)
                        .doOnSubscribe(disposable -> System.out.println("handler subscribed")) // (2)
                        .doOnNext(value -> System.out.println("value=" + value))) // (3)
                .subscribe(System.out::println, System.err::println);
        SystemClock.sleep(10000);
    }

    @Test
    public void retryWhenWithFault2() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10)
                .map(i -> 10 / (5 - i))
                .doOnSubscribe(disposable -> System.out.println("upstream subscribed"))
                .doOnDispose(() -> System.out.println("upstream disposed"))
                .doOnNext(v -> System.out.println("upstream value=" + v))
                .retryWhen(errors -> Observable.<Integer>create(emitter -> {
                            Thread.sleep(1000);
                            emitter.onNext(0);
                        }).subscribeOn(Schedulers.computation())
                        .doOnSubscribe(disposable -> System.out.println(System.currentTimeMillis() + "handler subscribed")) // (2)
                        .doOnNext(value -> System.out.println(System.currentTimeMillis() + "handler value=" + value))) // (3)
                .subscribe(System.out::println, System.err::println);
        SystemClock.sleep(10000);
    }

    @Test
    public void retryWhenWithFault2_ver2() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10)
                .map(i -> 10 / (5 - i))
//                .doOnSubscribe(disposable -> System.out.println("upstream subscribed"))
//                .doOnDispose(() -> System.out.println("upstream disposed"))
//                .doOnNext(value -> System.out.println("upstream value=" + value))
                .retryWhen(errors -> Observable.<Integer>create(emitter -> { // (1) 시작
                            Schedulers.computation().scheduleDirect(() -> {
                                emitter.onNext(0);
                            }, 1, TimeUnit.SECONDS);
                        }) // (1) 끝
                        .doOnSubscribe(disposable -> System.out.println("handler subscribed"))
                        .doOnNext(value -> System.out.println("handler value=" + value)))
                .subscribe(System.out::println, System.err::println);
        SystemClock.sleep(10000);
    }

    /**
     * 갯수를 5개까지만 해서 에러가 안 나는 버전
     * 그래도 retryWhen이 되는지 확인
     */
    @Test
    public void retryWhenWithFault2_ver3() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .doOnNext(next -> System.out.println("interval value=" + next))
                .take(5)
                .map(i -> 10 / (5 - i))
                .doOnSubscribe(disposable -> System.out.println("upstream subscribed"))
                .doOnDispose(() -> System.out.println("upstream disposed"))
                .doOnNext(value -> System.out.println("upstream value=" + value))
                .retryWhen(errors -> Observable.<Integer>create(emitter -> { // (1) 시작
                    Schedulers.computation().scheduleDirect(() -> {
                        emitter.onNext(0);
                    }, 1, TimeUnit.SECONDS);
                }) // (1) 끝
                        .doOnSubscribe(disposable -> System.out.println("subscribed"))
                        .doOnNext(value -> System.out.println("value=" + value)))
                .subscribe(System.out::println, System.err::println);
        SystemClock.sleep(30000);
    }

    @Test
    public void retryWhenDelayed() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10)
                .map(i -> 10 / (5 - i))
                .doOnSubscribe(disposable -> System.out.println("upstream subscribed"))
                .doOnDispose(() -> System.out.println("upstream disposed"))
                .doOnNext(value -> System.out.println("upstream value=" + value))
                .retryWhen(errors -> errors.flatMap(e -> Observable.timer(1, TimeUnit.SECONDS))) // (1)
                .subscribe(System.out::println, System.err::println);
        SystemClock.sleep(10000);
    }

    @Test
    public void retryWhenDelayed2() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10)
                .map(i -> 10 / (5 - i))
                .doOnSubscribe(disposable -> System.out.println("upstream subscribed"))
                .doOnDispose(() -> System.out.println("upstream disposed"))
                .doOnNext(value -> System.out.println("upstream value=" + value))
                .retryWhen(errors -> errors.delay(1, TimeUnit.SECONDS)) // (1)
                .subscribe(System.out::println, System.err::println);
        SystemClock.sleep(10000);
    }

    @Test
    public void retryWhenWithTake() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .doOnNext(value -> System.out.println("Thread interval=" + Thread.currentThread().getName()))
                .doOnSubscribe(disposable -> System.out.println("Thread=" + Thread.currentThread().getName()))
                .take(10)
                .map(i -> 10 / (5 - i))
                .doOnDispose(() -> System.out.println("upstream disposed")) // (1)
                .retryWhen(errors -> errors.doOnNext(error -> System.out.println("handler onNext=" + error)) // (2)
                        .take(3) // (3)
                        .doOnComplete(() -> System.out.println("handler completed")) // (4)
                        .flatMap(e -> Observable.timer(1, TimeUnit.SECONDS).doOnNext(value -> System.out.println("Thread timer=" + Thread.currentThread().getName())))
                        .doOnComplete(() -> System.out.println("handler completed2:" + Thread.currentThread().getName())) // (4)
                )
                .subscribe(System.out::println, System.err::println);
        SystemClock.sleep(10000);
    }

    /**
     * 이것도 어쩔 수 없이 2회만 재시도한다.
     */
    @Test
    public void retryWhenWithTake_withSingleThread() {
        Scheduler scheduler = Schedulers.from(Executors.newSingleThreadScheduledExecutor()); // (1)
        Observable.interval(100, TimeUnit.MILLISECONDS, scheduler) // (2)
                .doOnNext(value -> System.out.println("Thread interval=" + Thread.currentThread().getName()))
                .doOnSubscribe(disposable -> System.out.println("Thread=" + Thread.currentThread().getName()))
                .take(10)
                .map(i -> 10 / (5 - i))
                .doOnDispose(() -> System.out.println("upstream disposed"))
                .retryWhen(errors -> errors.doOnNext(error -> System.out.println("handler onNext=" + error))
                        .take(3)
                        .doOnComplete(() -> System.out.println("handler completed"))
                        .flatMap(e -> Observable.timer(1, TimeUnit.SECONDS, scheduler).doOnNext(value -> System.out.println("Thread timer=" + Thread.currentThread().getName()))) // (3)
                        .doOnComplete(() -> System.out.println("handler completed2:" + Thread.currentThread().getName()))
                )
                .subscribe(System.out::println, System.err::println);
        SystemClock.sleep(10000);
    }


    @Test
    public void retryWhenWithTake_rangeObservable() {
        Observable.range(0, 10)
                .map(i -> 10 / (5 - i))
                .doOnSubscribe(disposable -> System.out.println("Thread=" + Thread.currentThread().getName()))
                .doOnDispose(() -> System.out.println("upstream disposed")) // (1)
                .retryWhen(errors -> errors.doOnNext(error -> System.out.println("handler onNext=" + error)) // (2)
                        .take(4) // (3)
                        .doOnComplete(() -> System.out.println("handler completed")) // (4)
                        .flatMap(e -> Observable.timer(1, TimeUnit.SECONDS).doOnNext(value -> System.out.println("Thread timer=" + Thread.currentThread().getName())))
                        .doOnComplete(() -> System.out.println("handler completed2: " + Thread.currentThread().getName())) // (4)
                )
                .subscribe(System.out::println, System.err::println);
        SystemClock.sleep(10000);
    }

    @Test
    public void retryWhenWithTake2() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10)
                .map(i -> 10 / (5 - i))
                .doOnDispose(() -> System.out.println("upstream disposed")) // (1)
                .retryWhen(errors -> errors.doOnNext(error -> System.out.println("handler onNext=" + error)) // (2)
                        .take(4) // (3)
                        .doOnComplete(() -> System.out.println("handler completed")) // (4)
                        .delay(3, TimeUnit.SECONDS)
                        .doOnComplete(() -> System.out.println("handler completed2"))) // (4)
                .subscribe(System.out::println, System.err::println);
        SystemClock.sleep(20000);
    }

    @Test
    public void retryWhenWithTake3() {
        Observable.range(1, 10)
                .map(i -> 10 / (5 - i))
                .doOnDispose(() -> System.out.println("upstream disposed")) // (1)
                .retryWhen(errors -> errors.doOnNext(error -> System.out.println("handler onNext=" + error)) // (2)
                        .take(4) // (3)
                        .doOnComplete(() -> System.out.println("handler completed")) // (4)
                        .delay(3, TimeUnit.SECONDS)
                        .doOnComplete(() -> System.out.println("handler completed2")) // (4)
                ).subscribe(System.out::println, System.err::println);
        SystemClock.sleep(20000);
    }

    @Test
    public void retryWhenWithOnlyThreeRetry() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10)
                .map(i -> 10 / (5 - i))
                .retryWhen(backOffDelay(3, 1000)) // (1)
                .subscribe(System.out::println, System.err::println);
        SystemClock.sleep(10000);
    }

    private Function<Observable<Throwable>, Observable<?>> backOffDelay(int retry, int delayMillis) {
        return errors -> errors.zipWith(Observable.range(1, retry + 1), // (1)
                (e, attempt) -> attempt) // (2)
                .flatMap(attempt -> Observable.timer(attempt * delayMillis, TimeUnit.MILLISECONDS))
                .doOnComplete(() -> System.out.println("onComplete")); // (3)
    }

    @Test
    public void retryWhenWithRetryAndError() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(10)
                .map(i -> 10 / (5 - i))
                .retryWhen(backOffDelay2(3, 1000)) // (1)
                .subscribe(System.out::println, System.err::println);
        SystemClock.sleep(  20000);
    }

    private Function<? super Observable<Throwable>, ? extends Observable<?>> backOffDelay2(
            int retry, int delayMillis) {
        return errors -> errors.zipWith(Observable.range(1, retry + 1),
                (e, attempt) -> {
                    if (attempt == retry + 1) { // (1) 시작
                        return Observable.error(e);
                    } else { // (1) 끝
                        return Observable.timer(attempt * delayMillis, TimeUnit.MILLISECONDS);
                    }
                }
            ).flatMap(x -> x); // (2)
    }

    @Test
    public void retryWhenWithRetryAndError2() {
        Observable.range(1, 10)
                .take(10)
                .map(i -> 10 / (5 - i))
                .retryWhen(backOffDelay2(3, 1000)) // (1)
                .subscribe(System.out::println, System.err::println);
        SystemClock.sleep(  20000);
    }

    // https://github.com/nurkiewicz/rxjava-book-examples/blob/master/src/test/java/com/oreilly/rxjava/ch7/RetryTimeouts.java 참고
    private Function<? super Observable<Throwable>, ? extends Observable<?>> backOffDelay_power2(
            int retry, int delayMillis) {
        return errors -> errors.zipWith(Observable.range(1, retry + 1),
                (e, attempt) -> {
                    if (attempt == retry + 1) { // (1) 시작
                        return Observable.error(e);
                    } else { // (1) 끝
                        return Observable.timer((long) Math.pow(2, attempt - 1), TimeUnit.MILLISECONDS);
                    }
                }
        ).flatMap(x -> x); // (2)
    }

}

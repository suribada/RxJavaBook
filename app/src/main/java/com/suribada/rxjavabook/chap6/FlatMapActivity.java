package com.suribada.rxjavabook.chap6;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;

import com.suribada.rxjavabook.R;
import com.suribada.rxjavabook.chap5.SearchResult;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.schedulers.IoScheduler;
import io.reactivex.schedulers.Schedulers;

import static java.util.concurrent.TimeUnit.SECONDS;

public class FlatMapActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.five_buttons);
    }

    public void onClickButton1(View view) {
        getMembers().subscribeOn(Schedulers.io()) // (1)
                .flatMap(member -> getProfile(member).subscribeOn(Schedulers.io())) // (2)
                .observeOn(AndroidSchedulers.mainThread()) // (3)
                .subscribe(System.out::println);
    }

    public void onClickButton2(View view) {
        takeProfilesFlatMap();
    }

    public void takeProfilesFlatMap() {
        getMembers().subscribeOn(Schedulers.io())
                .flatMap(member -> getProfile(member).subscribeOn(Schedulers.io()), 20) // (1)
                .doOnEach(profile -> System.out.println(Thread.currentThread().getName() + ":" + profile.toString()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(System.out::println);
    }

    private void takeProfilesSchdulerWhen() {
        Scheduler maxConcurrentScheduler = getMaxConcurrentScheduler(); // (1)
        getMembers().subscribeOn(maxConcurrentScheduler)
                .doOnEach(System.out::println)
                .flatMap(member -> getProfile(member).subscribeOn(maxConcurrentScheduler)) // (2)
                .doOnEach(profile -> System.out.println(Thread.currentThread().getName() + ":" + profile.toString() + ": ioScheduler.size=" + ((IoScheduler) Schedulers.io()).size()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(System.out::println);
        getAnotherMembers().subscribeOn(maxConcurrentScheduler)
                .flatMap(member -> getProfile(member).subscribeOn(maxConcurrentScheduler)) // (3)
                .doOnEach(profile -> System.out.println(Thread.currentThread().getName() + ":" + profile.toString()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(System.out::println);
    }

    private void showResults() {
        Scheduler maxConcurrentScheduler = getMaxConcurrentScheduler(); // (2)
        getMembers().subscribeOn(maxConcurrentScheduler) // (3)
                .doOnEach(System.out::println)
                .flatMap(member -> getProfile(member).subscribeOn(maxConcurrentScheduler)) // (4)
                .doOnEach(profile -> System.out.println(Thread.currentThread().getName() + ":"
                        + profile.toString() + ": ioScheduler.size=" + ((IoScheduler) Schedulers.io()).size()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(System.out::println);
        getSearchResult().subscribeOn(maxConcurrentScheduler) // (5)
                .flatMap(searchResult ->
                        getSearchDetail(searchResult).subscribeOn(maxConcurrentScheduler)) // (6)
                .doOnEach(profile -> System.out.println(Thread.currentThread().getName() + ":" + profile.toString()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(System.out::println);
    }

    private Observable<Member> getMembers() {
        return Observable.range(1, 10000) // onClickButton1() 에서 OutOfMemoryError가 나게 하려면 count=10000 정도로 크게 하면 된다.
                .map(value -> new Member("David " + value));

    }

    private Observable<Member> getAnotherMembers() {
        return Observable.range(201, 200)
                .map(value -> new Member("David " + value));
    }

    private Observable<SearchResult> getSearchResult() {
        return Observable.range(201, 200)
                .map(value -> new SearchResult("David " + value));
    }

    private Observable<Profile> getProfile(Member member) {
        return Observable.create(emitter -> {
            SystemClock.sleep(2000);
            emitter.onNext(new Profile(member));
            emitter.onComplete(); // 빠뜨리면 2번째 것이 실행되지 않음
        });
    }

    private Observable<SearchDetail> getSearchDetail(SearchResult searchResult) {
        return Observable.create(emitter -> {
            SystemClock.sleep(2000);
            emitter.onNext(new SearchDetail(searchResult));
            emitter.onComplete(); // 빠뜨리면 2번째 것이 실행되지 않음
        });
    }

    /**
     * RxJava 테스트코드에서 가져온 코드
     */
    public void onClickButton3(View view) {
        final int m = 4;
        Observable<Integer> source = Observable.range(1, 100)
                .flatMap(t -> Observable.range(t * 10, 2)
                        .subscribeOn(Schedulers.computation()), m);
        source.subscribe(System.out::println);
    }

    public void onClickButton4(View view) {
        takeProfilesSchdulerWhen();
    }

    private Scheduler getMaxConcurrentScheduler() {
        return Schedulers.io().when(workerActions -> {
            Flowable<Completable> workers = workerActions.map(actions -> Completable.concat(actions));
            return Completable.merge(workers, 20); // (3)
            //return Completable.merge(Flowable.merge(workerActions), 20);
        });
    }

    public void onClickButton5(View view) {
        showResultsDelayed();
    }

    private Scheduler getDelayScheduler() {
        return Schedulers.io().when(workerActions -> {
            Flowable<Completable> workers = workerActions.map(actions -> Completable.concat(actions));
            return Completable.concat(workers.map(worker -> worker.delay(1, SECONDS))); // (1)
        });
    }

    private void showResultsDelayed() {
        Scheduler delayScheduler = getDelayScheduler();
        getMembers().subscribeOn(delayScheduler)
                .doOnEach(System.out::println)
                .flatMap(member -> getProfile(member).subscribeOn(delayScheduler)
                        .doOnSubscribe(ignored -> System.out.println(Thread.currentThread().getName() + ": subscribe time=" + System.currentTimeMillis()))
                        .doOnComplete(() -> System.out.println(Thread.currentThread().getName() + ": complete time=" + System.currentTimeMillis())))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(System.out::println);
    }

}

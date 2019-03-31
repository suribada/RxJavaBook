package com.suribada.rxjavabook.chap5;

import android.support.v4.util.Pair;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava2.math.MathObservable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Noh.Jaechun on 2018. 9. 11..
 */
public class ConnectableObservableTest {

    @Test
    public void testConnect() {
        ConnectableObservable<Integer> obs = Observable.range(1, 10)
            .map(this::getScore)
            .doOnNext(value -> System.out.println("next=" + value))
            .publish(); // (1)
        obs.to(MathObservable::sumInt)
            .doOnSubscribe(disposable -> System.out.println("observer1 subscribed"))
            .subscribe(sum -> System.out.println("sum=" + sum)); // (2)
        obs.to(MathObservable::averageDouble)
            .doOnSubscribe(disposable -> System.out.println("observer2 subscribed2"))
            .subscribe(average -> System.out.println("average=" + average)); // (3)
        obs.connect(); // (4)
        obs.to(MathObservable::min)
            .doOnSubscribe(disposable -> System.out.println("observer3 subscribed"))
            .subscribe(min -> System.out.println("min=" + min)); // (5)
        obs.to(MathObservable::max)
            .doOnSubscribe(disposable -> System.out.println("observer4 subscribed"))
            .subscribe(min -> System.out.println("max=" + min)); // (6)
    }

    private Random random = new Random();

    private int getScore(long value) {
        return random.nextInt(101);
    }

    @Test
    public void testConnect_thread() {
        ConnectableObservable<Integer> obs = Observable.interval(1, TimeUnit.SECONDS)
            .take(10)
            .map(this::getScore)
            .doOnNext(value -> System.out.println("next=" + value))
            .publish();
        obs.to(MathObservable::sumInt)
            .doOnSubscribe(disposable -> System.out.println("observer1 subscribed"))
            .subscribe(sum -> System.out.println("sum=" + sum));
        obs.to(MathObservable::averageDouble)
            .doOnSubscribe(disposable -> System.out.println("observer2 subscribed"))
            .subscribe(average -> System.out.println("average=" + average));
        obs.connect(); // (1)
        SystemClock.sleep(3000); // (2)
        obs.to(MathObservable::sumInt)
            .doOnSubscribe(disposable -> System.out.println("observer3 subscribed"))
            .subscribe(min -> System.out.println("sum2=" + min));
        obs.to(MathObservable::averageDouble)
            .doOnSubscribe(disposable -> System.out.println("observer4 subscribed"))
            .subscribe(min -> System.out.println("average2=" + min));
        SystemClock.sleep(10000);
    }

    @Test
    public void testConnect_thread_and_dispose() {
        ConnectableObservable<Integer> obs = Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> System.out.println("obs disposed"))
                .map(this::getScore)
                .doOnDispose(() -> System.out.println("obs mapped disposed")) // (1)
                .doOnNext(value -> System.out.println("next=" + value))
                .publish();
        obs.doOnSubscribe(disposable -> System.out.println("observer1 subscribed"))
                .doOnDispose(() -> System.out.println("observer1 disposed")) // (2)
                .map(score -> "score: " + score)
                .subscribe(value -> System.out.println("observer1 " + value));
        obs.doOnSubscribe(disposable -> System.out.println("observer2 subscribed"))
                .doOnDispose(() -> System.out.println("observer2 disposed")) // (3)
                .map(score -> "score: " + (10 + score))
                .subscribe(value -> System.out.println("observer2 " + value));
        Disposable disposable = obs.connect(); // (4)
        SystemClock.sleep(3000); // (5)
        disposable.dispose(); // (6)
        obs.subscribe(score -> System.out.println("observer3 score: " + score)); // (7)
        SystemClock.sleep(10000);
    }

    @Test
    public void testConnect_thread_and_dispose_separate() {
        ConnectableObservable<Integer> obs = Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> System.out.println("obs disposed"))
                .map(this::getScore)
                .doOnDispose(() -> System.out.println("obs mapped disposed"))
                .doOnNext(value -> System.out.println("next=" + value))
                .publish();
        Disposable disposable1
            = obs.doOnSubscribe(disposable -> System.out.println("observer1 subscribed")) // (1)
                .doOnDispose(() -> System.out.println("observer1 disposed")) // (2)
                .map(score -> "score: " + score)
                .subscribe(value -> System.out.println("observer1 " + value));
        Disposable disposable2
            = obs.doOnSubscribe(disposable -> System.out.println("observer2 subscribed")) // (3)
                .doOnDispose(() -> System.out.println("observer2 disposed")) // (4)
                .map(score -> "score: " + (10 + score))
                .subscribe(value -> System.out.println("observer2 " + value));
        obs.connect();
        SystemClock.sleep(3000);
        disposable1.dispose(); // (5)
        SystemClock.sleep(2000);
        disposable2.dispose(); // (6)
        obs.subscribe(score -> System.out.println("observer3 score: " + score)); // (7)
        SystemClock.sleep(10000);

    }

    @Test
    public void testConnect_thread_withDisposeFunction() {
        ConnectableObservable<Integer> obs = Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> System.out.println("obs disposed"))
                .map(this::getScore)
                .doOnNext(value -> System.out.println("next=" + value))
                .publish();
        obs.doOnSubscribe(disposable -> System.out.println("observer1 subscribed"))
                .doOnDispose(() -> System.out.println("observer1 disposed"))
                .map(score -> "score: " + score)
                .subscribe(value -> System.out.println("observer1 " + value));
        obs.doOnSubscribe(disposable -> System.out.println("observer2 subscribed"))
                .doOnDispose(() -> System.out.println("observer2 disposed"))
                .map(score -> 10 + score)
                .subscribe(addedScore -> System.out.println("observer2 score=" + addedScore));
        obs.connect(disposable -> {
            SystemClock.sleep(3000);
            disposable.dispose(); // (1)
        });
        SystemClock.sleep(10000); // (2)
    }

    @Test
    public void testConnect_thread_withSeparateDisposable_withLambda() {
        ConnectableObservable<Integer> obs = Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> System.out.println("obs disposed"))
                .map(this::getScore)
                .doOnNext(value -> System.out.println("next=" + value))
                .publish();
        obs.doOnSubscribe(disposable -> System.out.println("observer1 subscribed"))
                .map(score -> "score: " + score)
                .subscribe(value -> System.out.println("observer1 " + value));
        obs.doOnSubscribe(disposable -> System.out.println("observer2 subscribed"))
                .map(score -> 10 + score)
                .subscribe(addedScore -> System.out.println("observer2 score=" + addedScore));
        obs.connect(disposable -> {
            Calendar calendar = Calendar.getInstance();
            if (calendar.get(Calendar.HOUR_OF_DAY) < 12) { // (1) 시작
                disposable.dispose();
            }
        });
        SystemClock.sleep(3000);
    }

    @Test
    public void testConnect_thread_withSeparateDisposable() {
        ConnectableObservable<Integer> obs = Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> System.out.println("obs disposed"))
                .map(this::getScore)
                .doOnNext(value -> System.out.println("next=" + value))
                .publish();
        obs.doOnSubscribe(disposable -> System.out.println("observer1 subscribed"))
                .map(score -> "score: " + score)
                .subscribe(value -> System.out.println("observer1 " + value));
        obs.doOnSubscribe(disposable -> System.out.println("observer2 subscribed"))
                .map(score -> 10 + score)
                .subscribe(addedScore -> System.out.println("observer2 score=" + addedScore));
        ConnectConsumer connectConsumer = new ConnectConsumer();
        obs.connect(connectConsumer); // (1)
        SystemClock.sleep(3000);
        connectConsumer.disposable.dispose(); // (2)
        SystemClock.sleep(10000);
    }

    private class ConnectConsumer implements Consumer<Disposable> {
        public Disposable disposable;

        @Override
        public void accept(Disposable disposable) throws Exception {
            this.disposable = disposable;
            Calendar calendar = Calendar.getInstance();
            if (calendar.get(Calendar.HOUR_OF_DAY) < 12) { // (1) 시작
                disposable.dispose();
            } // (1) 끝
        }
    }

    @Test
    public void testAutoConnect() {
        Observable<Integer> obs = Observable.range(1, 10)
                .map(this::getScore)
                .doOnNext(value -> System.out.println("next=" + value))
                .publish()
                .autoConnect(2); // (1)
        obs.to(MathObservable::sumInt)
                .doOnSubscribe(disposable -> System.out.println("observer1 subscribed"))
                .subscribe(sum -> System.out.println("sum=" + sum));
        obs.to(MathObservable::averageDouble)
                .doOnSubscribe(disposable -> System.out.println("observer2 subscribed2"))
                .subscribe(average -> System.out.println("average=" + average));
        obs.to(MathObservable::min)
                .doOnSubscribe(disposable -> System.out.println("observer3 subscribed"))
                .subscribe(min -> System.out.println("min=" + min));
        obs.to(MathObservable::max)
                .doOnSubscribe(disposable -> System.out.println("observer4 subscribed"))
                .subscribe(min -> System.out.println("max=" + min));
    }

    @Test
    public void testAutoConnect_thread() {
        Observable<Integer> obs = Observable.interval(1, TimeUnit.SECONDS)
                .take(10)
                .map(this::getScore)
                .doOnNext(value -> System.out.println("next=" + value))
                .publish()
                .autoConnect(2);
        obs.to(MathObservable::sumInt)
                .doOnSubscribe(disposable -> System.out.println("observer1 subscribed"))
                .subscribe(sum -> System.out.println("sum=" + sum));
        obs.to(MathObservable::averageDouble)
                .doOnSubscribe(disposable -> System.out.println("observer2 subscribed"))
                .subscribe(average -> System.out.println("average=" + average));
        SystemClock.sleep(3000);
        obs.to(MathObservable::sumInt)
                .doOnSubscribe(disposable -> System.out.println("observer3 subscribed"))
                .subscribe(min -> System.out.println("sum2=" + min));
        obs.to(MathObservable::averageDouble)
                .doOnSubscribe(disposable -> System.out.println("observer4 subscribed"))
                .subscribe(min -> System.out.println("average2=" + min));
        SystemClock.sleep(10000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRefCount_withZeroSubscriberCount() {
        Observable<Integer> obs = Observable.range(1, 10)
                .doOnNext(value -> System.out.println("next=" + value))
                .publish().refCount(0); // (1)
        obs.doOnSubscribe(disposable -> System.out.println("observer1 subscribed"))
                .subscribe(value -> System.out.println("value=" + value));
        obs.doOnSubscribe(disposable -> System.out.println("observer2 subscribed2"))
                .subscribe(value -> System.out.println("value=" + value));
        obs.doOnSubscribe(disposable -> System.out.println("observer3 subscribed"))
                .subscribe(value -> System.out.println("value=" + value));
    }

    @Test
    public void testRefCount() {
        Observable<Integer> obs = Observable.range(1, 3)
                .doOnNext(value -> System.out.println("next=" + value))
                .doOnDispose(() -> System.out.println("source disposed"))
                .publish().refCount(2); // (1)
        obs.doOnSubscribe(disposable -> System.out.println("observer1 subscribed"))
                .doOnDispose(() -> System.out.println("observer1 disposed"))
                .subscribe(value -> System.out.println("value1=" + value)); // (2)
        obs.doOnSubscribe(disposable -> System.out.println("observer2 subscribed"))
                .doOnDispose(() -> System.out.println("observer2 disposed"))
                .subscribe(value -> System.out.println("value2=" + value)); // (3)
        obs.doOnSubscribe(disposable -> System.out.println("observer3 subscribed"))
                .subscribe(value -> System.out.println("value3=" + value)); // (4)
        obs.doOnSubscribe(disposable -> System.out.println("observer4 subscribed"))
                .subscribe(value -> System.out.println("value4=" + value)); // (5)
        obs.doOnSubscribe(disposable -> System.out.println("observer5 subscribed"))
                .subscribe(value -> System.out.println("value5=" + value)); // (6)
    }

    @Test
    public void testAutoConnect_forDiff() {
        Observable<Integer> obs = Observable.range(1, 3)
                .doOnNext(value -> System.out.println("next=" + value))
                .doOnDispose(() -> System.out.println("source disposed"))
                .publish().autoConnect(2); // (1)
        obs.doOnSubscribe(disposable -> System.out.println("observer1 subscribed"))
                .doOnDispose(() -> System.out.println("observer1 disposed"))
                .subscribe(value -> System.out.println("value1=" + value)); // (2)
        obs.doOnSubscribe(disposable -> System.out.println("observer2 subscribed"))
                .doOnDispose(() -> System.out.println("observer2 disposed"))
                .subscribe(value -> System.out.println("value2=" + value)); // (3)
        obs.doOnSubscribe(disposable -> System.out.println("observer3 subscribed"))
                .subscribe(value -> System.out.println("value3=" + value)); // (4)
        obs.doOnSubscribe(disposable -> System.out.println("observer4 subscribed"))
                .subscribe(value -> System.out.println("value4=" + value)); // (5)
        obs.doOnSubscribe(disposable -> System.out.println("observer5 subscribed"))
                .subscribe(value -> System.out.println("value5=" + value)); // (6)
    }

    @Test
    public void testRefCount_interval() {
        Observable<Long> obs = Observable.interval(100, TimeUnit.MILLISECONDS)
                .doOnNext(value -> System.out.println("next=" + value))
                .doOnDispose(() -> System.out.println("source disposed"))
                .publish().refCount(2); // (1)
        obs.take(3)
                .doOnSubscribe(disposable -> System.out.println("observer1 subscribed"))
                .doOnDispose(() -> System.out.println("observer1 disposed"))
                .subscribe(value -> System.out.println("value1=" + value));
        obs.take(5)
                .doOnSubscribe(disposable -> System.out.println("observer2 subscribed"))
                .doOnDispose(() -> System.out.println("observer2 disposed"))
                .subscribe(value -> System.out.println("value2=" + value));
        //SystemClock.sleep(1000);
        obs.take(2)
                .doOnSubscribe(disposable -> System.out.println("observer3 subscribed"))
                .subscribe(value -> System.out.println("value3=" + value));
        obs.take(4)
                .doOnSubscribe(disposable -> System.out.println("observer4 subscribed"))
                .subscribe(value -> System.out.println("value4=" + value));
        //SystemClock.sleep(1000);
        obs.take(7)
                .doOnSubscribe(disposable -> System.out.println("observer5 subscribed"))
                .subscribe(value -> System.out.println("value5=" + value));
        SystemClock.sleep(1000);
    }

    @Test
    public void testPublishWithSelector() {
        Observable<Long> observable = Observable.interval(100, TimeUnit.MILLISECONDS)
                .doOnSubscribe(disposable -> System.out.println("interval Observable subscribed"))
                .publish(obs -> obs.take(10));
        observable.subscribe(value -> System.out.println("observer1=" + value));
        SystemClock.sleep(500);
        observable.subscribe(value -> System.out.println("observer2=" + value));
        SystemClock.sleep(1500);
    }

    @Test
    public void testPublishWithSelectorAnotherType() {
        Observable<String> observable = Observable.interval(100, TimeUnit.MILLISECONDS)
                .publish(obs -> obs.map(value -> "kk" + value));
        observable.subscribe(value -> System.out.println("observer1=" + value));
        SystemClock.sleep(500);
        observable.subscribe(value -> System.out.println("observer2=" + value));
        SystemClock.sleep(1500);
    }

    @Test
    public void testReplay() {
        Observable<Long> obs = Observable.interval(1, TimeUnit.SECONDS)
                .take(5)
                .doOnNext(value -> System.out.println("next=" + value))
                .replay().autoConnect(2); // (1)
        obs.doOnSubscribe(disposable -> System.out.println("observer1 subscribed"))
                .subscribe(value -> System.out.println("value1=" + value)); // (2)
        obs.doOnSubscribe(disposable -> System.out.println("observer2 subscribed"))
                .subscribe(value -> System.out.println("value2=" + value)); // (3)
        SystemClock.sleep(3000);
        obs.doOnSubscribe(disposable -> System.out.println("observer3 subscribed"))
                .subscribe(value -> System.out.println("value3=" + value)); // (4)
        SystemClock.sleep(1000);
    }

    @Test
    public void eachCafeLeaders_inefficeint() {
        getCafeListObservable()
                .subscribeOn(Schedulers.io())
                .flatMapIterable(v -> v)
                .map(cafe -> cafe.leader).toList()
                .subscribe(System.out::println);
        getCafeListObservable()
                .subscribeOn(Schedulers.io())
                .flatMapIterable(v -> v)
                .map(cafe -> cafe.coleader).toList()
                .subscribe(System.out::println);
    }

    @Test
    public void eachCafeLeaders_connectable() {
        ConnectableObservable<Cafe> obs = getCafeListObservable()
                .subscribeOn(Schedulers.io())
                .flatMapIterable(v -> v)
                .publish();
        obs.map(cafe -> cafe.leader).toList()
                .subscribe(System.out::println);
        obs.map(cafe -> cafe.coleader).toList()
                .subscribe(System.out::println);
        Disposable disposable = obs.connect();
    }

    @Test
    public void eachCafeLeaders() {
        Observable<Cafe> obs = getCafeListObservable()
                .subscribeOn(Schedulers.io())
                .flatMapIterable(v -> v)
                .publish()
                .autoConnect(2);
        obs.map(cafe -> cafe.leader).toList()
                .subscribe(System.out::println);
        obs.map(cafe -> cafe.coleader).toList()
                .subscribe(System.out::println);
    }

    @Test
    public void  nestedSubscribe() {
        getCafeListObservable()
                .subscribeOn(Schedulers.io())
                .flatMapIterable(v -> v)
                .subscribe(cafe -> { // (1) 시작
                   if (cafe.type == 1) {
                       findArea(cafe).subscribeOn(Schedulers.io())
                               .subscribe(area -> { // (2) 시작
                                  System.out.println("cafe=" + cafe + ", area=" + area);
                               }); // (2) 끝
                   } else if (cafe.type == 2) {
                       findHobby(cafe).subscribeOn(Schedulers.io())
                               .subscribe(hobby -> { // (3) 시작
                                   System.out.println("cafe=" + cafe + ", hobby=" + hobby);
                               }); // (3) 끝
                   }
                }); // (1) 끝
        SystemClock.sleep(1000);
    }

    @Test
    public void  nestedSubscribe_refactor_connectable() {
        ConnectableObservable<Cafe> obs = getCafeListObservable()
                .subscribeOn(Schedulers.io())
                .flatMapIterable(v -> v)
                .publish();
        obs.filter(cafe -> cafe.type == 1) // (1)
                .flatMap(cafe -> findArea(cafe).subscribeOn(Schedulers.io())
                        .map(area -> Pair.create(cafe, area))) // (2)
                .subscribe(pair -> {
                    System.out.println("cafe=" + pair.first + ", area=" + pair.second);
                });
        obs.filter(cafe -> cafe.type == 2) // (3)
                .flatMap(cafe -> findHobby(cafe).subscribeOn(Schedulers.io())
                        .map(area -> Pair.create(cafe, area))) // (4)
                .subscribe(pair -> {
                    System.out.println("cafe=" + pair.first + ", hobby=" + pair.second);
                });
        Disposable disposable = obs.connect();
        SystemClock.sleep(1000);
    }

    /**
     * 구독 해제 문제 있음
     *
     */
    @Test
    public void  nestedSubscribe_refactor2() {
        Observable<Cafe> obs = getCafeListObservable()
                .subscribeOn(Schedulers.io())
                .flatMapIterable(v -> v)
                .publish()
                .autoConnect(2);
        obs.filter(cafe -> cafe.type == 1) // (1)
                .flatMap(cafe -> findArea(cafe).subscribeOn(Schedulers.io())
                        .map(area -> Pair.create(cafe, area))) // (2)
                .subscribe(pair -> {
                    System.out.println("cafe=" + pair.first + ", area=" + pair.second);
                });
        obs.filter(cafe -> cafe.type == 2) // (3)
                .flatMap(cafe -> findHobby(cafe).subscribeOn(Schedulers.io())
                        .map(area -> Pair.create(cafe, area))) // (4)
                .subscribe(pair -> {
                    System.out.println("cafe=" + pair.first + ", hobby=" + pair.second);
                });
        SystemClock.sleep(1000);
    }

    public Observable<List<Cafe>> getCafeListObservable() {
        List<Cafe> cafes = new ArrayList<>();
        cafes.add(new Cafe(1, "분당사랑", "suribada", "horseridingking"));
        cafes.add(new Cafe(2,"바둑사랑", "ias", "iasadbc"));
        cafes.add(new Cafe(2,"승마사랑", "eof", "endofhope"));
        return Observable.just(cafes);
    }

    public Observable<String> findArea(Cafe cafe) {
        return Observable.just("분당");
    }

    public Observable<String> findHobby(Cafe cafe) {
        return Observable.just("승마");
    }

    class Cafe {
        int type; // 카페 타입
        String name; // 카페명
        String leader; // 운영자
        String coleader; // 부운영자

        Cafe(int type, String name, String leader, String coleader) {
            this.type = type;
            this.name = name;
            this.leader = leader;
            this.coleader = coleader;
        }
    }
}

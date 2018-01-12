package com.suribada.rxjavabook;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;

/**
 * Created by Noh.Jaechun on 2017. 3. 9..
 */
public class RxTest {

	@Test
	public void testCollectionMinus() {
		List<Integer> first = Arrays.asList(1, 2, 3);
		List<Integer> second = Arrays.asList(1, 4, 5);

		Observable<Integer> firstObservable = Observable.fromIterable(first);

		firstObservable.filter(firstItem -> !second.contains(firstItem)).forEach(System.out::println);
	}

	@Test
	public void testTake() {
		Observable.range(1, 5).takeUntil(x -> x == 3).forEach(System.out::println);
		Observable.range(1, 5).takeWhile(x -> x == 3).forEach(System.out::println);
	}

	@Test
	public void testObserver() {
	List<Integer> list = Arrays.asList(1, 2, 3, 4);
		Observer<Integer> observer = new Observer<Integer>() {

			@Override
			public void onSubscribe(Disposable d) {
				System.out.println("onSubscribe = " + d.getClass().getName());
			}

			@Override
			public void onNext(Integer value) {
				System.out.println("value=" + value);
			}

			@Override
			public void onError(Throwable e) {
				System.out.println("onErrror=" + e.toString());
			}

			@Override
			public void onComplete() {
				System.out.println("onComplete");
			}

		};
		//Observable.fromIterable(list).subscribe(observer);
		Observable.fromIterable(list)
				.filter(x -> x > 2)
				.map(x -> x + 5)
				.subscribe(observer);
	observer.onNext(77);
	}

	@Test
	public void testWindow() {
		Observable
				.merge(
						Observable.range(0, 5)
								.window(3,1))
				.subscribe(System.out::println);
	}

	@Test
	public void testJoin() {
		Observable<String> left =
				Observable.interval(100, TimeUnit.MILLISECONDS)
						.map(i -> "L" + i);
		Observable<String> right =
				Observable.interval(200, TimeUnit.MILLISECONDS)
						.map(i -> "R" + i);

		left
				.join(
						right,
						i -> Observable.never(),
						i -> Observable.timer(0, TimeUnit.MILLISECONDS),
						(l,r) -> l + " - " + r
				)
				.take(10)
				.timeInterval()
				.blockingSubscribe(timed -> {
					System.out.println(timed.time(TimeUnit.MILLISECONDS) + ":" + timed.value());
				});
	}

	@Test
	public void testFuse() {
		Observable.just(1, 2, 3, 4, 5, 6, 7)
				.filter(x -> x > 1)
				.filter(x -> x < 7)
				.observeOn(Schedulers.io())
				.subscribe(System.out::println);
	}

}

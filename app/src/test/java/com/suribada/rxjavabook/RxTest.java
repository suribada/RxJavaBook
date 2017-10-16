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
		Observable.fromIterable(list).subscribe(observer);
		Observable.fromIterable(list).filter(x -> x > 2).map(x -> x + 5).subscribe(observer);
		observer.onNext(77);
	}

	@Test
	public void testFuse() {
		Observable.just(1, 2, 3, 4, 5, 6, 7)
				.filter(x -> x > 1)
				.filter(x -> x < 7)
				.observeOn(Schedulers.io())
				.subscribe(System.out::println);
	}

	@Test
	public void schedulerTest() {
		//Declare TestScheduler
		TestScheduler testScheduler = new TestScheduler();

		//Declare TestObserver
		TestObserver<Long> testObserver = new TestObserver<>();

		//Declare Observable emitting every 1 minute
		Observable<Long> minuteTicker =
				Observable.interval(1, TimeUnit.MINUTES,
						testScheduler);

		//Subscribe to TestObserver
		minuteTicker.subscribe(testObserver);

		//Fast forward by 30 seconds
		testScheduler.advanceTimeBy(30, TimeUnit.SECONDS);

		//Assert no emissions have occurred yet
		testObserver.assertValueCount(1);

		//Fast forward to 70 seconds after subscription
		testScheduler.advanceTimeTo(70, TimeUnit.SECONDS);

		//Assert the first emission has occurred
		testObserver.assertValueCount(1);

		//Fast Forward to 90 minutes after subscription
		testScheduler.advanceTimeTo(90, TimeUnit.MINUTES);

		//Assert 90 emissions have occurred
		testObserver.assertValueCount(90);
	}


}

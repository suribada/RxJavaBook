package com.suribada.rxjavabook.chap11;

import com.suribada.rxjavabook.api.model.Weather;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.TestScheduler;

import static org.mockito.Mockito.when;

public class WeatherUseCaseTest {

    @Rule // (1) 시작
    public MockitoRule mockito = MockitoJUnit.rule(); // (1) 끝

    @Mock // (2) 시작
    WeatherRepository weatherRepository; // (2) 끝

    @InjectMocks // (3) 시작
    WeatherUseCase weatherUseCase; // (3) 끝

    private TestScheduler testScheduler = new TestScheduler();

    @Before
    public void before() {
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> testScheduler); // (1) 시작
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> testScheduler); // (1) 끝
    }

    @After
    public void after() {
        RxJavaPlugins.reset(); // (2)
    }

    @Test
    public void getWeatherKmaPeriodically_sameResponse() {
        Weather response = Weather.create(10, "흐리고 비", 11.0f); // (1) 시작
        Weather response2 = Weather.create(10, "흐리고 비", 11.0f); // (1) 끝

        when(weatherRepository.getWeatherKma()).thenReturn(Observable.just(response)); // (2) 시작
        TestObserver testObserver = weatherUseCase.getWeatherKmaPeriodically().test();
        testScheduler.advanceTimeBy(0, TimeUnit.MINUTES);
        testObserver.assertValue(response); // (2) 끝

        when(weatherRepository.getWeatherKma()).thenReturn(Observable.just(response2)); // (3) 시작
        testScheduler.advanceTimeBy(1, TimeUnit.MINUTES);
        testObserver.assertValueCount(1); // (3) 끝
    }

    @Test
    public void getWeatherKmaPeriodically_differentResponse() {
        Weather response = Weather.create(10, "흐리고 비", 11.0f); // (1) 시작
        Weather response2 = Weather.create(12, "흐림", 11.0f);
        Weather response3 = Weather.create(13, "흐림", 11.0f); // (1) 끝

        when(weatherRepository.getWeatherKma()).thenReturn(Observable.just(response)); // (2) 시작
        TestObserver testObserver = weatherUseCase.getWeatherKmaPeriodically().test();
        testScheduler.advanceTimeBy(0, TimeUnit.SECONDS);
        testObserver.assertValue(response); // (2) 끝

        when(weatherRepository.getWeatherKma()).thenReturn(Observable.just(response2)); // (3) 시작
        testScheduler.advanceTimeBy(1, TimeUnit.MINUTES);
        testObserver.assertValues(response, response2); // (3) 끝

        when(weatherRepository.getWeatherKma()).thenReturn(Observable.just(response3)); // (4) 시작
        testScheduler.advanceTimeBy(1, TimeUnit.MINUTES);
        testObserver.assertValues(response, response2, response3); // (4) 끝
    }

    @Test
    public void getWeatherKmaPeriodically_sameResponse_separateTestScheduler() {
        Weather response = Weather.create(10, "흐리고 비", 11.0f); // (1) 시작
        Weather response2 = Weather.create(10, "흐리고 비", 11.0f); // (1) 끝

        when(weatherRepository.getWeatherKma()).thenReturn(Observable.just(response)); // (2) 시작
        TestScheduler testScheduler = new TestScheduler();
        TestObserver testObserver = weatherUseCase.getWeatherKmaPeriodically(testScheduler).test();
        testScheduler.advanceTimeBy(0, TimeUnit.MINUTES);
        testObserver.assertValue(response); // (2) 끝

        when(weatherRepository.getWeatherKma()).thenReturn(Observable.just(response2)); // (3) 시작
        testScheduler.advanceTimeBy(1, TimeUnit.MINUTES);
        testObserver.assertValueCount(1); // (3) 끝
    }

    @Test
    public void getFastWeatherPeriodically() {
        Weather responseKma = Weather.create(10, "흐리고 비", 11.0f); // (1) 시작
        Weather responseKweather = Weather.create(12, "흐림", 11.0f);

        when(weatherRepository.getWeatherKma())
                .thenReturn(Observable.just(responseKma).delay(20, TimeUnit.SECONDS)); // (2)
        when(weatherRepository.getWeatherKweather())
                .thenReturn(Observable.just(responseKweather).delay(15, TimeUnit.SECONDS)); // (2)

        TestObserver testObserver = weatherUseCase.getFastWeatherPeriodically().test(); // (3) 시작
        testScheduler.advanceTimeBy(10, TimeUnit.SECONDS);
        testObserver.assertNoValues(); // (3) 끝

        testScheduler.advanceTimeBy(20, TimeUnit.SECONDS); // (4) 시작
        testObserver.assertValue(responseKweather); // (4) 끝

        testScheduler.advanceTimeTo(1, TimeUnit.MINUTES); // (5) 시작
        testObserver.assertValue(responseKweather); // (5)끝

       when(weatherRepository.getWeatherKweather())
                .thenReturn(Observable.just(responseKma).delay(25, TimeUnit.SECONDS)); // (2)
        testScheduler.advanceTimeBy(1, TimeUnit.MINUTES); // (5)
        testObserver.assertValue(responseKweather)
                .assertNotComplete()
                .assertNoErrors();
    }
}

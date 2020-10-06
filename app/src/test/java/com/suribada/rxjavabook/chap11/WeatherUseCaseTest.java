package com.suribada.rxjavabook.chap11;

import com.suribada.rxjavabook.api.model.Weather;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.TestScheduler;

import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

        when(weatherRepository.getWeatherKma()) // (2) 시작
                .thenReturn(Observable.just(response))
                .thenReturn(Observable.just(response2)); // (2) 끝

        TestObserver testObserver = weatherUseCase.getWeatherKmaPeriodically().test(); // (3) 시작
        testScheduler.advanceTimeBy(0, TimeUnit.MINUTES);
        testObserver.assertValue(response); // (3) 끝

        testScheduler.advanceTimeBy(1, TimeUnit.MINUTES); // (4) 시작
        testObserver.assertValueCount(1); // (4) 끝
    }

    @Test
    public void getWeatherKmaPeriodically_differentResponse() {
        Weather response = Weather.create(10, "흐리고 비", 11.0f); // (1) 시작
        Weather response2 = Weather.create(12, "흐림", 11.0f);
        Weather response3 = Weather.create(13, "흐림", 11.0f); // (1) 끝

        when(weatherRepository.getWeatherKma()) // (2) 시작
                .thenReturn(Observable.just(response))
                .thenReturn(Observable.just(response2))
                .thenReturn(Observable.just(response3)); // (2) 끝

        TestObserver testObserver = weatherUseCase.getWeatherKmaPeriodically().test(); // (3) 시작
        testScheduler.advanceTimeBy(0, TimeUnit.SECONDS);
        testObserver.assertValue(response); // (3) 끝

        testScheduler.advanceTimeBy(1, TimeUnit.MINUTES); // (4) 시작
        testObserver.assertValues(response, response2); // (4) 끝

        testScheduler.advanceTimeBy(1, TimeUnit.MINUTES); // (5) 시작
        testObserver.assertValues(response, response2, response3); // (5) 끝
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
        Weather responseKma2 = Weather.create(10, "흐리고 비", 12.0f); // (1) 시작
        Weather responseKma3 = Weather.create(10, "흐리고 비", 13.0f); // (1) 시작
        Weather responseKweather = Weather.create(12, "흐림", 11.0f); // (1) 끝
        Weather responseKweather2 = Weather.create(12, "흐림", 12.0f); // (1) 끝
        Weather responseKweather3 = Weather.create(12, "흐림", 13.0f); // (1) 끝

        when(weatherRepository.getWeatherKma()) // (2) 시작
                .thenReturn(Observable.just(responseKma).delay(20, TimeUnit.SECONDS))
                .thenReturn(Observable.just(responseKma2).delay(10, TimeUnit.SECONDS))
                .thenReturn(Observable.just(responseKma3).delay(10, TimeUnit.SECONDS));
        when(weatherRepository.getWeatherKweather())
                .thenReturn(Observable.just(responseKweather).delay(15, TimeUnit.SECONDS))
                .thenReturn(Observable.just(responseKweather2).delay(12, TimeUnit.SECONDS))
                .thenReturn(Observable.just(responseKweather3).delay(12, TimeUnit.SECONDS)); // (2) 끝

        TestObserver testObserver = weatherUseCase.getFastWeatherPeriodically().test(); // (3) 시작
        testScheduler.advanceTimeBy(10, TimeUnit.SECONDS);
        testObserver.assertNoValues(); // (3) 끝

        testScheduler.advanceTimeBy(20, TimeUnit.SECONDS); // (4) 시작
        testObserver.assertValue(responseKweather); // (4) 끝

        testScheduler.advanceTimeBy(1, TimeUnit.MINUTES); // (5) 시작
        testObserver.assertValues(responseKweather, responseKweather2); // (5)끝

        testScheduler.advanceTimeBy(1, TimeUnit.MINUTES); // (6) 시작
        testObserver.assertValues(responseKweather, responseKweather2, responseKweather3); // (6) 끝

        verify(weatherRepository, times(1)).getWeatherKma(); // (7) 시작
        verify(weatherRepository, times(3)).getWeatherKweather(); // (7) 끝
    }
}

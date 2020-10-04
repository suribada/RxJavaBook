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

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Mock
    WeatherRepository weatherRepository;

    @InjectMocks
    WeatherUseCase weatherUseCase;

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
    public void getWeatherKmaPeriodically_sameResponse() throws InterruptedException {
        Weather response = Weather.create(10, "흐리고 비", 11.0f);
        Weather response2 = Weather.create(10, "흐리고 비", 11.0f);

        when(weatherRepository.getWeatherKma()).thenReturn(Observable.just(response));
        TestObserver testObserver = weatherUseCase.getWeatherKmaPeriodically().test();
        testScheduler.advanceTimeBy(1, TimeUnit.MINUTES); // (1)
        testObserver.assertValue(response); // (2)

        when(weatherRepository.getWeatherKma()).thenReturn(Observable.just(response2));
        testScheduler.advanceTimeBy(1, TimeUnit.MINUTES); // (3)
        testObserver.assertValueCount(1); // (4)
    }

    @Test
    public void getWeatherKmaPeriodically_differentResponse() throws InterruptedException {
        Weather response = Weather.create(10, "흐리고 비", 11.0f); // (1) 시작
        Weather response2 = Weather.create(12, "흐림", 11.0f);
        Weather response3 = Weather.create(13, "흐림", 11.0f); // (1) 끝

        when(weatherRepository.getWeatherKma()).thenReturn(Observable.just(response)); // (2)
        TestObserver testObserver = weatherUseCase.getWeatherKmaPeriodically().test();
        testScheduler.advanceTimeBy(30, TimeUnit.SECONDS); // (3)
        testObserver.assertValue(response);

        when(weatherRepository.getWeatherKma()).thenReturn(Observable.just(response2)); // (4)
        testScheduler.advanceTimeBy(1, TimeUnit.MINUTES); // (5)
        testObserver.assertValues(response, response2); // (6)

        when(weatherRepository.getWeatherKma()).thenReturn(Observable.just(response3)); // (7)
        testScheduler.advanceTimeBy(1, TimeUnit.MINUTES); // (8)
        testObserver.assertValues(response, response2, response3); // (9)
    }

    @Test
    public void getFastWeatherPeriodically() {
        Weather response = Weather.create(10, "흐리고 비", 11.0f); // (1) 시작
        Weather response2 = Weather.create(12, "흐림", 11.0f);

        when(weatherRepository.getWeatherKma())
                .thenReturn(Observable.just(response).delay(20, TimeUnit.SECONDS)); // (2)
        when(weatherRepository.getWeatherKweather())
                .thenReturn(Observable.just(response2).delay(15, TimeUnit.SECONDS)); // (2)
        TestObserver testObserver = weatherUseCase.getFastWeatherPeriodically().test();
        testScheduler.advanceTimeBy(10, TimeUnit.SECONDS); // (3)
        testObserver.assertNoValues();

        testScheduler.advanceTimeBy(20, TimeUnit.SECONDS); // (3)
        testObserver.assertValue(response2);

        testScheduler.advanceTimeTo(1, TimeUnit.MINUTES); // (3)
        testObserver.assertValue(response2);

        when(weatherRepository.getWeatherKweather())
                .thenReturn(Observable.just(response).delay(25, TimeUnit.SECONDS)); // (2)
        testScheduler.advanceTimeBy(1, TimeUnit.MINUTES); // (5)
        testObserver.assertValue(response2)
                .assertNotComplete()
                .assertNoErrors();
    }
}

package com.suribada.rxjavabook.chap11;

import com.suribada.rxjavabook.api.model.Weather;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.schedulers.TestScheduler;

import static org.mockito.Mockito.when;

public class WeatherUseCaseRuleTest {

    @Rule // (1) 시작
    public MockitoRule mockito = MockitoJUnit.rule(); // (1) 끝

    @Rule
    public TestSchedulerRule testSchedulerRule = new TestSchedulerRule();

    @Mock // (2) 시작
    WeatherRepository weatherRepository; // (2) 끝

    @InjectMocks // (3) 시작
    WeatherUseCase weatherUseCase; // (3) 끝

    @Test
    public void getWeatherKmaPeriodically_sameResponse() {
        TestScheduler testScheduler = testSchedulerRule.getTestScheduler();
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
}

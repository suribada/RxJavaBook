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

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Rule
    public TestSchedulerRule testSchedulerRule = new TestSchedulerRule(); // (1)

    @Mock
    WeatherRepository weatherRepository;

    @InjectMocks
    WeatherUseCase weatherUseCase;

    @Test
    public void getWeatherKmaPeriodically_sameResponse() {
        TestScheduler testScheduler = testSchedulerRule.getTestScheduler(); // (2)
        Weather response = Weather.create(10, "흐리고 비", 11.0f);
        Weather response2 = Weather.create(10, "흐리고 비", 11.0f);

        when(weatherRepository.getWeatherKma()).thenReturn(Observable.just(response));
        TestObserver testObserver = weatherUseCase.getWeatherKmaPeriodically().test();
        testScheduler.advanceTimeBy(0, TimeUnit.MINUTES);
        testObserver.assertValue(response); // (2) 끝

        when(weatherRepository.getWeatherKma()).thenReturn(Observable.just(response2));
        testScheduler.advanceTimeBy(1, TimeUnit.MINUTES);
        testObserver.assertValueCount(1);
    }
}

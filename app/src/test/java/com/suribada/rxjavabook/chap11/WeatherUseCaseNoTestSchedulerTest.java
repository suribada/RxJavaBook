package com.suribada.rxjavabook.chap11;

import com.suribada.rxjavabook.api.model.Weather;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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

/**
 * TestScheduler를 사용하지 않은 버전
 * 이렇게 하지 말 것
 */
public class WeatherUseCaseNoTestSchedulerTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Mock
    WeatherRepository weatherRepository;

    @InjectMocks
    WeatherUseCase weatherUseCase;

    @Test
    public void getWeatherKmaPeriodically_sameResponse() throws InterruptedException {
        Weather response = Weather.create(10, "흐리고 비", 11.0f); // (1) 시작
        Weather response2 = Weather.create(10, "흐리고 비", 11.0f); // (1) 끝

        when(weatherRepository.getWeatherKma()).thenReturn(Observable.just(response)); // (2)
        TestObserver testObserver = weatherUseCase.getWeatherKmaPeriodically().test(); // (3)
        testObserver.await(10, TimeUnit.SECONDS); // (4)
        testObserver.assertValueCount(1); // (5)

        when(weatherRepository.getWeatherKma()).thenReturn(Observable.just(response2)); // (6)
        testObserver.await(60, TimeUnit.SECONDS); // (7)
        testObserver.assertValueCount(1); // (8)
    }

    @Test
    public void getWeatherKmaPeriodically_differentResponse() throws InterruptedException {
        Weather response = Weather.create(10, "흐리고 비", 11.0f); // (1) 시작
        Weather response2 = Weather.create(12, "흐림", 11.0f); // (1) 끝

        when(weatherRepository.getWeatherKma()).thenReturn(Observable.just(response)); // (2) 시작
        TestObserver testObserver = weatherUseCase.getWeatherKmaPeriodically().test();
        testObserver.await(10, TimeUnit.SECONDS);
        testObserver.assertValueCount(1); // (2) 끝

        when(weatherRepository.getWeatherKma()).thenReturn(Observable.just(response2)); // (3) 시작
        testObserver.await(60, TimeUnit.SECONDS);
        testObserver.assertValueCount(2)
                .assertValueAt(1, response2); // (3) 끝
    }

    @Test
    public void getWeatherKmaPeriodically_problem() throws InterruptedException {
        Weather response = Weather.create(10, "흐리고 비", 11.0f);
        when(weatherRepository.getWeatherKma()).thenReturn(Observable.just(response));
        // await()는 onComplete나 onError가 호출돼야 하기 때문에 여기서는 사실상 무한대기
        weatherUseCase.getWeatherKmaPeriodically().test()
                .await()
                .assertNotComplete();
    }

    @Test
    public void getWeatherKmaPeriodically_await() throws InterruptedException {
        Weather response = Weather.create(10, "흐리고 비", 11.0f);
        when(weatherRepository.getWeatherKma()).thenReturn(Observable.just(response));
        TestObserver testObserver = weatherUseCase.getWeatherKmaPeriodically().test();
        testObserver.awaitCount(3) // timeout이 5초라서 사실상 의미없음
                .assertValues(response)
                .assertNotComplete();
    }
}

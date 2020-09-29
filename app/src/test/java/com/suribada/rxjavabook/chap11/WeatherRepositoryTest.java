package com.suribada.rxjavabook.chap11;

import com.suribada.rxjavabook.api.model.Weather;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.sql.Time;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.schedulers.TestScheduler;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class WeatherRepositoryTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Mock
    KmaDataSource kmaDataSource;

    @Mock
    KweatherDataSource kweatherDataSource;

    @InjectMocks
    WeatherRepository weatherRepository;

    @Test
    public void getWeatherKma_blocking_normal() {
        Weather response = Weather.create(10, "흐리고 비", 11.0f);
        when(kmaDataSource.getWeather()).thenReturn(Observable.just(response));
        Weather result = weatherRepository.getWeatherKma().blockingFirst();
        assertEquals(response, result);
    }

    /**
     * 테스트 실패
     * blockingFirst 자체가 null이면 안 됨
     */
    @Test
    public void getWeatherKma_blocking_empty() {
        Weather response = Weather.create(-1, "what's problem", 11.0f);
        when(kmaDataSource.getWeather()).thenReturn(Observable.just(response));
        Weather result = weatherRepository.getWeatherKma().blockingFirst();
        assertNull(result);
    }

    @Test(expected = NoSuchElementException.class)
    public void getWeatherKma_blocking_empty_error() {
        Weather response = Weather.create(-1, "what's problem", 11.0f);
        when(kmaDataSource.getWeather()).thenReturn(Observable.just(response));
        Weather result = weatherRepository.getWeatherKma().blockingFirst();
        assertNull(result);
    }

    /**
     * 테스트 실패
     * java.lang.Exception: Unexpected exception, expected<java.util.concurrent.TimeoutException> but was<java.lang.RuntimeException>
     */
    @Test(expected = TimeoutException.class)
    public void getWeatherKma_blocking_error() {
        when(kmaDataSource.getWeather()).thenReturn(Observable.error(new TimeoutException()));
        Weather result = weatherRepository.getWeatherKma().blockingFirst();
        assertNull(result);
    }

    @Test(expected = RuntimeException.class)
    public void getWeatherKma_blocking_error2() {
        when(kmaDataSource.getWeather()).thenReturn(Observable.error(new TimeoutException()));
        Weather result = weatherRepository.getWeatherKma().blockingFirst();
        assertNull(result);
    }

    @Test
    public void getWeatherKma_normal() {
        Weather response = Weather.create(10, "흐리고 비", 11.0f);
        when(kmaDataSource.getWeather()).thenReturn(Observable.just(response));
        weatherRepository.getWeatherKma().test()
                .assertValue(response)
                .assertComplete();

    }

    /**
     * assertEmpty()는 문제가 발생한다. java.lang.AssertionError: Completed! (latch = 0, values = 0, errors = 0, completions = 1)
     * notComplete라는 조건이 있는 것이다..
     * assertNoValues()는 문제가 없다.
     */
    @Test
    public void getWeatherKma_empty() {
        Weather response = Weather.create(-1, "what's problem", 11.0f);
        when(kmaDataSource.getWeather()).thenReturn(Observable.just(response));
        weatherRepository.getWeatherKma().test()
                .assertNoValues();
    }

    @Test
    public void getWeatherKma_error() {
        when(kmaDataSource.getWeather()).thenReturn(Observable.error(new TimeoutException()));
        weatherRepository.getWeatherKma().test()
                .assertError(TimeoutException.class);
    }

}

package com.suribada.rxjavabook.chap11;

import com.suribada.rxjavabook.api.model.Weather;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WeatherUseCase {

    private WeatherRepository weatherRepository;

    public WeatherUseCase(WeatherRepository weatherRepository) { // (1) 시작
        this.weatherRepository = weatherRepository;
    } // (1) 끝

    public Observable<Weather> getWeatherKmaPeriodically() { // (2) 시작
        return Observable.interval(0L, 1, TimeUnit.MINUTES, Schedulers.io())
                .flatMap(ignored -> weatherRepository.getWeatherKma())
                .distinctUntilChanged();
    }

    public Observable<Weather> getWeatherKweatherPeriodically() {
        return Observable.interval(0L, 1, TimeUnit.MINUTES, Schedulers.io())
                .flatMap(ignored -> weatherRepository.getWeatherKweather())
                .distinctUntilChanged();
    } // (2) 끝

    public Observable<Weather> getFastWeatherPeriodically() {
        return Observable.ambArray(getWeatherKmaPeriodically(), // (3) 시작
                getWeatherKweatherPeriodically()); // (3) 끝
    }

    public Observable<Weather> getWeatherKmaPeriodically(Scheduler scheduler) {
        return Observable.interval(0L, 1, TimeUnit.MINUTES, scheduler) // (1)
                .flatMap(ignored -> weatherRepository.getWeatherKma())
                .distinctUntilChanged();
    }

    public Observable<Weather> getWeatherKweatherPeriodically(Scheduler scheduler) {
        return Observable.interval(0L, 1, TimeUnit.MINUTES, scheduler)
                .flatMap(ignored -> weatherRepository.getWeatherKweather())
                .distinctUntilChanged();
    }

}

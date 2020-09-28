package com.suribada.rxjavabook.chap11;

import com.suribada.rxjavabook.api.model.Weather;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WeatherRepository {

    private KmaDataSource kmaDataSource;
    private KweatherDataSource kweatherDataSource;

    public WeatherRepository(KmaDataSource kmaDataSource, KweatherDataSource kweatherDataSource) {
        this.kmaDataSource = kmaDataSource;
        this.kweatherDataSource = kweatherDataSource;
    }

    public Observable<Weather> getWeatherKma() {
        return kmaDataSource.getWeather()
                .filter(weather -> weather.getWeatherCode() > 0);
    }

    public Observable<Weather> getWeatherKweather() {
        return kweatherDataSource.getWeather()
                .filter(weather -> weather.getWeatherCode() > 0);
    }

    public Observable<Weather> getWeatherKmaPeriodically() {
        return Observable.interval(0L, 1, TimeUnit.MINUTES, Schedulers.io())
                .flatMap(ignored -> getWeatherKma())
                .distinctUntilChanged();
    }



    public Observable<Weather> getWeatherKweatherPeriodically() {
        return Observable.interval(0L, 1, TimeUnit.MINUTES, Schedulers.io())
                .flatMap(ignored -> getWeatherKweather())
                .distinctUntilChanged();
    }

    public Observable<Weather> getFastWeatherPeriodically() {
        return Observable.ambArray(getWeatherKmaPeriodically(), getWeatherKweatherPeriodically());
    }

    public Observable<Weather> getWeatherKmaPeriodically(Scheduler scheduler) {
        return Observable.interval(0L, 1, TimeUnit.MINUTES, scheduler)
                .flatMap(ignored -> getWeatherKma())
                .distinctUntilChanged();
    }

    public Observable<Weather> getWeatherKweatherPeriodically(Scheduler scheduler) {
        return Observable.interval(0L, 1, TimeUnit.MINUTES, scheduler)
                .flatMap(ignored -> getWeatherKweather())
                .distinctUntilChanged();
    }

}

package com.suribada.rxjavabook.chap11;

import com.suribada.rxjavabook.api.model.Weather;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WeatherRepository {

    private KmaDataSource kmaDataSource;
    private KweatherDataSource kweatherDataSource;

    public WeatherRepository(KmaDataSource kmaDataSource,
                             KweatherDataSource kweatherDataSource) { // (1) 시작
        this.kmaDataSource = kmaDataSource;
        this.kweatherDataSource = kweatherDataSource;
    } // (1) 끝

    public Observable<Weather> getWeatherKma() { // (2) 시작
        return kmaDataSource.getWeather()
                .filter(weather -> weather.getWeatherCode() > 0);
    }

    public Observable<Weather> getWeatherKweather() {
        return kweatherDataSource.getWeather()
                .filter(weather -> weather.getWeatherCode() > 0);
    } // (2) 끝

}
